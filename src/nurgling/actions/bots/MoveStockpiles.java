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

public class MoveStockpiles implements Action {

    private StockpileTransferInfo transferInfoWindow; // Окно для отображения информации

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
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

                ArrayList<String> names = new ArrayList<>();
                if (NUtils.getGameUI().getInventory().getFreeSpace() == 0) {
                    for (WItem item : gui.getInventory().getItems()) {
                        String itemName = ((NGItem) item.item).name();
                        double itemQuality = ((NGItem) item.item).quality != null ? ((NGItem) item.item).quality : 1.0;
                        transferInfoWindow.addItem(itemName, itemQuality); // Добавляем предмет через окно
                        names.add(itemName);

                    }
                    itemAlias = new NAlias(names);
                    new TransferToPiles(outArea, itemAlias).run(gui);
                }
            }
        }
        for (WItem item : gui.getInventory().getItems()) {
            String itemName = ((NGItem) item.item).name();
            double itemQuality = ((NGItem) item.item).quality != null ? ((NGItem) item.item).quality : 1.0;
            transferInfoWindow.addItem(itemName, itemQuality); // Добавляем предмет через окно
        }
        // Завершаем работу и обновляем окно
        new TransferToPiles(outArea, itemAlias).run(gui);
        transferInfoWindow.updateStatus("Transfer complete.");
        return Results.SUCCESS();
    }
}
