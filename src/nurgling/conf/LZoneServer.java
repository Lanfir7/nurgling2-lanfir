package nurgling.conf;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.json.JSONArray;

public class LZoneServer {

    private static final String SERVER_URL = "https://worker-production-e53b.up.railway.app";  // Адрес сервера

    // Отправка зоны на сервер
    public static void sendZoneToServer(JSONObject zoneData) {
        try {
            String serverUrl = SERVER_URL + "/add_zone";
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            // Отправка JSON на сервер
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = zoneData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Проверка ответа от сервера
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

    // Получение всех зон с сервера
    public static JSONArray getAllZones() {
        try {
            String serverUrl = SERVER_URL + "/get_zones";
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Парсим ответ в JSON массив
                return new JSONArray(response.toString());
            } else {
                System.out.println("Failed to get zones. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Получение зон, обновленных после определенной даты
    public static JSONArray getUpdatedZones(String lastUpdated) {
        try {
            String serverUrl = SERVER_URL + "/get_zones?updated_after=" + lastUpdated;
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONArray(response.toString());
            } else {
                System.out.println("Failed to get updated zones. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Получение конкретной зоны по UUID
    public static JSONObject getZoneByUUID(String uuid) {
        try {
            String serverUrl = SERVER_URL + "/get_zone/" + uuid;
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());
            } else {
                System.out.println("Failed to get zone. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
