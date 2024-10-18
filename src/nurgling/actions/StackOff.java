package nurgling.actions;

import haven.Coord;
import haven.Inventory;
import haven.MenuGrid;
import haven.WItem;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NInventory;
import nurgling.NUtils;
import nurgling.tasks.WaitItemInEquip;
import nurgling.tasks.WaitItemInHand;
import nurgling.tools.NAlias;
import nurgling.tools.NParser;
import nurgling.widgets.NEquipory;

import java.util.HashSet;

public class StackOff {
    boolean status;
    public StackOff(boolean status) {
        this.status = status;
    }

    public boolean run(NGameUI gui) throws InterruptedException {
        NInventory inv = (NInventory) NUtils.getGameUI().maininv;
        boolean old =inv.bundle.a;
        if (inv.bundle.a && !status) {
            NUtils.getGameUI().msg("Выключаю стаки");
            MenuGrid.PagButton but = inv.pagBundle;
            if (but != null) {
                but.use(new MenuGrid.Interaction(1, 0));
            }
        } else if (!inv.bundle.a && status){
            NUtils.getGameUI().msg("Включаю стаки");
            MenuGrid.PagButton but = inv.pagBundle;
            if (but != null) {
                but.use(new MenuGrid.Interaction(1, 0));
            }
        }
        return old;
    }

}
