package nurgling.actions;

import haven.*;
import nurgling.NFlowerMenu;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.tasks.NFlowerMenuIsClosed;
import nurgling.tasks.WaitCollectState;
import nurgling.tasks.WaitPose;
import nurgling.tools.NAlias;

import static haven.OCache.posres;

public class CollectFromGob implements Action {

    Gob target;
    String action;
    String pose;
    boolean withPiles = false;
    Coord targetSize = null;

    public CollectFromGob(Gob target, String action, String pose, Coord targetSize, NAlias targetItems, Pair<Coord2d, Coord2d> pileArea) {
        this.target = target;
        this.action = action;
        this.pose = pose;
        this.targetSize = targetSize;
        this.withPiles = true;
        this.targetItems = targetItems;
        this.pileArea = pileArea;
    }

    NAlias targetItems;
    Pair<Coord2d, Coord2d> pileArea = null;

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        WaitCollectState wcs = null;
        boolean success = false;  // Флаг для успешного сбора
        int attempts = 0;  // Счетчик попыток сборов

        while (attempts < 2) {  // Цикл для двух попыток сбора с одного объекта
            // Проверяем свободное место в инвентаре перед сбором
            if (NUtils.getGameUI().getInventory().getNumberFreeCoord(targetSize) == 0) {
                if (withPiles) {
                    new TransferToPiles(pileArea, targetItems).run(gui);
                }
            }

            // Пытаемся взаимодействовать с объектом "Dreca"
            if (target != null) {
                gui.map.wdgmsg("click", Coord.z, target.rc.floor(posres), 3, 0, 1, (int) target.id, target.rc.floor(posres), 0, -1);
            } else {
                return Results.FAIL();
            }

            NFlowerMenu fm = NUtils.findFlowerMenu();
            if (fm != null) {
                if (fm.hasOpt(action)) {
                    success = true;  // Нужное действие найдено, продолжаем сбор

                    // Двигаемся к объекту для выполнения действия
                    if (target != null) {
                        new PathFinder(target).run(gui);
                    } else {
                        return Results.FAIL();
                    }

                    // Выполняем выбор действия из меню
                    if (fm.chooseOpt(action)) {
                        NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());

                        // Если цель - Dreca, не ждем позу, а проверяем появление ресурса в инвентаре
                        if (pose == null) {
                            wcs = new WaitCollectState(target, targetSize);
                            NUtils.getUI().core.addTask(wcs);
                        } else {
                            NUtils.getUI().core.addTask(new WaitPose(NUtils.player(), pose));
                            wcs = new WaitCollectState(target, targetSize);
                            NUtils.getUI().core.addTask(wcs);
                        }

                        // После успешного сбора увеличиваем счетчик
                        attempts++;
                    } else {
                        NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());
                    }
                } else {
                    // Если меню не содержит нужного действия, закрываем и выходим
                    fm.wdgmsg("cl", -1);
                    NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());
                    return Results.SUCCESS();  // Переход к следующему объекту
                }
            } else {
                return Results.FAIL();  // Если меню не появилось
            }

            // Если оба ресурса собраны (2 попытки), выходим
            if (attempts >= 2) {
                break;
            }
        }

        // Если ни одно действие не было выполнено успешно, переходим к следующему объекту
        if (!success) {
            return Results.FAIL();  // Если ничего не было собрано, бот считает это неудачей
        }

        return Results.SUCCESS();
    }
}