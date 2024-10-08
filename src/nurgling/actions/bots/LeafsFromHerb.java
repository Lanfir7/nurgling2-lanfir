package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.tools.Container;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LeafsFromHerb implements Action {

    // Определяем алиасы для столов хербалиста и стокпайлов для листьев
    private static final NAlias herbalistTables = new NAlias("gfx/terobjs/htable");
    private static final NAlias leafPiles = new NAlias("gfx/terobjs/stockpile-leaf");
    private static final NAlias teaLeaves = new NAlias(new ArrayList<>(Arrays.asList("Fresh Tea Leaves", "Green Tea Leaves")));

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Выбор области со столами хербалиста
        SelectArea herbTableArea;
        NUtils.getGameUI().msg("Please select area with herbalist tables");
        (herbTableArea = new SelectArea()).run(gui);

        // Выбор области для стокпайлов
        SelectArea leafPileArea;
        NUtils.getGameUI().msg("Please select area for leaf piles");
        (leafPileArea = new SelectArea()).run(gui);

        // Находим столы хербалиста в указанной области
        ArrayList<Gob> herbTables = Finder.findGobs(herbTableArea.getRCArea(), herbalistTables);
        if (herbTables.isEmpty()) {
            NUtils.getGameUI().msg("No herbalist tables found in selected area!");
            return Results.FAIL();
        }

        // Сортируем столы для оптимизации процесса
        herbTables.sort(NUtils.d_comp);

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

        // Перемещение листьев в стокпайлы
        new TransferToPiles(leafPileArea.getRCArea(), teaLeaves).run(gui);

        NUtils.getGameUI().msg("Successfully transferred leaves from herbalist tables to leaf piles.");
        return Results.SUCCESS();
    }
}
