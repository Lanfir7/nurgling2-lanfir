package nurgling.actions;

import haven.Coord;
import haven.Gob;
import haven.WItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.areas.NArea;
import nurgling.tasks.FollowAndPose;
import nurgling.tasks.HandIsFree;
import nurgling.tools.Context;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;
import java.util.Arrays;

import static haven.OCache.posres;

public class PrepareWorkStation implements Action
{
    public PrepareWorkStation(String name)
    {
        this.name = name;
    }

    String name;
    @Override
    public Results run(NGameUI gui) throws InterruptedException
    {
        Gob ws = Finder.findGob(new NAlias(name));
        if(ws == null)
            return Results.ERROR("NO WORKSTATION");
        else
        {
            if(name.contains("crucible"))
            {
                if(fillCrucible(ws,gui))
                    new LightGob(new ArrayList<>(Arrays.asList(ws)),4).run(gui);
            }
        }
        return Results.SUCCESS();
    }

    boolean fillCrucible(Gob crucible, NGameUI gui) throws InterruptedException
    {
        if((crucible.ngob.getModelAttribute()&2)==2)
            return true;
        int count = 1;
        if(NUtils.getGameUI().getInventory().getFreeSpace()==0)
            return false;

        if(NUtils.getGameUI().getInventory().getItems("Coal").isEmpty()) {
            int target_size = count;
            while (target_size != 0 && NUtils.getGameUI().getInventory().getFreeSpace() != 0) {
                ArrayList<Gob> piles = Finder.findGobs(NArea.findSpec(Specialisation.SpecName.fuel.toString(), "Coal"), new NAlias("stockpile"));
                if (piles.isEmpty()) {
                    if (gui.getInventory().getItems().isEmpty())
                        return false;
                    else
                        break;
                }
                piles.sort(NUtils.d_comp);

                Gob pile = piles.get(0);
                new PathFinder(pile).run(gui);
                new OpenTargetContainer("Stockpile", pile).run(gui);
                TakeItemsFromPile tifp;
                (tifp = new TakeItemsFromPile(pile, gui.getStockpile(), Math.min(target_size, gui.getInventory().getFreeSpace()))).run(gui);
                new CloseTargetWindow(NUtils.getGameUI().getWindow("Stockpile")).run(gui);
                target_size = target_size - tifp.getResult();
            }
        }
        new PathFinder(crucible).run(gui);
        ArrayList<WItem> fueltitem = NUtils.getGameUI().getInventory().getItems("Coal");
        if (fueltitem.isEmpty()) {
            return false;
        }
        for(int i=0; i<1;i++) {
            NUtils.takeItemToHand(fueltitem.get(i));
            NUtils.activateItem(crucible);
            NUtils.getUI().core.addTask(new HandIsFree(NUtils.getGameUI().getInventory()));
        }
        return true;
    }
}
