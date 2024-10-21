package nurgling.actions.bots;

import nurgling.NGameUI;
import nurgling.actions.Action;
import nurgling.actions.Results;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResourceFinder implements Action {

    private static final String BASE_URL = "http://game.havenandhearth.com/res/gfx/invobjs/";

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<String> resourceNames = generateResourceNames();  // Генерация ресурсов
        findResources(resourceNames);
        return Results.SUCCESS();
    }

    // Генерация списка возможных ресурсов с различными префиксами
    private ArrayList<String> generateResourceNames() {
        ArrayList<String> resourceNames = new ArrayList<>();
        String[] prefixes = {"egg-", "herbs-", "seed-", "bird-"};
        String[] names = {"chicken", "goose", "duck", "turkey", "pigeon", "peacock", "flax", "hemp", "carrot", "turnip"};

        for (String prefix : prefixes) {
            for (String name : names) {
                resourceNames.add(prefix + name);  // Формирование ресурсов по префиксу и имени
            }
        }

        return resourceNames;
    }

    // Поиск ресурсов из списка
    public static void findResources(ArrayList<String> resourceNames) {
        for (String resource : resourceNames) {
            String fullUrl = BASE_URL + resource + ".res";
            if (checkResource(fullUrl)) {
                System.out.println("Resource found: " + fullUrl);
            } else {
                System.out.println("Resource missing: " + fullUrl);
            }
        }
    }

    // Проверка ресурса по URL
    private static boolean checkResource(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;  // Возвращает true, если ресурс найден
        } catch (IOException e) {
            return false;  // Если произошла ошибка или ресурс не найден
        }
    }
}
