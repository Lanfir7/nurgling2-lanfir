package nurgling.conf;

import nurgling.NConfig;
import nurgling.NMapView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nurgling.areas.NArea;
import nurgling.NUtils;

public class LZoneSync {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public void start()  {
        JSONArray serverZones = LZoneServer.getAllZones();

        if (serverZones != null) {
            syncZones(serverZones);
            NConfig.needAreasUpdate();
            ((NMapView)NUtils.getGameUI().map).destroyDummys();
            ((NMapView)NUtils.getGameUI().map).initDummys();
        }
    }
    public void scheduleSync() {
            scheduler.scheduleAtFixedRate(() -> {
                new Thread(() -> {
                    start();// Запускаем ваш поток
                }).start();
            }, 30,30, TimeUnit.SECONDS);  // Задержка в 1 минут
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
        Set<String> processedUUIDs = new HashSet<>();
        for (int i = 0; i < serverZones.length(); i++) {
            JSONObject jsonZone = serverZones.getJSONObject(i);
            String uuid = jsonZone.getString("uuid");

            processedUUIDs.add(uuid);

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

//                        LocalDateTime serverLastUpdated = LocalDateTime.parse(jsonZone.getString("last_updated"));
//                        serverLastUpdated = serverLastUpdated.atZone(ZoneOffset.UTC).toLocalDateTime();
                        LocalDateTime serverLastUpdated = LocalDateTime.parse(jsonZone.getString("last_updated"));
//                        serverLastUpdated = serverLastUpdated.atZone(ZoneOffset.UTC).toLocalDateTime();

//                        ZonedDateTime localLastUpdatedZoned = localZone.lastUpdated.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
//                        LocalDateTime localLastUpdatedUTC = localLastUpdatedZoned.toLocalDateTime();



                        if (localZone.lastUpdated.isBefore(serverLastUpdated)) {
                            System.out.println("ПОЛУЧЕНИЕ:Время сервера: " + serverLastUpdated + " Время локальное: " + localZone.lastUpdated);
                            System.out.println("Серверная зона новее, получаем на локально: " + localZone.name);
                            localZone.updateFromJSON(jsonZone);
                            NUtils.getGameUI().areas.addArea(localZone.id, localZone.name, localZone);
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
        // Теперь проверяем локальные зоны, которых нет на сервере, или если локальные зоны новее серверных
        for (NArea localZone : localZones.values()) {
            if (!processedUUIDs.contains(localZone.uuid)) {
                // Если сервер не обработал эту зону, значит её нет на сервере - отправляем на сервер
                System.out.println("Новая зона,"+LocalDateTime.now(ZoneOffset.UTC)+" отправляется на сервер: " + localZone.name + localZone.lastUpdated);
                if (localZone.uuid == null || localZone.uuid.isEmpty()) {
                    // Если у зоны нет UUID, генерируем новый
                    localZone.uuid = java.util.UUID.randomUUID().toString();
                }
                JSONObject jsonZone = localZone.toJson();
                LZoneServer.sendZoneToServer(jsonZone);
            } else {
                // Если зона уже существует на сервере, проверяем на обновления
                // Получаем серверные данные по этой зоне
                JSONObject serverZone = getServerZoneByUUID(serverZones, localZone.uuid);
                if (serverZone != null) {
                    LocalDateTime serverLastUpdated = LocalDateTime.parse(serverZone.getString("last_updated"));
//                    serverLastUpdated = serverLastUpdated.atZone(ZoneOffset.UTC).toLocalDateTime();
//                    ZonedDateTime localLastUpdatedZoned = localZone.lastUpdated.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
//                    LocalDateTime localLastUpdatedUTC = localLastUpdatedZoned.toLocalDateTime();


                    if (localZone.lastUpdated.isAfter(serverLastUpdated)) {
                        // Локальная зона новее, отправляем её на сервер
                        System.out.println("ОТПРАВКА:Серверное: " + serverLastUpdated + " Локальное: " + localZone.lastUpdated);
                        System.out.println("Локальная зона новее, отправляется на сервер: " + localZone.name);

                        if (localZone.uuid == null || localZone.uuid.isEmpty()) {
                            // Если у зоны нет UUID, генерируем новый
                            localZone.uuid = java.util.UUID.randomUUID().toString();
                        }
                        JSONObject jsonZone = localZone.toJson();
                        LZoneServer.sendZoneToServer(jsonZone);
                    }
                }
            }
        }
    }
    private static JSONObject getServerZoneByUUID(JSONArray serverZones, String uuid) {
        for (int i = 0; i < serverZones.length(); i++) {
            JSONObject jsonZone = serverZones.getJSONObject(i);
            if (jsonZone.getString("uuid").equals(uuid)) {
                return jsonZone;
            }
        }
        return null;
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
