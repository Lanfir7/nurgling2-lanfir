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
//    private boolean switching = true;
//    private boolean altPressed = false;
//    private PathFollower pathFollower = new PathFollower();
//    private boolean recordingRoute = false;
//    private List<Coord2d> waypoints = new ArrayList<>();

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
//    @Override
//    public boolean keydown(KeyEvent ev) {
//        if (ev.getKeyCode() == KeyEvent.VK_ALT && !recordingRoute) {
//            altPressed = true;
//            recordingRoute = true;
//            pathFollower.clear();
//            waypoints.clear();
//            return true;
//        }
//        return super.keydown(ev);
//    }

//    @Override
//    public boolean keyup(KeyEvent ev) {
//        if (ev.getKeyCode() == KeyEvent.VK_ALT) {
//            altPressed = false;
//            recordingRoute = false;
//            if (!pathFollower.isMoving()) {
//                pathFollower.moveToNextPoint();
//            }
//            return true;
//        }
//        return super.keyup(ev);
//    }
//    @Override
//    public boolean mousedown(Coord c, int button) {
//        if (recordingRoute && button == 1) {
//            Coord2d worldCoord = mapToWorld(c);
//            if (worldCoord != null) {
//                waypoints.add(worldCoord);
//                pathFollower.addPoint(worldCoord);
//                System.out.println("Player position: " + NUtils.player().rc.floor(posres));
//                System.out.println("Added movement point: " + worldCoord);
//                return true;
//            } else {
//                System.out.println("Failed to convert map click to world coordinates.");
//            }
//        }
//        return super.mousedown(c, button);
//    }
//    private Coord worldToMap(Coord2d worldCoord) {
//        if (view.dloc == null)
//            return null;
//
//        Coord2d delta = worldCoord.sub(view.dloc.tc.mul(MCache.tilesz));
//
//        double scale = view.scalef() * MCache.tilesz.x;
//
//        Coord2d scaled = delta.div(scale);
//
//        Coord mapCoord = scaled.round().add(view.sz.div(2));
//
//        return mapCoord;
//    }
//
//    private Coord2d mapToWorld(Coord c) {
//        MiniMap.Location clickLoc = view.xlate(c);
//        if (clickLoc == null)
//            return null;
//
//        Coord2d click_wc = clickLoc.tc.mul(MCache.tilesz).add(MCache.tilesz.div(2));
//
//        return click_wc;
//    }


//    @Override
//    public void draw(GOut g) {
//        super.draw(g);
//
//        if (waypoints.size() > 1) {
//            g.chcolor(Color.CYAN);
//            Coord lastCoord = null;
//            for (Coord2d worldCoord : waypoints) {
//                Coord mapCoord = worldToMap(worldCoord);
//                if (mapCoord != null) {
//                    if (lastCoord != null) {
//                        g.line(lastCoord, mapCoord, 2);
//                    }
//                    lastCoord = mapCoord;
//                }
//            }
//            g.chcolor();
//        }
//    }
//    @Override
//    public void tick(double dt) {
//        super.tick(dt);
//        pathFollower.tick();
//    }

//    public void pathCompleted() {
//        waypoints.clear();
//    }
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
