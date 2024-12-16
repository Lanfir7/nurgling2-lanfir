package nurgling.conf;

import haven.Coord;
import haven.Coord2d;
import nurgling.NUtils;

import java.util.ArrayList;
import java.util.List;

import static haven.OCache.posres;

public class PathFollower {
    public interface Listener {
        void pathCompleted();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public List<Coord2d> pathQueue = new ArrayList<>();
    private boolean isMoving = false;
    private Coord2d currentDestination = null;

    public void addPoint(Coord2d coord) {
        pathQueue.add(coord);
        if (!isMoving) {
            moveToNextPoint();
        }
    }

    public void clear() {
        pathQueue.clear();
        isMoving = false;
        currentDestination = null;
    }

    public boolean isMoving() {
        return isMoving;
    }
    public void setPoints(List<Coord2d> coords) {
        clear();
        pathQueue.addAll(coords);
        // Как только список точек установлен, можно сразу начать движение к первой точке
        if (!pathQueue.isEmpty()) {
            moveToNextPoint();
        }
    }

    public void moveToNextPoint() {
        if (!pathQueue.isEmpty()) {
            Coord2d nextCoord = pathQueue.remove(0);
            moveTo(nextCoord);
        } else {
            if (listener != null) {
                listener.pathCompleted();
            }
        }
    }

    public void moveTo(Coord2d destination) {
        currentDestination = destination;
        NUtils.getGameUI().map.wdgmsg("click", Coord.z, destination.floor(posres), 1, 0);
        isMoving = true;
    }

    public void tick() {
        if (isMoving && currentDestination != null) {
            Coord2d playerPos = NUtils.player().rc;
            double dist = playerPos.dist(currentDestination);
            if (dist < 1.0) {
                onReachDestination();
            }
        }
    }

    public void onReachDestination() {
        isMoving = false;
        currentDestination = null;
        moveToNextPoint();
    }
}
