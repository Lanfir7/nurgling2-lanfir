package nurgling.actions;

import haven.WItem;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Context;

import java.util.ArrayList;
import java.util.HashSet;

public class FreeInventory implements Action
{
    public FreeInventory() { }

    HashSet<String> targets = new HashSet<>();

    @Override
    public Results run(NGameUI gui) throws InterruptedException
    {
        Context context = new Context();

        // Получаем все предметы из инвентаря игрока
        for (WItem item : gui.getInventory().getItems()) {
            String itemName = ((NGItem) item.item).name();  // Получаем имя предмета
            double quality = ((NGItem) item.item).quality != null ? ((NGItem) item.item).quality : 1;  // Получаем качество предмета

            // Определяем назначение для каждого предмета (по его имени и качеству)
            NArea targetArea = NArea.findOut(itemName, quality);
            if (targetArea != null) {
                targets.add(itemName);  // Добавляем предмет в список для дальнейшей раскладки
            }
        }

        // Проходим по всем найденным целям и выкладываем их по назначению
        for (String name : targets) {
            // Передаем контекст и предметы для раскладки
            new TransferItems(context, name).run(gui);
        }

        return Results.SUCCESS();
    }
}
