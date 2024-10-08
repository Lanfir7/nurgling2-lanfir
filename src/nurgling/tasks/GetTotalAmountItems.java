package nurgling.tasks;

import haven.GItem;
import haven.WItem;
import haven.Widget;
import nurgling.NGItem;
import nurgling.NInventory;
import nurgling.tools.NAlias;
import nurgling.tools.NParser;

public class GetTotalAmountItems implements NTask {
    private final NAlias name;
    private final NInventory inventory;
    private final boolean eq;
    private final GItem target;
    private int count = 0;

    public GetTotalAmountItems(NInventory inventory, NAlias name) {
        this.name = name;
        this.inventory = inventory;
        this.eq = false;
        this.target = null;
    }

    public GetTotalAmountItems(NInventory inventory, GItem target) {
        this.target = target;
        this.inventory = inventory;
        this.eq = true;
        this.name = new NAlias(((NGItem) target).name());
    }

    @Override
    public boolean check() {
        count = 0;

        // Если задана конкретная цель, проверяем ее
        if (target != null && ((NGItem) target).name() != null) {
            name.keys.add(((NGItem) target).name());
        } else if (target != null) {
            return false;
        }

        // Проходим по всем предметам в инвентаре
        for (Widget widget = inventory.child; widget != null; widget = widget.next) {
            if (widget instanceof WItem) {
                WItem item = (WItem) widget;
                NGItem ngItem = (NGItem) item.item;
                String itemName = ngItem.name();

                if (itemName == null) {
                    continue; // Пропускаем предметы без имени
                }

                // Проверяем, соответствует ли предмет указанному названию
                if (name == null || (eq && !name.keys.isEmpty() ? itemName.equals(name.getDefault()) : NParser.checkName(itemName, name))) {
                    // Увеличиваем счетчик на 1 за каждый найденный предмет
                    count++;
                }
            }
        }

        return true;
    }

    public int getResult() {
        return count;
    }
}
