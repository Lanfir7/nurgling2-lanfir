package nurgling.conf;

import haven.Coord;
import haven.Coord2d;
import nurgling.NUtils;

import java.util.ArrayList;
import java.util.List;

import static haven.OCache.posres;

public class PathFollower {
    public List<Coord2d> pathQueue = new ArrayList<>();
    private boolean isMoving = false;

    public void addPoint(Coord2d coord) {
        pathQueue.add(coord);
        if (!isMoving) {
            moveToNextPoint();
        }
    }

    public void clear() {
        pathQueue.clear();
        isMoving = false;
    }

    private void moveToNextPoint() {
        if (!pathQueue.isEmpty()) {
            Coord2d nextCoord = pathQueue.remove(0);
            moveTo(nextCoord);
        }
    }

    public void moveTo(Coord2d destination) {
        NUtils.getGameUI().map.wdgmsg("click", Coord.z, destination.floor(posres), 1, 0);  // Перемещение персонажа
        isMoving = true;
    }

    public void onReachDestination() {
        isMoving = false;
        if (!pathQueue.isEmpty()) {
            moveToNextPoint();
        }
    }
}
