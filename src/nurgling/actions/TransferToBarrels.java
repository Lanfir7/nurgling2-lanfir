package nurgling.actions;

import haven.*;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.tasks.NotThisInHand;
import nurgling.tasks.WaitItems;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransferToBarrels implements Action {

    NAlias items;
    Pair<Coord2d, Coord2d> zone; // Зона, в которой находятся бочки
    //int th = 9000; // Лимит наполнения бочки (вместимость)
    int th = -1;
    public TransferToBarrels(Pair<Coord2d, Coord2d> zone, NAlias items) {
        this.zone = zone;
        this.items = items;
    }

    public TransferToBarrels(Pair<Coord2d, Coord2d> zone, NAlias items, int th) {
        this.zone = zone;
        this.items = items;
        this.th = th;
    }

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<Gob> barrels = Finder.findGobs(zone, new NAlias("barrel")); // Ищем все бочки в зоне

        if (barrels.isEmpty()) {
            NUtils.getGameUI().msg("No barrels found in the zone.");
            return Results.ERROR("No barrels found.");
        }

        ArrayList<WItem> witems = gui.getInventory().getItems(items);
        if (witems.isEmpty()) {
            return Results.ERROR("No items found.");
        }
        for (Gob barrel : barrels) {
            if (witems.isEmpty()) break;
            new TransferToBarrel(barrel, items).run(gui);
            witems = gui.getInventory().getItems(items);
        }

        return witems.isEmpty() ? Results.SUCCESS() : Results.FAIL();
    }
}
