package nurgling.actions;

import haven.WItem;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Context;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.HashSet;

public class FreeContainers implements Action {
    ArrayList<Container> containers;
    NAlias excludedItemAlias; // Новый NAlias для исключаемых предметов

    public FreeContainers(ArrayList<Container> containers) {
        this.containers = containers;
    }

    // Новый конструктор с исключением предметов
    public FreeContainers(ArrayList<Container> containers, NAlias excludedItemAlias) {
        this.containers = containers;
        this.excludedItemAlias = excludedItemAlias;
    }

    HashSet<String> targets = new HashSet<>();

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        Context context = new Context();
        for (Container container : containers) {
            Container.Space space;
            if ((space = container.getattr(Container.Space.class)).isReady()) {
                if (space.getRes().get(Container.Space.FREESPACE) == space.getRes().get(Container.Space.MAXSPACE))
                    continue;
            }
            new PathFinder(container.gob).run(gui);
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
                new PathFinder(container.gob).run(gui);
                new OpenTargetContainer(container).run(gui);
            }
            new CloseTargetContainer(container).run(gui);
        }

        for (String name : targets) {
            new TransferItems(context, name).run(gui);
        }

        return Results.SUCCESS();
    }
}
