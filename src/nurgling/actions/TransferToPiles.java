package nurgling.actions;

import haven.*;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NInventory;
import nurgling.NUtils;
import nurgling.tasks.*;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.tools.NParser;

import java.util.ArrayList;

public class TransferToPiles implements Action{

    NAlias items;

    Pair<Coord2d,Coord2d> out;

    public TransferToPiles(Pair<Coord2d,Coord2d> out, NAlias items) {
        this.out = out;
        this.items = items;
    }


    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<WItem> witems;
        NAlias pileName;
        if (!(witems = gui.getInventory().getItems(items)).isEmpty() ) {
                Gob target = null;
                for (Gob gob : Finder.findGobs(out, pileName = getStockpileName(items))) {
                    if (gob.ngob.getModelAttribute() != 31) {
                        if(PathFinder.isAvailable(gob)) {
                            target = gob;
                            new PathFinder(target).run(gui);
                            witems = gui.getInventory().getItems(items);
                            int size = witems.size();
                            System.out.println(" witems" + size);
                            new OpenTargetContainer("Stockpile", target).run(gui);
                            int target_size = Math.min(size,gui.getStockpile().getFreeSpace());
                            int fullSize = gui.getInventory().getItems().size();
                            for (int i = 0; i < target_size; i++)
                            {
                                witems.get(i).item.wdgmsg("transfer", Coord.z);
                            }
                            NUtils.getUI().core.addTask(new FilledPile(target, items, target_size, size));
                            NUtils.getUI().core.addTask(new WaitTargetSize(NUtils.getGameUI().getInventory(), fullSize - target_size));
                            System.out.println(" WaitAnotherSize" + fullSize);
                            if((witems = gui.getInventory().getItems(items)).isEmpty())
                                return Results.SUCCESS();
                        }
                    }
                }

                while(!(gui.getInventory().getItems(items)).isEmpty()) {
                    PileMaker pm;

                    (pm = new PileMaker(out, items, pileName)).run(gui);
                    Gob pile = pm.getPile();
                    witems = gui.getInventory().getItems(items);
                    int size = witems.size();
                    new OpenTargetContainer("Stockpile", pile).run(gui);
                    int target_size = Math.min(size, gui.getStockpile().getFreeSpace());
                    int fullSize = gui.getInventory().getItems().size();
                    System.out.println("gui.getInventory().getItems()" + fullSize);
                    for (int i = 0; i < target_size; i++)
                    {
                        witems.get(i).item.wdgmsg("transfer", Coord.z);
                    }
                    NUtils.getUI().core.addTask(new FilledPile(pile, items, target_size, size));
                    NUtils.getUI().core.addTask(new WaitAnotherSize(NUtils.getGameUI().getInventory(), fullSize));

                }
            }
        return Results.SUCCESS();
        }



    NAlias getStockpileName(NAlias items) {
        if (NParser.checkName(items.getDefault(), new NAlias("soil"))) {
            return new NAlias("gfx/terobjs/stockpile-soil");
        } else if (NParser.checkName(items.getDefault(), new NAlias("board"))) {
            return new NAlias("gfx/terobjs/stockpile-board");
        } else if (NParser.checkName(items.getDefault(), new NAlias("pumpkin"))) {
            return new NAlias("gfx/terobjs/stockpile-pumpkin");
        } else if (NParser.checkName(items.getDefault(), new NAlias("metal"))) {
            return new NAlias("gfx/terobjs/stockpile-metal");
        } else if (NParser.checkName(items.getDefault(), new NAlias("brick"))) {
            return new NAlias("gfx/terobjs/stockpile-brick");
        } else if (NParser.checkName(items.getDefault(), new NAlias("leaf"))) {
            return new NAlias("gfx/terobjs/stockpile-leaf");
        } else if (NParser.checkName(items.getDefault(), new NAlias("Hemp Cloth"))) {
            return new NAlias("gfx/terobjs/stockpile-cloth");
        } else if (NParser.checkName(items.getDefault(), new NAlias("Linen Cloth"))) {
            return new NAlias("gfx/terobjs/stockpile-cloth");
        }
        else
            return new NAlias("stockpile");
    }
}
