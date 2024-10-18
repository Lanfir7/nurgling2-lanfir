package nurgling.actions.bots;

import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.areas.NArea;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TurnipsLFarmer implements Action {
    private Gob nearestChest;
    private Gob nearestBarrel;
    private Map<String, Integer> fieldQualities = new HashMap<>();
    private String bestField = "";
    private int highestQuality = 0;

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // 1. Найти первые три зоны с подспециализацией Turnip (Represents crop fields)
        ArrayList<NArea> turnipFields = NArea.findAllSpecs("crop", "Turnip");
        if (turnipFields.size() > 3) {
            turnipFields.subList(0, 3); // Ограничиваем тремя первыми зонами
        }

        if (turnipFields.isEmpty()) {
            NUtils.getGameUI().msg("No Turnip fields found.");
            return Results.FAIL();
        }

        // 2. Найти ближайший сундук и бочку
        nearestChest = Finder.findNearestObject(new NAlias("chest"), 1000);
        nearestBarrel = Finder.findNearestObject(new NAlias("barrel"), 1000);

        if (nearestChest == null || nearestBarrel == null) {
            NUtils.getGameUI().msg("No nearby chest or barrel found.");
            return Results.FAIL();
        }
//
//        // 3. Обойти каждое поле и собрать один урожай для определения качества
//        for (NArea field : turnipFields) {
//            Gob crop = Finder.findNearestObject(field.getRCArea(), new NAlias("crop"), 1000);
//
//            if (crop != null) {
//                // Собираем один урожай
//                new HarvestCrop(field, new NAlias("Turnip"), crop).run(gui);
//
//                // Получаем качество семян в инвентаре
//                int seedQuality = NUtils.getSeedQuality("Turnip");
//
//                // Запоминаем качество для каждого поля
//                fieldQualities.put(field.name, seedQuality);
//
//                // Определяем самое высокое качество семян
//                if (seedQuality > highestQuality) {
//                    highestQuality = seedQuality;
//                    bestField = field.name;
//                }
//
//                NUtils.getGameUI().msg("Field " + field.name + " quality: " + seedQuality);
//            }
//        }
//
//        NUtils.getGameUI().msg("Best field: " + bestField + " with quality: " + highestQuality);
//
//        // 4. Переместить лучшие семена в сундук
//        new StoreItemsInContainer(nearestChest, highestQuality).run(gui);
//
//        // 5. Переместить оставшиеся семена в бочку
//        new StoreItemsInContainer(nearestBarrel, -1).run(gui);
//
//        // 6. Собрать все остальные поля и пересадить на те же места
//        for (NArea field : turnipFields) {
//            if (!field.name.equals(bestField)) {
//                new HarvestAndReplant(field, nearestBarrel, gui).run(gui);
//            }
//        }
//
//        // 7. Собрать поле с высоким качеством и пересадить его семена на поля с низким качеством
//        NUtils.getGameUI().msg("Now collecting the best quality field: " + bestField);
//        NArea highQualityField = NArea.findSpec(bestField);
//        new HarvestHighQualityField(highQualityField, nearestBarrel, nearestChest, highestQuality).run(gui);

        NUtils.getGameUI().msg("All tasks completed successfully.");
        return Results.SUCCESS();
    }
}