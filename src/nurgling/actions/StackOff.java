package nurgling.actions;

import haven.MenuGrid;
import nurgling.NGameUI;
import nurgling.NInventory;
import nurgling.NUtils;


public class StackOff {
    boolean status;
    public StackOff(boolean status) {
        this.status = status;
    }

    public boolean run(){
        NInventory inv = (NInventory) NUtils.getGameUI().maininv;
        boolean old =inv.bundle.a;
        if (inv.bundle.a && !status) {
            MenuGrid.PagButton but = inv.pagBundle;
            if (but != null) {
                but.use(new MenuGrid.Interaction(1, 0));
            }
        } else if (!inv.bundle.a && status){
            MenuGrid.PagButton but = inv.pagBundle;
            if (but != null) {
                but.use(new MenuGrid.Interaction(1, 0));
            }
        }
        return old;
    }

}
