package nurgling.widgets.bots;

import haven.*;
import nurgling.NUtils;

public class DreamCollector extends Window {

    private int collectedDreams = 0; // Количество собранных дримов
    private int cycles = 0; // Количество циклов
    private Label dreamCountLabel; // Текстовое поле для вывода количества дримов
    private Label cycleCountLabel; // Текстовое поле для вывода количества циклов
    private Label statusLabel; // Текстовое поле для вывода текущего статуса
    private boolean isReady = false; // Флаг готовности
    private boolean isRunning = true; // Флаг для отслеживания выполнения

    public DreamCollector() {
        super(new Coord(200, 150), "Dream Counter");
        createDreamCountWindow();
    }

    // Метод для создания окна с метками
    private void createDreamCountWindow() {
        dreamCountLabel = new Label("Collected Dreams: " + collectedDreams);
        this.add(dreamCountLabel, new Coord(10, 10));

        cycleCountLabel = new Label("Cycles completed: " + cycles);
        this.add(cycleCountLabel, new Coord(10, 30));

        statusLabel = new Label("Status: Waiting for the next cycle. Starting...");
        this.add(statusLabel, new Coord(10, 50));

        pack(); // Автоподгонка размеров окна
    }

    // Метод для обновления количества собранных дримов
    public void updateDreamCount(int collected) {
        collectedDreams += collected;
        dreamCountLabel.settext("Collected Dreams: " + collectedDreams);
    }

    // Метод для обновления количества циклов
    public void updateCycleCount() {
        cycles++;
        cycleCountLabel.settext("Cycles completed: " + cycles);
    }

    // Метод для обновления текущего статуса
    public void updateStatus(String status) {
        statusLabel.settext("Status: " + status);
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isRunning() {
        return isRunning;
    }

    // Обработчик закрытия окна
    @Override
    public void wdgmsg(String msg, Object... args) {
        if (msg.equals("close")) {
            isRunning = false; // Останавливаем выполнение
            hide(); // Скрываем окно
            isReady = true;
        }
        super.wdgmsg(msg, args);
    }
}
