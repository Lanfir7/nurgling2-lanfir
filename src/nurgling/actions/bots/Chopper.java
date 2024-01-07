package nurgling.actions.bots;

import haven.Gob;
import haven.UI;
import haven.Widget;
import haven.Window;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.conf.NChopperProp;
import nurgling.tasks.WaitCheckable;
import nurgling.tasks.WaitChopperState;
import nurgling.tasks.WaitPos;
import nurgling.tasks.WaitPose;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.bots.Checkable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chopper implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        nurgling.widgets.bots.Chopper w = null;
        NChopperProp prop = null;
        try {
            NUtils.getUI().core.addTask(new WaitCheckable( NUtils.getGameUI().add((w = new nurgling.widgets.bots.Chopper()), UI.scale(200,200))));
            prop = w.prop;
        }
        catch (InterruptedException e)
        {
            throw e;
        }
        finally {
            if(w!=null)
                w.destroy();
        }
        if(prop == null)
        {
            return Results.ERROR("No config");
        }
        SelectArea insa;
        NUtils.getGameUI().msg("Please select area for deforestation");
        (insa = new SelectArea()).run(gui);
        NAlias pattern = prop.stumps ? new NAlias(new ArrayList<String>(List.of("gfx/terobjs/tree")),new ArrayList<String>(Arrays.asList("log","oldtrunk"))) :
                new NAlias(new ArrayList<String>(List.of("gfx/terobjs/tree")),new ArrayList<String>(Arrays.asList("log", "oldtrunk", "stump")));
        ArrayList<Gob> trees;
        while (!(trees = Finder.findGobs(insa.getRCArea(),pattern)).isEmpty())
        {
            trees.sort(NUtils.d_comp);

            Gob tree = trees.get(0);
            new PathFinder(tree).run(gui);
            //TODO equip
            while (Finder.findGob(tree.id)!=null)
            {
                new SelectFlowerAction("Chop", tree).run(gui);
                NUtils.getUI().core.addTask(new WaitPose(NUtils.player(),"gfx/borka/treechop"));
                WaitChopperState wcs = new WaitChopperState(tree, prop);
                NUtils.getUI().core.addTask(wcs);
                switch (wcs.getState())
                {
                    case TREENOTFOUND:
                        break;
                    case TIMEFORDRINK: {
                        if(prop.autorefill)
                        {
                            if(FillWaterskins.checkIfNeed())
                                new FillWaterskins().run(gui);
                            new PathFinder(tree).run(gui);
                        }
                        new Drink(0.9).run(gui);
                        break;
                    }
                    case TIMEFOREAT:
                    {
                        new AutoEater().run(gui);
                        break;
                    }
                    case DANGER:
                        return Results.ERROR("SOMETHING WRONG, STOP WORKING");

                }
            }
        }

        return Results.SUCCESS();
    }
}
