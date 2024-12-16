package nurgling.widgets;

import haven.*;
import nurgling.NMapView;
import nurgling.NUtils;
import nurgling.conf.PathFollower;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static haven.MCache.tilesz;
import static haven.OCache.posres;

public class NMapWnd extends MapWnd {
    // Флаги и данные для записи и отрисовки пути
    private boolean altPressed = false;
    private boolean recordingRoute = false;
    private PathFollower pathFollower = new PathFollower();
    private List<Coord2d> waypoints = new ArrayList<>();

    public NMapWnd(MapFile file, MapView mv, Coord sz, String title) {
        super(file, mv, sz, title);
        setfocus(view);
    }

    public class GobMarker extends MapFile.Marker {
        public final long gobid;
        public final Indir<Resource> res;
        private Coord2d rc = null;
        public final Color col;

        public GobMarker(Gob gob) {
            super(0, gob.rc.floor(tilesz), "");
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
            if (gob != null) {
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
            System.out.println("ALT pressed - starting route recording...");
            altPressed = true;
            recordingRoute = true;
            pathFollower.clear();
            waypoints.clear();
            return true;
        }
        return super.keydown(ev);
    }

    @Override
    public boolean keyup(KeyEvent ev) {
        System.out.println("Key up: " + ev.getKeyCode());
        if (ev.getKeyCode() == KeyEvent.VK_ALT) {
            System.out.println("ALT released - finishing route recording...");
            altPressed = false;
            recordingRoute = false;
            if (!waypoints.isEmpty() && !pathFollower.isMoving()) {
                // Передаем все точки пути в pathFollower
                for (Coord2d p : waypoints) {
                    pathFollower.addPoint(p);
                }
            }
            return true;
        }
        return super.keyup(ev);
    }

    @Override
    public boolean mousedown(Coord c, int button) {
        if (recordingRoute && button == 1) {
            // Переводим координаты из системы NMapWnd в систему miniMap (view)
            Coord vc = c.sub(view.c);
            Coord2d worldCoord = mapToWorld(vc);
            if (worldCoord != null) {
                waypoints.add(worldCoord);
                pathFollower.addPoint(worldCoord);
                //System.out.println("Player position: " + NUtils.player().rc);
                Coord2d delta = worldCoord.sub(view.dloc.tc.mul(MCache.tilesz));
                double scale = view.scalef() * MCache.tilesz.x;
                Coord2d scaled = delta.div(scale);
                Coord mapCoord = scaled.round().add(view.sz.div(2));
                System.out.println("Added movement point worldCoord: " + worldCoord);
                System.out.println("Added movement point delta: " + delta);
                System.out.println("Added movement point scaled: " + scaled);
                System.out.println("Added movement point mapCoord: " + mapCoord);
                System.out.println("Added movement point:RAW" + vc);
                System.out.println("Added movement point MAP: " + worldToMap(worldCoord));
                return true;
            } else {
                System.out.println("Failed to convert map click to world coordinates.");
            }
        }
        return super.mousedown(c, button);
    }

    /**
     * Преобразование координат клика на миникарте в глобальные координаты мира.
     * Учитываем текущее положение сессии (sessloc.tc), чтобы точки совпадали с глобальными координатами.
     */
    private Coord2d mapToWorld(Coord vc) {
        MiniMap.Location clickLoc = view.xlate(vc);
        if (clickLoc == null || view.sessloc == null || clickLoc.seg != view.sessloc.seg)
            return null;

        // Вычисляем глобальные координаты
        Coord2d globalCoord = clickLoc.tc.sub(view.sessloc.tc).mul(MCache.tilesz).add(MCache.tilesz.div(2)).sub(MCache.tilesz.mul(47));//6
        return globalCoord;
    }

    private Coord worldToMap(Coord2d worldCoord) {
        if (view.dloc == null)
            return null;

        Coord2d delta = worldCoord.sub(view.dloc.tc.mul(MCache.tilesz));
        double scale = view.scalef() * MCache.tilesz.x;
        Coord2d scaled = delta.div(scale);
        Coord mapCoord = scaled.round().add(view.sz.div(2));
        return mapCoord;
    }


    @Override
    public void draw(GOut g) {

        super.draw(g);
        // Отрисовка линии между точками маршрута
        if (waypoints.size() > 1) {
            g.chcolor(Color.cyan);
            Coord lastCoord = null;
            for (Coord2d worldCoord : waypoints) {
                Coord mapCoord = worldToMap(worldCoord);
                if (mapCoord != null) {
                    if (lastCoord != null) {
                        g.line(lastCoord, mapCoord, 2);
                    }
                    lastCoord = mapCoord;
                }
            }
            g.chcolor();
        }

    }

    @Override
    public void tick(double dt) {
        pathFollower.tick();
        super.tick(dt);
        if (ui != null && ui.root.focused != view) {
            setfocus(view); // Если фокус не на карте, устанавливаем
        }
    }

    public void pathCompleted() {
        waypoints.clear();
    }

    public long playerSegmentId() {
        MiniMap.Location sessloc = view.sessloc;
        if (sessloc == null) {return 0;}
        return sessloc.seg.id;
    }

    public MiniMap.Location playerLocation() {
        return view.sessloc;
    }

    public Coord2d findMarkerPosition(String name) {
        MiniMap.Location sessloc = view.sessloc;
        if (sessloc == null) {return null;}
        for (Map.Entry<Long, MapFile.SMarker> e : file.smarkers.entrySet()) {
            MapFile.SMarker m = e.getValue();
            if(m.seg == sessloc.seg.id && m.nm != null && name != null && m.nm.contains(name)) {
                return m.tc.sub(sessloc.tc).mul(tilesz);
            }
        }
        return null;
    }

    public void addMarker(Coord at, String name) {
        // Можно реализовать добавление маркера, если нужно
    }
}
