package nurgling.conf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
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
    // Метод для синхронизации зон с сервером
    public static void syncZones(JSONArray serverZones) {
        Map<Integer, NArea> localZones = NUtils.getGameUI().map.glob.map.areas;

        for (int i = 0; i < serverZones.length(); i++) {
            JSONObject jsonZone = serverZones.getJSONObject(i);
            String uuid = jsonZone.getString("uuid");

            // Проверяем, помечена ли зона как удаленная на сервере
            boolean isDeleted = jsonZone.optBoolean("deleted", false);

            if (isDeleted) {
                // Если зона помечена как удаленная на сервере, удаляем ее локально
                NArea localZone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);
                if (localZone != null) {
                    System.out.println("Удаление зоны с UUID: " + uuid);
                    NUtils.getGameUI().map.glob.map.areas.remove(localZone.id);
                    NUtils.getGameUI().areas.al.updateList();  // Обновляем список зон в UI
                }
            } else {
                // Если зона не удалена, продолжаем обычную синхронизацию
                boolean foundLocally = false;
                for (NArea localZone : localZones.values()) {
                    if (localZone.uuid != null && localZone.uuid.equals(uuid)) {
                        // Получаем дату обновления зоны с сервера
                        LocalDateTime serverLastUpdated = LocalDateTime.parse(jsonZone.getString("last_updated"));

                        // Если локальная зона не имеет даты обновления, инициализируем ее
                        LocalDateTime localLastUpdated = localZone.lastUpdated != null ? localZone.lastUpdated : LocalDateTime.MIN;

                        // Сравнение дат
                        if (localLastUpdated.isBefore(serverLastUpdated)) {
                            // Если серверная зона обновлялась позже, обновляем локальную зону
                            localZone.updateFromJSON(jsonZone);
                        }
                        foundLocally = true;
                        break;
                    }
                }

                // Если зона не найдена локально, создаем новую
                if (!foundLocally) {
                    NArea newZone = createZoneFromJSON(jsonZone);
                    if (newZone != null) {
                        NUtils.getGameUI().areas.addArea(newZone.id, newZone.name, newZone);
                        NUtils.getGameUI().map.addCustomOverlay(newZone.id);  // Добавляем зону на карту
                    }
                }
            }
        }
    }

    // Новый метод для удаления зоны с клиента и сервера
    public static void deleteZone(String uuid) {
        NArea zone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);

        if (zone != null) {
            // Удаляем зону локально
            NUtils.getGameUI().map.glob.map.areas.remove(zone.id);

            // Отправляем запрос на сервер для удаления зоны
            LZoneServer.deleteZoneFromServer(uuid);

            // Обновляем отображение зон
            NUtils.getGameUI().areas.al.updateList();
        } else {
            System.out.println("Зона с UUID " + uuid + " не найдена.");
        }
    }


    // Генерация нового локального ID для зоны
    private static int generateLocalZoneId() {
        return NUtils.getGameUI().map.glob.map.generateNextZoneId();
    }
}
