package nurgling.actions.bots;

import haven.*;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.LiftObject;
import nurgling.actions.PathFinder;
import nurgling.actions.Results;
import nurgling.tasks.ChangeModelAtrib;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class testingRes implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<String> resourcePaths = new ArrayList<>();
        resourcePaths.add("gfx/invobjs/adderhide-blood");
        resourcePaths.add("gfx/invobjs/aurochshide-blood");
        resourcePaths.add("gfx/invobjs/badgerhide-blood");
        resourcePaths.add("gfx/invobjs/bathide-blood");
        resourcePaths.add("gfx/invobjs/bearhide-blood");
        resourcePaths.add("gfx/invobjs/beaverhide-blood");
        resourcePaths.add("gfx/invobjs/boarhide-blood");
        resourcePaths.add("gfx/invobjs/borewormhide-blood");
        resourcePaths.add("gfx/invobjs/cattlehide-blood");
        resourcePaths.add("gfx/invobjs/caveanglerscales-blood");
        resourcePaths.add("gfx/invobjs/caverathide-blood");
        resourcePaths.add("gfx/invobjs/stoathide-winter-blood");
        resourcePaths.add("gfx/invobjs/goathide-blood");
        resourcePaths.add("gfx/invobjs/greysealhide-blood");
        resourcePaths.add("gfx/invobjs/hedgehoghide-blood");
        resourcePaths.add("gfx/invobjs/horsehide-blood");
        resourcePaths.add("gfx/invobjs/lynxhide-blood");
        resourcePaths.add("gfx/invobjs/mammothhide-blood");
        resourcePaths.add("gfx/invobjs/molehide-blood");
        resourcePaths.add("gfx/invobjs/moosehide-blood");
        resourcePaths.add("gfx/invobjs/mouflonhide-blood");
        resourcePaths.add("gfx/invobjs/otterhide-blood");
        resourcePaths.add("gfx/invobjs/pighide-blood");
        resourcePaths.add("gfx/invobjs/rabbithide-blood");
        resourcePaths.add("gfx/invobjs/reddeerhide-blood");
        resourcePaths.add("gfx/invobjs/reindeerhide-blood");
        resourcePaths.add("gfx/invobjs/roedeerhide-blood");
        resourcePaths.add("gfx/invobjs/sheephide-blood");
        resourcePaths.add("gfx/invobjs/squirrelhide-blood");
        resourcePaths.add("gfx/invobjs/squirreltail-blood");
        resourcePaths.add("gfx/invobjs/stoathide-blood");
        resourcePaths.add("gfx/invobjs/trollhide-blood");
        resourcePaths.add("gfx/invobjs/walrushide-blood");
        resourcePaths.add("gfx/invobjs/wildgoathide-blood");
        resourcePaths.add("gfx/invobjs/wildhorsehide-blood");
        resourcePaths.add("gfx/invobjs/wolfhide-blood");
        resourcePaths.add("gfx/invobjs/wolverinehide-blood");







        checkResources(resourcePaths);
        return Results.SUCCESS();
    }

    // Метод для проверки списка ресурсов
    private void checkResources(ArrayList<String> resourcePaths) {
        for (String path : resourcePaths) {
            if (checkResource(path)) {
                System.out.println("         Resource found: " + path);
            } else {
                System.out.println("missing: " + path);
            }
        }
    }

    // Метод для проверки конкретного ресурса по URL
    private boolean checkResource(String path) {
        String baseUrl = "http://game.havenandhearth.com/res/";  // Базовый URL ресурса
        String fullUrl = baseUrl + path + ".res";  // Полный URL ресурса с расширением

        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");  // Используем метод HEAD для минимального ответа
            int responseCode = connection.getResponseCode();

            // Если ответ 200, ресурс существует
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // Возвращаем false, если произошла ошибка
        }
    }
}
