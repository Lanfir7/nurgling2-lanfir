package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.tasks.WaitForBurnout;
import nurgling.tools.Container;
import nurgling.tools.Context;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;
import java.util.Arrays;

public class KilnAction implements Action {
    private static final NAlias Piles = new NAlias("gfx/terobjs/stockpile", "gfx/terobjs/chest");
    private static final NAlias blockPiles = new NAlias("gfx/terobjs/stockpile-wblock");
    private static final NAlias boardPiles = new NAlias("gfx/terobjs/stockpile-board");
    private static final NAlias clayPiles = new NAlias("gfx/terobjs/stockpile-clay");
    private static final NAlias bonePiles = new NAlias("gfx/terobjs/stockpile-bone");
    private static final NAlias chests = new NAlias("gfx/terobjs/chest");

    private static final NAlias kilnsAlias = new NAlias("gfx/terobjs/kiln");
    private static final NAlias blockItems = new NAlias(new ArrayList<>(Arrays.asList("Block")));
    private static final NAlias boardItems = new NAlias(new ArrayList<>(Arrays.asList("Board")));
    private static final NAlias clayItems = new NAlias(new ArrayList<>(Arrays.asList("Clay")));
    private static final NAlias boneItems = new NAlias(new ArrayList<>(Arrays.asList("Bone")));
    private static final NAlias chestItems = new NAlias(new ArrayList<>(Arrays.asList("Fishwrap", "Fruitroast","Burst Glutton","Nutjerky","Stuffed Bird","Hand Impression","Toy Chariot")));

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        NUtils.getGameUI().msg("Starting kiln ash collection...");

        NArea blockPileArea = NArea.findSpec(Specialisation.SpecName.toFire.toString());
        ArrayList<Gob> stockpiles = Finder.findGobs(blockPileArea, Piles);
        if (stockpiles.isEmpty()) {
            NUtils.getGameUI().msg("No piles found!");
            return Results.FAIL();
        }

        NAlias selectedItemsAlias = null;
        ArrayList<Coord> coords = new ArrayList<>();
        int maxFuelLvl = 0;
        boolean stockpileBool = true;


        // Проходим по стокпайлам и выбираем нужные параметры в зависимости от их типа

            if (NUtils.checkGobName(stockpiles.get(0), blockPiles)) {
                NUtils.getGameUI().msg("Found block stockpile.");
                selectedItemsAlias = blockItems;
                coords = new ArrayList<>(Arrays.asList(new Coord(1, 2)));
                maxFuelLvl = 8;
            } else if (NUtils.checkGobName(stockpiles.get(0), boardPiles)) {
                NUtils.getGameUI().msg("Found board stockpile.");
                selectedItemsAlias = boardItems;
                coords = new ArrayList<>(Arrays.asList(new Coord(4, 1)));
                maxFuelLvl = 3;
            } else if (NUtils.checkGobName(stockpiles.get(0), clayPiles)) {
                NUtils.getGameUI().msg("Found clay stockpile.");
                selectedItemsAlias = clayItems;
                coords = new ArrayList<>(Arrays.asList(new Coord(1, 1)));
                maxFuelLvl = 2;
            } else if (NUtils.checkGobName(stockpiles.get(0), bonePiles)) {
                NUtils.getGameUI().msg("Found bone stockpile.");
                selectedItemsAlias = boneItems;
                coords = new ArrayList<>(Arrays.asList(new Coord(1, 1)));
                maxFuelLvl = 6;
            } else if (NUtils.checkGobName(stockpiles.get(0), chests)) {
                NUtils.getGameUI().msg("Found chests.");
                selectedItemsAlias = chestItems;
                coords = new ArrayList<>(Arrays.asList(new Coord(1, 1)));
                maxFuelLvl = 5;
                stockpileBool = false;
            }

        NArea kilnArea = NArea.findSpec(Specialisation.SpecName.kiln.toString());
        ArrayList<Gob> kilns = Finder.findGobs(kilnArea, kilnsAlias);
        if (kilns.isEmpty()) {
            NUtils.getGameUI().msg("No kilns found!");
            return Results.FAIL();
        }

        ArrayList<Container> containers = new ArrayList<>();
        for (Gob kiln : kilns) {
            Container container = new Container();
            container.gob = kiln;
            container.cap = "Kiln";
            container.initattr(Container.Space.class);
            container.initattr(Container.FuelLvl.class);
            container.getattr(Container.FuelLvl.class).setMaxlvl(maxFuelLvl);
            container.getattr(Container.FuelLvl.class).setFueltype("branch");
            container.initattr(Container.Tetris.class);
            Container.Tetris tetris = container.getattr(Container.Tetris.class);

            tetris.getRes().put(Container.Tetris.TARGET_COORD, coords);

            containers.add(container);
        }
        ArrayList<Gob> lighted = new ArrayList<>();
        for (Container cont : containers) {
            lighted.add(cont.gob);
        }
        Results res = null;
        while (res == null || res.IsSuccess()) {
            NUtils.getUI().core.addTask(new WaitForBurnout(lighted, 1));
            new FreeContainers(containers).run(gui);

            NUtils.getGameUI().msg("Transferring items to kilns...");
            if (stockpileBool){
                res = new FillContainersFromPiles(containers, blockPileArea.getRCArea(), selectedItemsAlias).run(gui);
            }else{
                //res = new FillContainersFromAreas(containers, selectedItemsAlias, icontext).run(gui);
                Results res2 = null;
                while (true) {
                    Context icontext = new Context();
                    for (Gob sm : stockpiles) {
                        Container cand = new Container();
                        cand.gob = sm;
                        cand.cap = Context.contcaps.get(cand.gob.ngob.name);
                        cand.initattr(Container.Space.class);
                        cand.initattr(Container.TargetItems.class);
                        cand.getattr(Container.TargetItems.class).addTarget("Fishwrap");
                        cand.getattr(Container.TargetItems.class).addTarget("Fruitroast");
                        cand.getattr(Container.TargetItems.class).addTarget("Burst Glutton");
                        cand.getattr(Container.TargetItems.class).addTarget("Nutjerky");
                        cand.getattr(Container.TargetItems.class).addTarget("Stuffed Bird");
                        cand.getattr(Container.TargetItems.class).addTarget("Hand Impression");
                        cand.getattr(Container.TargetItems.class).addTarget("Toy Chariot");
                        icontext.icontainers.add(cand);
                    }
                    new FillContainersFromAreas(containers, selectedItemsAlias, icontext).run(gui);
                    if (!anyContainerHasItems(icontext.icontainers, selectedItemsAlias)) {
                        break;
                    }

                }
            }
            ArrayList<Container> forFuel = new ArrayList<>();
            for(Container container: containers) {
                Container.Space space = container.getattr(Container.Space.class);
                if(!space.isEmpty())
                    forFuel.add(container);
            }
            new FuelToContainers(forFuel).run(gui);

            ArrayList<Gob> flighted = new ArrayList<>();
            for (Container cont : forFuel) {
                flighted.add(cont.gob);
            }
            if (!new LightGob(flighted, 1).run(gui).IsSuccess())
                return Results.ERROR("I can't start a fire");

        }

        NUtils.getGameUI().msg("Successfully kilns.");
        return Results.SUCCESS();
    }
    private boolean anyContainerHasItems(ArrayList<Container> containers, NAlias items) {
        for (Container container : containers) {
            Container.TargetItems targetItems = container.getattr(Container.TargetItems.class);
            if (targetItems != null && targetItems.getTargets(items) > 0) {
                return true;
            }
        }
        return false;
    }
}
