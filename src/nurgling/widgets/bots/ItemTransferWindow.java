package nurgling.widgets.bots;

import haven.*;
import nurgling.NUtils;

import java.util.HashMap;
import java.util.Map;

public class ItemTransferWindow extends Window {

    private Map<String, Integer> itemTransferSummary = new HashMap<>(); // Карта для хранения информации о перемещенных предметах
    private Label itemTransferLabel; // Текстовое поле для вывода информации

    public ItemTransferWindow() {
        super(new Coord(300, 150), "Item Transfer Summary");
        createTransferSummaryWindow();
    }

    // Метод для создания окна
    private void createTransferSummaryWindow() {
        itemTransferLabel = new Label("Transferred Items:");
        this.add(itemTransferLabel, new Coord(10, 10));
        pack(); // Автоподгонка размеров окна
    }

    // Метод для обновления информации о перемещённых предметах
    public void updateItemTransferSummary(String itemName, int quantity) {
        itemTransferSummary.put(itemName, itemTransferSummary.getOrDefault(itemName, 0) + quantity);
        updateLabel();
    }

    // Метод для обновления текстового поля с новой информацией
    private void updateLabel() {
        StringBuilder message = new StringBuilder("Transferred Items:\n");
        for (Map.Entry<String, Integer> entry : itemTransferSummary.entrySet()) {
            message.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append(" шт\n");
        }
        itemTransferLabel.settext(message.toString());
    }
}
