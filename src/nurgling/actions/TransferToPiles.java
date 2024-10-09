package nurgling.actions;

import haven.*;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.tasks.*;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.tools.NParser;

import java.util.ArrayList;

public class TransferToPiles implements Action {

    NAlias items;
    Pair<Coord2d, Coord2d> out;
    Integer th = -1; // Для фильтрации по качеству

    // Конструктор для передачи зоны и предметов
    public TransferToPiles(Pair<Coord2d, Coord2d> out, NAlias items) {
        this.out = out;
        this.items = items;
    }

    // Конструктор с фильтром по качеству
    public TransferToPiles(Pair<Coord2d, Coord2d> out, NAlias items, Integer th) {
        this.out = out;
        this.items = items;
        this.th = th;
    }

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<WItem> witems;
        NAlias pileName;

        if (!getItems(gui, th).isEmpty()) {
            Gob target = null;

            // Ищем существующие стокпайлы
            for (Gob gob : Finder.findGobs(out, pileName = getStockpileName(items))) {
                if (gob.ngob.getModelAttribute() != 31) { // Убедимся, что стокпайл не заполнен
                    if (PathFinder.isAvailable(gob)) {
                        target = gob;
                        new PathFinder(target).run(gui);

                        // Обновляем список предметов и фильтруем по качеству
                        witems = getItems(gui, th);


                        int size = witems.size();
                        new OpenTargetContainer("Stockpile", target).run(gui);
                        int target_size = Math.min(size, gui.getStockpile().getFreeSpace());
                        int fullSize = gui.getInventory().getItems().size();

                        // Перемещаем предметы в стокпайл
                        for (int i = 0; i < target_size; i++) {
                            witems.get(i).item.wdgmsg("transfer", Coord.z);
                        }

                        NUtils.getUI().core.addTask(new FilledPile(target, items, target_size, size, th));
                        NUtils.getUI().core.addTask(new WaitTargetSize(NUtils.getGameUI().getInventory(), fullSize - target_size));

                        // Если инвентарь пуст, завершаем
                        if ((witems = getItems(gui, th)).isEmpty()) {
                            return Results.SUCCESS();
                        }
                    }
                }
            }

            // Создаем новый стокпайл и продолжаем перемещать предметы, пока они остаются в инвентаре
            while (!getItems(gui, th).isEmpty()) {
                PileMaker pm = new PileMaker(out, items, pileName);
                pm.run(gui);

                witems = getItems(gui, th);

                Gob pile = pm.getPile();
                while (!getItems(gui, th).isEmpty() ) {
                    // Обновляем предметы и фильтруем по качеству
                    int size = witems.size();

                    new OpenTargetContainer("Stockpile", pile).run(gui);
                    int target_size = Math.min(size, gui.getStockpile().getFreeSpace());

                    if (target_size > 0 || !getItems(gui, th).isEmpty()) {
                        int fullSize = gui.getInventory().getItems().size();

                        // Перемещаем предметы в новый стокпайл
                        for (int i = 0; i < target_size; i++) {
                            witems.get(i).item.wdgmsg("transfer", Coord.z);
                        }

                        NUtils.getUI().core.addTask(new FilledPile(pile, items, target_size, size, th));
                        NUtils.getUI().core.addTask(new WaitAnotherSize(NUtils.getGameUI().getInventory(), fullSize));
                    } else {
                        break; // Выход из цикла, если нет свободного места
                    }
                }
            }
        }
        return Results.SUCCESS();
    }

    // Получаем имя стокпайла по типу предмета
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
        } else {
            return new NAlias("stockpile");
        }
    }
    private ArrayList<WItem> getItems(NGameUI gui, Integer th) throws InterruptedException {
        if (th == -1) {
            return gui.getInventory().getItems(items);
        } else {
            return gui.getInventory().getItems(items, th);
        }
    }
}
