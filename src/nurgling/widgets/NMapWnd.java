package nurgling.widgets;
import haven.*;
import nurgling.NMapView;
import nurgling.widgets.*;

import java.awt.event.*;
import java.util.Map;
import haven.*;
import nurgling.NUtils;
import nurgling.conf.PathFollower;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static haven.MCache.cmaps;
import static haven.MCache.tilesz;
import static haven.OCache.posres;

public class NMapWnd extends MapWnd {
    private boolean switching = true;
    private boolean altPressed = false;
    private PathFollower pathFollower = new PathFollower();
    private boolean recordingRoute = false;
    private List<Coord> waypoints = new ArrayList<>();

    public NMapWnd(MapFile file, MapView mv, Coord sz, String title) {
        super(file, mv, sz, title);

    }

    public class GobMarker extends MapFile.Marker {
        public final long gobid;
        public final Indir<Resource> res;
        private Coord2d rc = null;
        public final Color col;

        public GobMarker(Gob gob) {
            super(0, gob.rc.floor(tilesz), /*gob.tooltip()*/"");
            this.gobid = gob.id;
            GobIcon icon = gob.getattr(GobIcon.class);
            res = (icon == null) ? null : icon.res;
            col = color(gob);
        }

        private Color color(Gob gob) {
            return Color.LIGHT_GRAY;
        }

        public void update() {
            Gob gob = ui.sess.glob.oc.getgob(gobid);
            if(gob != null) {
                seg = view.sessloc.seg.id;
                try {
                    rc = gob.rc.add(view.sessloc.tc.mul(tilesz));
                    tc = rc.floor(tilesz);
                } catch (Exception ignore) {}
            }
        }

        public Coord2d rc() {
            try {
                return rc.sub(view.sessloc.tc.mul(tilesz));
            } catch (Exception ignore) {}
            return null;
        }

        @Override
        public int hashCode() {
            return Objects.hash(gobid);
        }
    }
    @Override
    public boolean keydown(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_ALT && !recordingRoute) {
            altPressed = true;
            recordingRoute = true;
            pathFollower.clear();
            //waypoints.clear(); // Start a new route
            return true;
        }
        return super.keydown(ev);
    }

    @Override
    public boolean keyup(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_ALT) {
            altPressed = false;
            recordingRoute = false;
            //List<Coord2d> worldWaypoints = convertWaypointsToWorldCoords();
            //pathFollower.setPath(worldWaypoints);
            //sendMovementCommands(); // Initiate movement along the route
            waypoints.clear(); // Clear the waypoints for the next route
            return true;
        }
        return super.keyup(ev);
    }
    @Override
    public boolean mousedown(Coord c, int button) {
        if (recordingRoute && button == 1) {
            waypoints.add(c);
            // Конвертируем координаты клика по карте в мировые координаты
            Coord2d worldCoord = mapToWorld(c);
            if (worldCoord != null) {
                pathFollower.addPoint(worldCoord);  // Добавляем мировые координаты в маршрут
                System.out.println("точка игрока: " + NUtils.player().rc.floor(posres));
                System.out.println("Добавлена точка для перемещения: " + worldCoord);
                return true;
            } else {
                System.out.println("Не удалось преобразовать координаты клика в мировые координаты.");
            }
        }
        return super.mousedown(c, button);
    }
    private List<Coord2d> convertWaypointsToWorldCoords() {
        List<Coord2d> worldCoords = new ArrayList<>();
        for (Coord mapCoord : waypoints) {
            Coord2d worldCoord = mapToWorld(mapCoord);
            if (worldCoord != null) {
                worldCoords.add(worldCoord);
            }
        }
        return worldCoords;
    }

    private Coord2d mapToWorld(Coord c) {
        // Получаем объект Location по координатам клика на карте
        MiniMap.Location clickLoc = view.xlate(c);
        if (clickLoc == null)
            return null;

        // Вычисляем мировые координаты клика
        Coord2d click_wc = clickLoc.tc.mul(MCache.tilesz).add(MCache.tilesz.div(2));

        return click_wc;
    }


    @Override
    public void draw(GOut g) {
        super.draw(g);

        // Отрисовываем маршрут на карте
        if (waypoints.size() > 1) {
            g.chcolor(Color.CYAN);
            Coord lastCoord = waypoints.get(0);
            for (int i = 1; i < waypoints.size(); i++) {
                Coord mapCoord = waypoints.get(i);
                g.line(lastCoord, mapCoord, 2);
                lastCoord = mapCoord;
            }
            g.chcolor();
        }
    }
    public long playerSegmentId() {
        MiniMap.Location sessloc = view.sessloc;
        if(sessloc == null) {return 0;}
        return sessloc.seg.id;
    }

    public MiniMap.Location playerLocation() {
        return view.sessloc;
    }

    public Coord2d findMarkerPosition(String name) {
        MiniMap.Location sessloc = view.sessloc;
        if(sessloc == null) {return null;}
        for (Map.Entry<Long, MapFile.SMarker> e : file.smarkers.entrySet()) {
            MapFile.SMarker m = e.getValue();
            if(m.seg == sessloc.seg.id && m.nm!= null && name!=null && m.nm.contains(name)) {
                return m.tc.sub(sessloc.tc).mul(tilesz);
            }
        }
        return null;
    }
}
