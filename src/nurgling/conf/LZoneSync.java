package nurgling.conf;

import haven.Gob;
import nurgling.NConfig;
import nurgling.NMapView;
import nurgling.overlays.map.NOverlay;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
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
            //NUtils.getGameUI().areas.show();
            ((NMapView)NUtils.getGameUI().map).destroyDummys();
            ((NMapView)NUtils.getGameUI().map).initDummys();
        }
    }
    public void scheduleSync() {
            scheduler.scheduleAtFixedRate(() -> {
                new Thread(() -> {
                    if (NUtils.getGameUI()!=null) {
                        long startTime = System.currentTimeMillis();
                        start();// Запускаем ваш поток
                        long endTime = System.currentTimeMillis();  // Конец замера времени
                        long executionTime = endTime - startTime;  // Разница во времени
                        //System.out.println("Sync time: " + executionTime + " ms");
                    }
                }).start();
            }, 240,240, TimeUnit.SECONDS);  // Задержка в 1 минут
    }

    public static NArea createZoneFromJSON(JSONObject jsonZone) {
        try {
            String uuid = jsonZone.getString("uuid");
            NArea existingZone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);
            if (existingZone != null) {
                int localId = existingZone.id;  // Сохраняем локальный ID
                existingZone.updateFromJSON(jsonZone);  // Обновляем данные из JSON
                existingZone.id = localId;  // Восстанавливаем локальный ID
                return existingZone;
            } else {
                int newId = generateLocalZoneId();  // Генерация нового ID
                NArea newZone = new NArea(jsonZone, newId);
                NUtils.getGameUI().map.glob.map.areas.put(newId, newZone);  // Добавляем новую зону локально
                return newZone;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для синхронизации зон с сервером
    public static void syncZones(JSONArray serverZones) {
        if (NUtils.getGameUI()!=null) {
            Map<Integer, NArea> localZones = NUtils.getGameUI().map.glob.map.areas;
            Set<String> processedUUIDs = new HashSet<>();
            for (int i = 0; i < serverZones.length(); i++) {
                JSONObject jsonZone = serverZones.getJSONObject(i);
                String uuid = jsonZone.getString("uuid");

                processedUUIDs.add(uuid);

                boolean isDeleted = jsonZone.optBoolean("deleted", false);

                if (isDeleted) {
                    NArea localZone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);
                    if (localZone != null) {
                        System.out.println("Удаление зоны с UUID: " + uuid);
                        NUtils.getGameUI().map.nols.remove(localZone.id);

                        Gob dummy = ((NMapView) NUtils.getGameUI().map).dummys.get(((NMapView) NUtils.getGameUI().map).glob.map.areas.get(localZone.id).gid);

                        // Проверка на null для dummy перед удалением из oc
                        if (dummy != null) {
                            NUtils.getGameUI().map.glob.oc.remove(dummy);
                            ((NMapView) NUtils.getGameUI().map).dummys.remove(dummy.id);
                        } else {
                            System.out.println("Не удалось найти объект dummy для зоны с UUID: " + uuid);
                        }

                        NOverlay nol = NUtils.getGameUI().map.nols.get(localZone.id);
                        if (nol != null)
                            nol.remove();

                        NUtils.getGameUI().map.glob.map.areas.remove(localZone.id);
                    }
                } else {
                    boolean foundLocally = false;
                    for (NArea localZone : localZones.values()) {
                        if (localZone.uuid != null && localZone.uuid.equals(uuid)) {
                            LocalDateTime serverLastUpdated = LocalDateTime.parse(jsonZone.getString("last_updated"));
                            if (localZone.lastUpdated.isBefore(serverLastUpdated)) {
                                //System.out.println("ПОЛУЧЕНИЕ:Время сервера: " + serverLastUpdated + " Время локальное: " + localZone.lastUpdated);
                                System.out.println("Серверная зона новее, получаем локально: " + localZone.name);
                                NOverlay nol = NUtils.getGameUI().map.nols.get(localZone.id);
                                nol.remove();
                                NUtils.getGameUI().map.nols.remove(localZone.id);
                                localZones.put(localZone.id, new NArea(jsonZone, localZone.id));
                                NUtils.getGameUI().map.nols.remove(localZone.id);
                            }
                            foundLocally = true;
                            break;
                        }
                    }
                    if (!foundLocally) {
                        NArea newZone = createZoneFromJSON(jsonZone);
                        System.out.println("Новая зона, получаю с сервера: " + newZone.name);
                        if (newZone != null) {
                            NUtils.getGameUI().map.addCustomOverlay(newZone.id);  // Добавляем зону на карту
                        }
                    }
                }
            }
            for (NArea localZone : localZones.values()) {
                if (!processedUUIDs.contains(localZone.uuid)) {
                    System.out.println("Новая зона," + LocalDateTime.now(ZoneOffset.UTC) + " отправляется на сервер: " + localZone.name + localZone.lastUpdated);
                    if (localZone.uuid == null || localZone.uuid.isEmpty()) {
                        localZone.uuid = java.util.UUID.randomUUID().toString();
                    }
                    JSONObject jsonZone = localZone.toJson();
                    LZoneServer.sendZoneToServer(jsonZone);
                } else {
                    JSONObject serverZone = getServerZoneByUUID(serverZones, localZone.uuid);
                    if (serverZone != null) {
                        LocalDateTime serverLastUpdated = LocalDateTime.parse(serverZone.getString("last_updated"));
                        if (localZone.lastUpdated.isAfter(serverLastUpdated)) {
                            System.out.println("Локальная зона новее, отправляется на сервер: " + localZone.name);

                            if (localZone.uuid == null || localZone.uuid.isEmpty()) {
                                localZone.uuid = java.util.UUID.randomUUID().toString();
                            }
                            JSONObject jsonZone = localZone.toJson();
                            LZoneServer.sendZoneToServer(jsonZone);
                        }
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
//    // Новый метод для удаления зоны с клиента и сервера
//    public static void deleteZone(String uuid) {
//        NArea zone = NUtils.getGameUI().map.glob.map.findAreaByUUID(uuid);
//
//        if (zone != null) {
//            NUtils.getGameUI().map.glob.map.areas.remove(zone.id);
//
//            LZoneServer.deleteZoneFromServer(uuid);
//        } else {
//            System.out.println("Зона с UUID " + uuid + " не найдена.");
//        }
//    }


    // Генерация нового локального ID для зоны
    private static int generateLocalZoneId() {
        return NUtils.getGameUI().map.glob.map.generateNextZoneId();
    }
}
