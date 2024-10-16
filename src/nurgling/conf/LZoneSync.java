package nurgling.conf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import nurgling.areas.NArea;
import nurgling.NUtils;

public class LZoneSync {
    public void start()  {
        // 1. Отправляем локальные зоны на сервер
        // Сначала получаем все локальные зоны
        Map<Integer, NArea> localZones = NUtils.getGameUI().map.glob.map.areas;

        for (NArea zone : localZones.values()) {
            if (zone.uuid == null || zone.uuid.isEmpty()) {
                // Если у зоны нет UUID, генерируем новый
                zone.uuid = java.util.UUID.randomUUID().toString();
            }

            // Отправляем каждую зону на сервер
            JSONObject jsonZone = zone.toJson();
            LZoneServer.sendZoneToServer(jsonZone);
        }

        JSONArray serverZones = LZoneServer.getAllZones();

        if (serverZones != null) {
            syncZones(serverZones);  // Передаем серверные зоны для синхронизации
        }
    }
    // Метод для создания или обновления зоны из JSON-данных
    public static NArea createZoneFromJSON(JSONObject jsonZone) {
        try {
            String uuid = jsonZone.getString("uuid");

            // Проверяем, есть ли такая зона локально по UUID
            NArea existingZone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);
            if (existingZone != null) {
                // Если зона уже существует, обновляем ее данные
                int localId = existingZone.id;  // Сохраняем локальный ID
                existingZone.updateFromJSON(jsonZone);  // Обновляем данные из JSON
                existingZone.id = localId;  // Восстанавливаем локальный ID
                return existingZone;
            } else {
                int newId = generateLocalZoneId();  // Генерация нового ID
                NArea newZone = new NArea(jsonZone, newId);
                NUtils.getGameUI().map.glob.map.addArea(newZone);  // Добавляем новую зону локально
                return newZone;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для синхронизации зон с сервером
    public static void syncZones(JSONArray serverZones) {
        Map<Integer, NArea> localZones = NUtils.getGameUI().map.glob.map.areas;


        for (int i = 0; i < serverZones.length(); i++) {
            JSONObject jsonZone = serverZones.getJSONObject(i);
            String uuid = jsonZone.getString("uuid");

            // Проверка, есть ли зона с этим UUID локально
            boolean foundLocally = false;
            for (NArea localZone : localZones.values()) {
                if (localZone.uuid != null && localZone.uuid.equals(uuid)) {
                    // Если зона найдена, обновляем ее данные
                    localZone.updateFromJSON(jsonZone);
                    foundLocally = true;
                    break;
                }
            }

            // Если зона не найдена локально, создаем новую
            if (!foundLocally) {
                NArea newZone = createZoneFromJSON(jsonZone);
                // Принудительно добавляем зону на карту или в отображение зон
                if (newZone != null) {
                    NUtils.getGameUI().areas.addArea(newZone.id, newZone.name, newZone);
                    NUtils.getGameUI().map.addCustomOverlay(newZone.id);  // Добавляем зону на карту
                }
            }
        }

    }

    // Генерация нового локального ID для зоны
    private static int generateLocalZoneId() {
        return NUtils.getGameUI().map.glob.map.generateNextZoneId();
    }
}
