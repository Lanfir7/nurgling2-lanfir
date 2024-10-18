package nurgling.actions;

import haven.*;
import nurgling.*;

public class LiftObject implements Action {
    @Override
    public Results run ( NGameUI gui )
            throws InterruptedException {
        if ( gob != null ) {
//            // Получаем позиции гоба и игрока
//            Coord2d gobPos = gob.rc;
//            Coord2d playerPos = NUtils.player().rc;
//
//            // Получаем хитбокс гоба
//            NHitBox gobHitBox = gob.ngob.hitBox;
//
//            // Рассчитываем направление сдвига по оси X: от гоба к игроку
//            double xDirection = Math.signum(playerPos.x - gobPos.x); // Если игрок левее гоба, сдвигаем влево, иначе вправо
//            double yDirection = Math.signum(playerPos.y - gobPos.y); // Если игрок левее гоба, сдвигаем влево, иначе вправо
//
//            // Рассчитываем сдвиг с учетом хитбокса (по оси X)
//            double hitboxWidth = gobHitBox.end.x - gobHitBox.begin.x;
//            double hitboxl = gobHitBox.end.y - gobHitBox.begin.y;
//
//            // Сдвиг по X на 5 тайлов + половина ширины хитбокса для точного позиционирования
//            Coord2d newPos = gobPos.add(xDirection * hitboxWidth , yDirection *(hitboxl/2));
//
//            new PathFinder ( newPos ).run(gui);
            new PathFinder(gob).run(gui);
            NUtils.lift (gob);
            return Results.SUCCESS();
        }
        return Results.ERROR("No gob for Lift");
    }

    public LiftObject (
            Gob gob

    ) {
        this.gob = gob;
    }

    Gob gob = null;
}