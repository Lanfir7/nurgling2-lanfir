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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class testingRes implements Action {
    //private static final Logger logger = Logger.getLogger(ResourceChecker.class.getName());
    private static final String LOG_FILE = "missing_resources.log";
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        ArrayList<String> resourcePaths = new ArrayList<>();
        resourcePaths.add("gfx/invobjs/herbs/buttonmushroom");
        resourcePaths.add("gfx/invobjs/herbs/chantrelles");
        // Добавляй сюда остальные ресурсы

        checkResources(resourcePaths);
        return Results.SUCCESS();
    }
    public static void checkResources(ArrayList<String> resourcePaths) {
        for (String path : resourcePaths) {
            try {
                // Предполагаемая функция для загрузки ресурса
                loadResource(path);
            } catch (ResourceNotFoundException e) {
                NUtils.getGameUI().msg("Please, select intput area."+ path);
            }
        }
    }

    private static void loadResource(String path) throws ResourceNotFoundException {
        // Симуляция загрузки ресурса
        boolean resourceExists = checkResource(path);
        if (!resourceExists) {
            NUtils.getGameUI().msg("Please, select intput area."+ path);
        }
    }

    private static boolean checkResource(String path) {
        // Здесь можно реализовать логику проверки ресурса
        // Например, проверка наличия файла по пути
        return false; // Симуляция отсутствующего ресурса
    }

    private static void logMissingResource(String path) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write("Missing resource: " + path + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
