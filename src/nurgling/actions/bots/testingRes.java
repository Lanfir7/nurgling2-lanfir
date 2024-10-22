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
        resourcePaths.add("gfx/invobjs/alabaster");
        resourcePaths.add("gfx/invobjs/apatite");
        resourcePaths.add("gfx/invobjs/arkose");
        resourcePaths.add("gfx/invobjs/basalt");
        resourcePaths.add("gfx/invobjs/batrock");
        resourcePaths.add("gfx/invobjs/blackcoal");
        resourcePaths.add("gfx/invobjs/magnetite");
        resourcePaths.add("gfx/invobjs/bloodore");
        resourcePaths.add("gfx/invobjs/breccia");
        resourcePaths.add("gfx/invobjs/cassiterite");
        resourcePaths.add("gfx/invobjs/catgold");
        resourcePaths.add("gfx/invobjs/chalcopyrite");
        resourcePaths.add("gfx/invobjs/chert");
        resourcePaths.add("gfx/invobjs/cinnabar");
        resourcePaths.add("gfx/invobjs/diabase");
        resourcePaths.add("gfx/invobjs/diorite");
        resourcePaths.add("gfx/invobjs/direveinore");
        resourcePaths.add("gfx/invobjs/dolomite");
        resourcePaths.add("gfx/invobjs/dross");
        resourcePaths.add("gfx/invobjs/eclogite");
        resourcePaths.add("gfx/invobjs/feldspar");
        resourcePaths.add("gfx/invobjs/flint");
        resourcePaths.add("gfx/invobjs/fluorospar");
        resourcePaths.add("gfx/invobjs/gabbro");
        resourcePaths.add("gfx/invobjs/galena");
        resourcePaths.add("gfx/invobjs/gneiss");
        resourcePaths.add("gfx/invobjs/granite");
        resourcePaths.add("gfx/invobjs/graywacke");
        resourcePaths.add("gfx/invobjs/greenschist");
        resourcePaths.add("gfx/invobjs/heavyearthore");
        resourcePaths.add("gfx/invobjs/hornsilver");
        resourcePaths.add("gfx/invobjs/hornblende");
        resourcePaths.add("gfx/invobjs/ironochreore");
        resourcePaths.add("gfx/invobjs/jasper");
        resourcePaths.add("gfx/invobjs/korundstone");
        resourcePaths.add("gfx/invobjs/kyanite");
        resourcePaths.add("gfx/invobjs/lavarock");
        resourcePaths.add("gfx/invobjs/leadglance");
        resourcePaths.add("gfx/invobjs/leaf");
        resourcePaths.add("gfx/invobjs/limestone");
        resourcePaths.add("gfx/invobjs/malachite");
        resourcePaths.add("gfx/invobjs/marble");
        resourcePaths.add("gfx/invobjs/meteorite");
        resourcePaths.add("gfx/invobjs/mica");
        resourcePaths.add("gfx/invobjs/microlite");
        resourcePaths.add("gfx/invobjs/obsidian");
        resourcePaths.add("gfx/invobjs/olivine");
        resourcePaths.add("gfx/invobjs/orthoclase");
        resourcePaths.add("gfx/invobjs/peacockore");
        resourcePaths.add("gfx/invobjs/pegmatite");
        resourcePaths.add("gfx/invobjs/porphyry");
        resourcePaths.add("gfx/invobjs/pumice");
        resourcePaths.add("gfx/invobjs/quarryquartz");
        resourcePaths.add("gfx/invobjs/quartz");
        resourcePaths.add("gfx/invobjs/rhyolite");
        resourcePaths.add("gfx/invobjs/rockcrystal");
        resourcePaths.add("gfx/invobjs/sandstone");
        resourcePaths.add("gfx/invobjs/schist");
        resourcePaths.add("gfx/invobjs/schrifterzore");
        resourcePaths.add("gfx/invobjs/serpentine");
        resourcePaths.add("gfx/invobjs/shardofconch");
        resourcePaths.add("gfx/invobjs/silvershineore");
        resourcePaths.add("gfx/invobjs/slag");
        resourcePaths.add("gfx/invobjs/slate");
        resourcePaths.add("gfx/invobjs/soapstone");
        resourcePaths.add("gfx/invobjs/sodalite");
        resourcePaths.add("gfx/invobjs/sunstone");
        resourcePaths.add("gfx/invobjs/wineglanceore");
        resourcePaths.add("gfx/invobjs/zincspar");






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
