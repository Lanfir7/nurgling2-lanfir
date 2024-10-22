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
        resourcePaths.add("gfx/invobjs/ambergris");
        resourcePaths.add("gfx/invobjs/blackpepper");
        resourcePaths.add("gfx/invobjs/herbs/chives");
        resourcePaths.add("gfx/invobjs/herbs/dill");
        resourcePaths.add("gfx/invobjs/juniperberries");
        resourcePaths.add("gfx/invobjs/kvann");
        resourcePaths.add("gfx/invobjs/leaf-laurel");
        resourcePaths.add("gfx/invobjs/propolis");
        resourcePaths.add("gfx/invobjs/sage");
        resourcePaths.add("gfx/invobjs/herbs/thyme");






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
class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
