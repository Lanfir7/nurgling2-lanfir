package nurgling.actions.bots;

import haven.*;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.bots.DreamCollector;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CollectDream implements Action {

    private DreamCollector dreamCollectorWindow;
    private static final NAlias dream = new NAlias("Dream", "dream");

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Создаем окно для управления сбором дримов
        dreamCollectorWindow = new DreamCollector();
        gui.add(dreamCollectorWindow, new Coord(300, 200));

        // Запоминаем начальную позицию персонажа
        Coord2d initialPosition = NUtils.player().rc;

        // Таймер для повторения выполнения каждые 5 минут
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (dreamCollectorWindow.isRunning()) {
                    try {
                        dreamCollectorWindow.updateStatus("Collecting dreams");
                        performDreamCollection(gui, initialPosition);

                        dreamCollectorWindow.updateCycleCount(); // Увеличиваем количество циклов
                    } catch (Exception e) {
                        e.printStackTrace(); // Обработка исключений
                    }
                } else {
                    timer.cancel(); // Останавливаем таймер, если окно закрыто
                }
            }
        }, 0, 5 * 60 * 1000); // запуск с задержкой каждые 5 минут

        dreamCollectorWindow.updateStatus("Waiting for the next cycle");
        return Results.SUCCESS();
    }

    private void performDreamCollection(NGameUI gui, Coord2d initialPosition) throws InterruptedException {

        ArrayList<Gob> drecaObjects = Finder.findNearbyObjects(new NAlias("gfx/terobjs/dreca"), 100); // Ищем ближайшие Dreca
        if (drecaObjects == null || drecaObjects.isEmpty()) {
            dreamCollectorWindow.updateStatus("No Dreca objects found!");
            return;
        }
        dreamCollectorWindow.updateStatus("Found " + drecaObjects.size() + " Dreca objects.");

        // Собираем ресурс "Dream" из каждого объекта Dreca
        for (Gob dreca : drecaObjects) {
            int initialDreamCount = gui.getInventory().getTotalAmountItems(dream); // Количество дримов перед сбором

            // Выполняем сбор
            new CollectFromGob(dreca, "Harvest", null, new Coord(1, 1), dream, null).run(gui);

            int newDreamCount = gui.getInventory().getTotalAmountItems(dream);
            int collected = newDreamCount - initialDreamCount; // Считаем только реально добавленные дримы

            if (collected > 0) {
                dreamCollectorWindow.updateDreamCount(collected); // Обновляем количество дримов
            }
        }

        // Переносим все мечты в шкафы
        storeDreamsInCupboards(gui);

        // Возвращаемся в исходную точку
        dreamCollectorWindow.updateStatus("Returned to the initial position.");
        new PathFinder(initialPosition).run(gui);
        dreamCollectorWindow.updateStatus("Waiting for the next cycle");
    }
    private void storeDreamsInCupboards(NGameUI gui) throws InterruptedException {
        while (!gui.getInventory().getItems(dream).isEmpty()) {
            if (NArea.findOuts(dream).isEmpty()) {
                ArrayList<Gob> cupboards = Finder.findNearbyObjects(new NAlias("gfx/terobjs/cupboard"), 100); // Ищем ближайшие шкафы
                if (cupboards == null || cupboards.isEmpty()) {
                    dreamCollectorWindow.updateStatus("No cupboards found!");
                    return;
                }
                for (Gob cupboard : cupboards) {
                    new TransferToContainer(cupboard, "Cupboard", dream).run(gui);

                    // Проверяем, остались ли еще дримы в инвентаре
                    if (gui.getInventory().getItems(dream).isEmpty()) {
                        NUtils.getGameUI().msg("Successfully transferred all dreams to cupboards.");
                        break; // Все перемещено, выходим
                    } else {
                        NUtils.getGameUI().msg("Still some dreams left in inventory.");
                    }
                }
            } else {
                new FreeInventory().run(gui);
                break;
            }
        }
    }
}
