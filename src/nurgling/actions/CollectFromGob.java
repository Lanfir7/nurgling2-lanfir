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
    String action; // Одиночное действие
    NAlias actions; // Множественные действия
    String pose;
    boolean withPiles = false;
    Coord targetSize = null;
    int marker = -1;

    // Конструктор для одиночного действия (String)
    public CollectFromGob(Gob target, String action, String pose, Coord targetSize, NAlias targetItems, Pair<Coord2d, Coord2d> pileArea) {
        this.target = target;
        this.action = action;
        this.pose = pose;
        this.targetSize = targetSize;
        this.withPiles = true;
        this.targetItems = targetItems;
        this.pileArea = pileArea;
    }

    // Конструктор для списка действий (NAlias)
    public CollectFromGob(Gob target, NAlias actions, String pose, Coord targetSize, NAlias targetItems, Pair<Coord2d, Coord2d> pileArea) {
        this.target = target;
        this.actions = actions;
        this.pose = pose;
        this.targetSize = targetSize;
        this.withPiles = true;
        this.targetItems = targetItems;
        this.pileArea = pileArea;
    }

    // Конструктор для одиночного действия с дополнительными параметрами
    public CollectFromGob(Gob target, String action, String pose, boolean withPiles, Coord targetSize, int marker, NAlias targetItems, Pair<Coord2d, Coord2d> pileArea) {
        this(target, new NAlias(action), pose, withPiles, targetSize, marker, targetItems, pileArea);
        this.action = action;
    }

    // Конструктор для списка действий с дополнительными параметрами
    public CollectFromGob(Gob target, NAlias actions, String pose, boolean withPiles, Coord targetSize, int marker, NAlias targetItems, Pair<Coord2d, Coord2d> pileArea) {
        this.target = target;
        this.actions = actions;
        this.pose = pose;
        this.withPiles = withPiles;
        this.targetSize = targetSize;
        this.marker = marker;
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

        while (attempts < 3) {  // Цикл для нескольких попыток сбора с одного объекта
            if (NUtils.getGameUI().getInventory().getNumberFreeCoord(targetSize) == 0) {
                if (withPiles) {
                    new TransferToPiles(pileArea, targetItems).run(gui);
                }
            }

            // Взаимодействуем с объектом
            if (target != null) {
                gui.map.wdgmsg("click", Coord.z, target.rc.floor(posres), 3, 0, 1, (int) target.id, target.rc.floor(posres), 0, -1);
            } else {
                return Results.FAIL();
            }

            if(marker != -1) {
                if((target.ngob.getModelAttribute() & marker) != marker) {
                    return Results.SUCCESS();
                }
            }

            NFlowerMenu fm = NUtils.findFlowerMenu();
            if (fm != null) {
                String chosenAction = null;

                if (action != null) {  // Если передан одиночный action
                    if (fm.hasOpt(action)) {
                        chosenAction = action;
                    }
                } else if (actions != null) {  // Если передан NAlias
                    for (String act : actions.keys) {
                        if (fm.hasOpt(act)) {
                            chosenAction = act;
                            break;
                        }
                    }
                }

                if (chosenAction != null) {
                    success = true;  // Нужное действие найдено

                    if (target != null) {
                        new PathFinder(target).run(gui);
                    } else {
                        return Results.FAIL();
                    }

                    if (fm.chooseOpt(chosenAction)) {
                        NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());

                        if (pose == null) {
                            wcs = new WaitCollectState(target, targetSize);
                            NUtils.getUI().core.addTask(wcs);
                        } else {
                            NUtils.getUI().core.addTask(new WaitPose(NUtils.player(), pose));
                            wcs = new WaitCollectState(target, targetSize);
                            NUtils.getUI().core.addTask(wcs);
                        }

                        attempts++;
                    } else {
                        NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());
                    }
                } else {
                    fm.wdgmsg("cl", -1);
                    NUtils.getUI().core.addTask(new NFlowerMenuIsClosed());
                    return Results.SUCCESS();
                }
            } else {
                return Results.FAIL();
            }

            if (attempts >= 3) {
                break;
            }
        }

        if (!success) {
            return Results.FAIL();
        }

        return Results.SUCCESS();
    }
}
