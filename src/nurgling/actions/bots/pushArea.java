package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import nurgling.NConfig;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.CollectFromGob;
import nurgling.actions.Results;
import nurgling.actions.TransferToPiles;
import nurgling.areas.NArea;
import nurgling.conf.JConf;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

public class pushArea implements Action {

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Предположим, что zone - это объект NArea, представляющий зону с ID 1
        NArea zone = NUtils.getGameUI().map.glob.map.areas.get(1);  // Получаем зону с ID 1
        sendZoneToServer(zone);
        return Results.SUCCESS();
    }
    public static void sendZoneToServer(NArea zone) {
        try {
            // Конвертируем объект зоны в JSON
            JSONObject jsonZone = zone.toJson();

            // Выводим JSON в консоль для отладки
            System.out.println("Отправляемый JSON:");
            System.out.println(jsonZone.toString(4));  // Печатает форматированный JSON

            // URL сервера
            String serverUrl = "https://worker-production-e53b.up.railway.app/add_zone";

            // Открываем соединение
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            // Отправляем JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonZone.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Проверяем статус ответа
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Zone sent successfully!");
            } else {
                System.out.println("Failed to send zone. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
