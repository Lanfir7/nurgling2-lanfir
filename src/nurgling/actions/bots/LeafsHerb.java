package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import haven.MenuGrid;
import nurgling.NGameUI;
import nurgling.NInventory;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class LeafsHerb implements Action {
    // Определяем элементы для поиска (стокпайлы и столы хербалиста)
    private static final NAlias leafPiles = new NAlias("gfx/terobjs/stockpile-leaf");
    private static final NAlias herbalistTables = new NAlias("gfx/terobjs/htable");
    private static final NAlias teaLeaves = new NAlias(new ArrayList<>(Arrays.asList("Fresh Tea Leaves")));


    @Override
    public Results run(NGameUI gui) throws InterruptedException {

        NUtils.getGameUI().msg("Starting leaf collection...");
       //NArea leafArea = NArea.findIn("Fresh Tea Leaves");
        NArea leafArea = NArea.findSpec(Specialisation.SpecName.leafs.toString());



        // Находим область для хербалист столов
        NArea herbTableArea = NArea.findSpec(Specialisation.SpecName.htable.toString());

        // Находим столы хербалиста
        ArrayList<Gob> herbTables = Finder.findGobs(herbTableArea, herbalistTables);
        if (herbTables.isEmpty()) {
            NUtils.getGameUI().msg("No herbalist tables found!");
            return Results.FAIL();
        }

        // Создаем контейнеры для каждого стола хербалиста
        ArrayList<Container> containers = new ArrayList<>();
        for (Gob table : herbTables) {
            Container container = new Container();
            container.gob = table;
            container.cap = "Herbalist Table";
            container.initattr(Container.Space.class);
            containers.add(container);
        }

        new FreeContainers(containers).run(gui);

        // Проверяем свободное место на столах
        boolean allContainersFull = true;
        for (Container container : containers) {
            Container.Space space = container.getattr(Container.Space.class);
            if ((Integer) space.getRes().get(Container.Space.FREESPACE) > 0) {
                allContainersFull = false;
                break; // Найдено свободное место
            }
        }
        // Находим стокпайлы с листьями
        ArrayList<Gob> leafPilesList = Finder.findGobs(leafArea, leafPiles);
        if (leafPilesList.isEmpty()) {
            NUtils.getGameUI().msg("No leaf piles found!");
            return Results.FAIL();
        }
        // Если все контейнеры заполнены, бот завершает работу
        if (allContainersFull) {
            NUtils.getGameUI().msg("All herbalist tables are full!");
            return Results.SUCCESS();
        }

        // Теперь раскладываем листья на столы хербалиста
        Results res = null;
        while (res == null || res.IsSuccess()) {
            NUtils.getGameUI().msg("Transferring leaves to herbalist tables...");
            res = new FillContainersFromPiles(containers, leafArea, teaLeaves).run(gui);

            if (res == null || !res.IsSuccess()) {
                NUtils.getGameUI().msg("Failed to transfer leaves to herbalist tables.");
                return Results.FAIL();
            }

            // Проверяем свободное место на столах после перемещения
            allContainersFull = true;
            for (Container container : containers) {
                Container.Space space = container.getattr(Container.Space.class);
                if ((Integer) space.getRes().get(Container.Space.FREESPACE) > 0) {
                    allContainersFull = false;
                    break; // Найдено свободное место
                }
            }

            // Если все контейнеры заполнены, бот завершает работу
            if (allContainersFull) {
                NUtils.getGameUI().msg("All herbalist tables are full!");
                break;
            }
        }

        NUtils.getGameUI().msg("Successfully transferred leaves to herbalist tables.");
        return Results.SUCCESS();
    }
}