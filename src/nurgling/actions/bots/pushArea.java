package nurgling.actions.bots;

import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.Results;
import nurgling.conf.LZoneServer;
import nurgling.conf.LZoneSync;
import nurgling.areas.NArea;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Map;

public class pushArea implements Action {

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
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
            LZoneSync.syncZones(serverZones);  // Передаем серверные зоны для синхронизации
        }

        return Results.SUCCESS();
    }
}

