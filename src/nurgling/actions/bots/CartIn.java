package nurgling.actions.bots;

import haven.*;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.conf.DatabaseUtils;
import nurgling.tasks.ChangeModelAtrib;
import nurgling.tasks.IsMoving;
import nurgling.tasks.WaitPose;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static haven.OCache.posres;


public class CartIn implements Action {
    private int getPerceptionValue(NGameUI gui) {
        // Поиск окна с атрибутами в интерфейсе
        if (gui.chrwdg != null && gui.chrwdg.battr != null) {
            List<BAttrWnd.Attr> attrs = gui.chrwdg.battr.attrs;
            for (BAttrWnd.Attr attr : attrs) {
                if (attr.nm.equals("prc")) {
                    return attr.attr.comp; // Возвращаем текущее значение Perception
                }
            }
        }
        return -1; // Возвращаем -1, если не нашли атрибут
    }
    private int getExplorationValue(NGameUI gui) {
        if (gui.chrwdg != null && gui.chrwdg.sattr != null) {
            List<SAttrWnd.SAttr> attrs = (List<SAttrWnd.SAttr>) gui.chrwdg.sattr.attrs;
            for (SAttrWnd.SAttr attr : attrs) {
                if (attr.nm.equals("explore")) {
                    return attr.attr.comp; // Возвращаем текущее значение Exploration
                }
            }
        }
        return -1; // Возвращаем -1, если не нашли атрибут
    }
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Select the target area

//        int exploration = getExplorationValue(gui);
//        System.out.println("Exploration: " + exploration);
//        int perception = getPerceptionValue(gui);
//        System.out.println("Perception: " + perception);
//        //DatabaseUtils.addItemsWithTiming(1);
        SelectArea targetArea;
        NUtils.getGameUI().msg("Please, select intput area.");
        (targetArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> targetZone = targetArea.getRCArea();

        Gob cart = Finder.findNearestObject(new NAlias("cart"), 1000);
        if (cart == null) {
            NUtils.getGameUI().msg("Cart not found.");
            return Results.ERROR("No cart found nearby.");
        }
        int maxGobsInCart = 6; // Максимальное количество гобов, которое можно положить в телегу
        int gobsInCart = 0;
        // Находим все объекты (гобы) в исходной зоне
        ArrayList<Gob> objectsToMove;
        NAlias lifted = new NAlias(new ArrayList<String>(List.of("")),new ArrayList<String>(Arrays.asList("body")));
        while (!(objectsToMove = Finder.findGobs(targetZone, lifted)).isEmpty()) {
            if (gobsInCart >= maxGobsInCart) {
                NUtils.getGameUI().msg("Cart is full (6 objects).");
                return Results.SUCCESS(); // Завершаем, если телега полная
            }
            // Фильтруем объекты, до которых можно дойти
            ArrayList<Gob> availableObjects = new ArrayList<>();
            for (Gob currGob : objectsToMove) {
                if (PathFinder.isAvailable(currGob) && currGob.ngob.isLiftable()) {
                    availableObjects.add(currGob);
                }
            }

            // Если не осталось доступных объектов, выводим сообщение об ошибке
            if (availableObjects.isEmpty()) {
                NUtils.getGameUI().msg("No reachable objects found.");
                return Results.ERROR("Cannot reach object.");
            }

            // Сортируем объекты по близости к игроку
            availableObjects.sort(NUtils.d_comp);
            Gob objectToMove = availableObjects.get(0);  // Берём ближайший объект

            // Поднимаем объект
            new LiftObject(objectToMove).run(gui);

            // Перемещаемся к телеге
            new PathFinder(cart).run(gui);

            // Складываем объект в телегу
            long old = cart.ngob.getModelAttribute();
            NUtils.activateGob(cart);
            NUtils.getUI().core.addTask(new ChangeModelAtrib(cart, old));
            //new PlaceObject(cart, NUtils.player().rc, 0).run(gui); // Параметры позиции и угла можно уточнить, если нужно
            gobsInCart++;
        }
        NUtils.getGameUI().msg("All objects moved successfully.");
        return Results.SUCCESS();
    }
}
