package nurgling.widgets.options;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ForageData {
    public static List<NForageHelper.ForagableData> loadForageData() {
        List<NForageHelper.ForagableData> items = new ArrayList<>();
        String path = "C:\\Users\\DENIS\\AppData\\Roaming\\Haven and Hearth\\foragedata.json"; // Укажите путь к JSON-файлу

        try (InputStream is = new FileInputStream(path)) { // Используем FileInputStream для абсолютного пути
            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonText);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                NForageHelper.ForagableData item = new NForageHelper.ForagableData(
                        obj.getString("name"),
                        obj.getString("inv_res"),
                        obj.getInt("min_val"),
                        obj.getInt("max_val"),
                        obj.getString("location"),
                        obj.getInt("spring") == 1,
                        obj.getInt("summer") == 1,
                        obj.getInt("autumn") == 1,
                        obj.getInt("winter") == 1
                );
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
