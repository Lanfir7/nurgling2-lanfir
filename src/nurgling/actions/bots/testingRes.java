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
        resourcePaths.add("gfx/invobjs/beetroot");
        resourcePaths.add("gfx/invobjs/pickledbeetroot");
        resourcePaths.add("gfx/invobjs/boiledegg");
        resourcePaths.add("gfx/invobjs/pickledherring");



//        rawMeat.add(new JSONObject("{\"layer\": [\"gfx/invobjs/meat-raw\", \"gfx/invobjs/meat-wildbeef\"], \"name\": \"Raw Wild Beef\"}"));
//        rawMeat.add(new JSONObject("{\"layer\": [\"gfx/invobjs/meat-raw\", \"gfx/invobjs/meat-wildmutton\"], \"name\": \"Raw Wild Mutton\"}"));
//        rawMeat.add(new JSONObject("{\"layer\": [\"gfx/invobjs/meat-raw\", \"gfx/invobjs/meat-wildpork\"], \"name\": \"Raw Wild Pork\"}"));






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
