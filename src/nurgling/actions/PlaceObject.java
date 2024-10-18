package nurgling.actions;

import haven.Coord2d;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NGob;
import nurgling.NHitBox;
import nurgling.NUtils;
import nurgling.tools.Finder;

import static nurgling.tools.Finder.findLiftedbyPlayer;

public class PlaceObject implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        if (gob == null)
            gob = findLiftedbyPlayer();
        if (gob != null) {
//            // Рассчитываем ближайший объект рядом с позицией укладки
//            Gob nearestGob = Finder.findNearestObject(pos, 25); // допустим радиус поиска 25 тайлов
//
//            // Если есть ближайший объект, проверяем его хитбокс
//            if (nearestGob != null && nearestGob.ngob.hitBox != null) {
//                NHitBox currentHitBox = gob.ngob.hitBox;
//                NHitBox nearestHitBox = nearestGob.ngob.hitBox;
//
//                // Сравниваем длину хитбоксов (по оси X)
//                double currentLength = currentHitBox.end.x - currentHitBox.begin.x; // длина текущего объекта
//                double nearestLength = nearestHitBox.end.x - nearestHitBox.begin.x; // длина ближайшего объекта
//                double currentW = currentHitBox.end.y - currentHitBox.begin.y; // ширина текущего объекта
//
//                if (nearestLength > currentLength && currentW < 6) {
//                    double shift = 6 - currentW; // Разница до 6 тайлов
//                    NUtils.getGameUI().msg("Был сдвиг");
//                    pos = pos.add(0, shift); // Сдвигаем только по оси X
//
//                }
//            }

            // Ищем путь и укладываем объект
            PathFinder pf = new PathFinder(NGob.getDummy(pos, a, gob.ngob.hitBox), true);
            pf.isHardMode = true;
            pf.run(gui);

            NUtils.place(gob, pos, a);
            return Results.SUCCESS();
        }
        return Results.ERROR("No gob for place");
    }

    public PlaceObject(Gob gob, Coord2d pos, double a) {
        this.gob = gob;
        this.pos = pos;
        this.a = a;
    }

    Gob gob = null;
    Coord2d pos = null;
    double a = 0;
}