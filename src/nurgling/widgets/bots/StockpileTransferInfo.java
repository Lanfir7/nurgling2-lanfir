package nurgling.widgets.bots;

import haven.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockpileTransferInfo extends Window {

    private Label statusLabel; // Текущий статус
    private ArrayList<Label> itemInfoLabels = new ArrayList<>(); // Список меток для отображения информации
    private Coord labelCoord = new Coord(10, 30); // Начальная позиция для меток
    private Button resetButton; // Кнопка для сброса информации

    public StockpileTransferInfo() {
        super(new Coord(300, 250), "Stockpile Transfer Info");
        createInfoWindow();
    }

    private void createInfoWindow() {
        statusLabel = new Label("Status: Waiting to start...");
        this.add(statusLabel, new Coord(10, 10));

        // Добавляем кнопку "Обнулить"
        resetButton = new Button(50, "Reset") {
            @Override
            public void click() {
                resetInfo(); // Действие по сбросу данных
            }
        };
        this.add(resetButton, new Coord(150, 5)); // Размещаем кнопку рядом со статусом

        pack(); // Автоподгонка размеров окна
    }

    public void updateStatus(String status) {
        statusLabel.settext("Status: " + status);
    }

    public void updateItemInfo(Map<String, Integer> itemSummary) {
        // Очищаем предыдущие метки
        for (Label label : itemInfoLabels) {
            label.destroy();
        }
        itemInfoLabels.clear();

        // Обновляем позицию для новых меток
        labelCoord = new Coord(10, 50); // Смещаем позицию, так как добавили кнопку

        // Преобразуем карту в список и сортируем по качеству (парсим качество из ключа)
        List<Map.Entry<String, Integer>> sortedItems = new ArrayList<>(itemSummary.entrySet());
        sortedItems.sort((entry1, entry2) -> {
            // Извлекаем качество из строки ключа (например, "ItemName - Quality: 50.0")
            double quality1 = extractQuality(entry1.getKey());
            double quality2 = extractQuality(entry2.getKey());
            return Double.compare(quality2, quality1); // Сортируем по убыванию качества
        });

        // Добавляем новые метки с информацией о перемещённых предметах
        for (Map.Entry<String, Integer> entry : sortedItems) {
            String info = entry.getKey() + " = " + entry.getValue() + " шт";
            Label newItemLabel = new Label(info);
            this.add(newItemLabel, labelCoord);
            labelCoord = labelCoord.add(0, 15); // Смещаем координаты для следующей метки
            itemInfoLabels.add(newItemLabel);
        }

        pack(); // Автоподгонка размеров окна
    }

    // Вспомогательный метод для извлечения качества из строки ключа
    private double extractQuality(String key) {
        try {
            String qualityPart = key.substring(key.indexOf("Quality: ") + 9);
            return Double.parseDouble(qualityPart.split(" ")[0]); // Парсим качество
        } catch (Exception e) {
            return 0.0; // В случае ошибки возвращаем 0
        }
    }

    // Метод для сброса информации о перемещённых предметах
    private void resetInfo() {
        for (Label label : itemInfoLabels) {
            label.destroy(); // Удаляем все существующие метки
        }
        itemInfoLabels.clear(); // Очищаем список меток
        labelCoord = new Coord(10, 50); // Сбрасываем координаты для меток

        updateStatus("Status: Data reset.");
        pack(); // Обновляем размеры окна
    }

    // Обработчик закрытия окна
    @Override
    public void wdgmsg(String msg, Object... args) {
        if (msg.equals("close")) {
            hide();
        }
        super.wdgmsg(msg, args);
    }
}
