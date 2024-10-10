package nurgling.actions.bots;

import haven.*;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.bots.StockpileTransferInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoveStockpiles implements Action {

    private StockpileTransferInfo transferInfoWindow; // Окно для отображения информации
    private Map<String, Integer> itemSummary = new HashMap<>(); // Карта для хранения информации о предметах

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        new StackOff().run(gui);
        transferInfoWindow = new StockpileTransferInfo();
        gui.add(transferInfoWindow, new Coord(300, 200)); // Открываем окно

        SelectArea insa;
        NAlias itemAlias = null;
        NUtils.getGameUI().msg("Please select the area with stockpiles to move.");
        (insa = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> inArea = insa.getRCArea();

        SelectArea outsa;
        NUtils.getGameUI().msg("Please select the target area for new stockpiles.");
        (outsa = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> outArea = outsa.getRCArea();

        ArrayList<Gob> stockpiles = Finder.findGobs(inArea, new NAlias("stockpile"));
        if (stockpiles.isEmpty()) {
            transferInfoWindow.updateStatus("No stockpiles found in the selected area.");
            return Results.ERROR("No stockpiles found in the selected area.");
        }

        stockpiles.sort(NUtils.d_comp);

        // Обрабатываем каждый стокпайл
        for (Gob stockpile : stockpiles) {
            while (!stockpile.getloc().isEmpty()) {
                new PathFinder(stockpile).run(gui);
                new OpenTargetContainer("Stockpile", stockpile).run(gui);
                new TakeItemsFromPile(stockpile, gui.getStockpile()).run(gui);
                new CloseTargetWindow(NUtils.getGameUI().getWindow("Stockpile")).run(gui);

                if (NUtils.getGameUI().getInventory().getFreeSpace() == 0) {
                    ArrayList<WItem> inventoryItems = gui.getInventory().getItems();
                    ArrayList<String> names = new ArrayList<>();

                    for (WItem item : inventoryItems) {
                        String itemName = ((NGItem) item.item).name();
                        double itemQuality = ((NGItem) item.item).quality != null ? ((NGItem) item.item).quality : 1.0;

                        // Обновляем информацию о перемещённом предмете
                        String itemKey = itemName + " - Quality: " + itemQuality;
                        itemSummary.put(itemKey, itemSummary.getOrDefault(itemKey, 0) + 1);

                        names.add(itemName);
                    }

                    itemAlias = new NAlias(names);
                    new TransferToPiles(outArea, itemAlias).run(gui);

                    // Обновляем окно с информацией
                    transferInfoWindow.updateItemInfo(itemSummary);
                }
            }
        }

        // Завершаем работу и обновляем окно
        new TransferToPiles(outArea, itemAlias).run(gui);
        transferInfoWindow.updateStatus("Transfer complete.");
        return Results.SUCCESS();
    }
}
