package nurgling.actions;

import haven.Coord;
import haven.WItem;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Context;
import nurgling.tools.NAlias;
import nurgling.widgets.bots.StockpileTransferInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FreeContainers implements Action {

    Context context = new Context();
    ArrayList<Container> containers;
    NAlias excludedItemAlias; // Новый NAlias для исключаемых предметов

    // Карта для хранения информации о перемещённых предметах
    Map<String, Integer> itemSummary = new HashMap<>();

    private StockpileTransferInfo transferInfoWindow; // Окно для отображения информации

    public FreeContainers(ArrayList<Container> containers) {
        this.containers = containers;
    }

    public FreeContainers(ArrayList<Container> containers, NAlias excludedItemAlias) {
        this.containers = containers;
        this.excludedItemAlias = excludedItemAlias;
    }

    HashSet<String> targets = new HashSet<>();

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        Context context = new Context();

        // Открываем окно для отображения информации
        transferInfoWindow = new StockpileTransferInfo();
        gui.add(transferInfoWindow, new Coord(300, 200));

        for (Container container : containers) {
            Container.Space space;
            if ((space = container.getattr(Container.Space.class)).isReady()) {
                if (space.getRes().get(Container.Space.FREESPACE) == space.getRes().get(Container.Space.MAXSPACE))
                    continue;
            }
            new PathFinder(container.gob).run(gui);

            if (container.cap.equals("Stockpile")) {
                while (!container.gob.getloc().isEmpty()) {
                    new OpenTargetContainer("Stockpile", container.gob).run(gui);
                    new TakeItemsFromPile(container.gob, gui.getStockpile()).run(gui);



                    if (NUtils.getGameUI().getInventory().getFreeSpace() == 0) {
                        collectItemsSummary(gui);
                        new FreeInventory(context).run(gui);
                    }

                    if (!container.gob.getloc().isEmpty()) {
                        new PathFinder(container.gob).run(gui);
                    }

                }
            } else {
                new OpenTargetContainer(container).run(gui);
                for (WItem item : gui.getInventory(container.cap).getItems()) {
                    String name = ((NGItem) item.item).name();

                    // Если предмет соответствует исключенному имени, пропускаем его
                    if (excludedItemAlias != null && excludedItemAlias.check(name)) {
                        continue;
                    }

                    NArea area = NArea.findOut(name, ((NGItem) item.item).quality != null ? ((NGItem) item.item).quality : 1);
                    if (area != null) {
                        targets.add(name);
                    }
                }

                while (!new TakeItemsFromContainer(container, targets).run(gui).isSuccess) {
                    for (String name : targets) {
                        new TransferItems(context, name).run(gui);
                    }
                    collectItemsSummary(gui);
                    new PathFinder(container.gob).run(gui);
                    new OpenTargetContainer(container).run(gui);
                }
            }

            transferInfoWindow.updateItemInfo(itemSummary);
            new CloseTargetContainer(container).run(gui);
        }

        // Выводим информацию о перемещённых предметах после завершения работы
        transferInfoWindow.updateStatus("Transfer complete.");
        new FreeInventory(context).run(gui);

        return Results.SUCCESS();
    }

    // Метод для сбора информации о предметах в инвентаре
    private void collectItemsSummary(NGameUI gui) throws InterruptedException {
        for (WItem item : gui.getInventory().getItems()) {
            NGItem ngItem = (NGItem) item.item;
            String itemName = ngItem.name();
            double quality = ngItem.quality != null ? ngItem.quality : 1;

            // Формируем ключ с именем предмета и его качеством
            String itemKey = itemName + " - Quality: " + quality;

            // Увеличиваем количество данного предмета в карте
            itemSummary.put(itemKey, itemSummary.getOrDefault(itemKey, 0) + 1);
        }
    }
}
