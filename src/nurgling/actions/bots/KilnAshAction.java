package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import haven.UI;
import haven.WItem;
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

public class KilnAshAction implements Action {
    private static final NAlias blockPiles = new NAlias("gfx/terobjs/stockpile-wblock");
    private static final NAlias kilnsAlias = new NAlias("gfx/terobjs/kiln");
    private static final NAlias blockItems = new NAlias(new ArrayList<>(Arrays.asList("Block")));

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        NUtils.getGameUI().msg("Starting kiln ash collection...");

        // Находим зону с печами (Kilns)
        NArea kilnArea = NArea.findSpec(Specialisation.SpecName.kiln.toString());
        // Находим печи (Kilns) в этой зоне
        ArrayList<Gob> kilns = Finder.findGobs(kilnArea, kilnsAlias);
        if (kilns.isEmpty()) {
            NUtils.getGameUI().msg("No kilns found!");
            return Results.FAIL();
        }

        // Создаем контейнеры для каждой печи
        ArrayList<Container> containers = new ArrayList<>();
        for (Gob kiln : kilns) {
            Container container = new Container();
            container.gob = kiln;
            container.cap = "Kiln";
            container.initattr(Container.Space.class);
            container.initattr(Container.FuelLvl.class);
            container.getattr(Container.FuelLvl.class).setMaxlvl(8);
            container.getattr(Container.FuelLvl.class).setFueltype("branch");
            container.initattr(Container.Tetris.class);
            Container.Tetris tetris = container.getattr(Container.Tetris.class);
            ArrayList<Coord> coords = new ArrayList<>();
            coords.add(new Coord(2, 1));

            tetris.getRes().put(Container.Tetris.TARGET_COORD, coords);

            containers.add(container);
        }

        // Освобождаем печи от золы или других предметов, если необходимо
        new FreeContainers(containers).run(gui);

        // Проверяем свободное место в печах
        boolean allContainersFull = true;
        for (Container container : containers) {
            Container.Space space = container.getattr(Container.Space.class);
            if ((Integer) space.getRes().get(Container.Space.FREESPACE) > 0) {
                allContainersFull = false;
                break; // Найдено свободное место
            }
        }

        // Находим стокпайлы с блоками в указанной зоне
        NArea blockPileArea = NArea.findSpec(Specialisation.SpecName.toFire.toString());
        ArrayList<Gob> blockPilesList = Finder.findGobs(blockPileArea, blockPiles);
        if (blockPilesList.isEmpty()) {
            NUtils.getGameUI().msg("No block piles found!");
            return Results.FAIL();
        }

        // Если все печи заполнены, завершаем работу
        if (allContainersFull) {
            NUtils.getGameUI().msg("All kilns are full!");
            return Results.SUCCESS();
        }

        // Теперь раскладываем блоки из стокпайлов в печи
        Results res = null;
        while (res == null || res.IsSuccess()) {
            NUtils.getGameUI().msg("Transferring blocks to kilns...");
            res = new FillContainersFromPiles(containers, blockPileArea, blockItems).run(gui);

            if (res == null || !res.IsSuccess()) {
                NUtils.getGameUI().msg("Failed to transfer blocks to kilns.");
                return Results.FAIL();
            }

            // Проверяем свободное место в печах после перемещения блоков
//            allContainersFull = true;
//            for (Container container : containers) {
//                Container.Space space = container.getattr(Container.Space.class);
//                if ((Integer) space.getRes().get(Container.Space.FREESPACE) > 0) {
//                    allContainersFull = false;
//                    break; // Найдено свободное место
//                }
//            }
//
//            // Если все печи заполнены, завершаем работу
//            if (allContainersFull) {
//                NUtils.getGameUI().msg("All kilns are full!");
//                break;
//            }
        }

        NUtils.getGameUI().msg("Successfully transferred blocks to kilns.");
        return Results.SUCCESS();
    }
}
