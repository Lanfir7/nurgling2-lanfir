package nurgling.tools;

import haven.*;
import haven.res.gfx.fx.eq.Equed;
import nurgling.*;
import nurgling.areas.*;
import nurgling.pf.*;
import nurgling.tasks.*;

import java.util.*;
import java.util.regex.Pattern;

public class Finder
{
    public static ArrayList<Gob> findNearbyObjects(NAlias objectName, double radius) throws InterruptedException {
        ArrayList<Gob> result = new ArrayList<>();
        Coord2d playerPos = NUtils.player().rc;

        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    // Проверяем, что объект соответствует переданному названию
                    if (NParser.isIt(gob, objectName)) {
                        double dist = gob.rc.dist(playerPos);
                        if (dist <= radius) {
                            result.add(gob);
                        }
                    }
                }
            }
        }

        // Сортируем объекты по расстоянию от игрока
        result.sort(Comparator.comparingDouble(gob -> gob.rc.dist(playerPos)));
        return result;
    }
    public static Gob findNearestObject(NAlias objectName, double radius) throws InterruptedException {
        Gob nearestGob = null;
        Coord2d playerPos = NUtils.player().rc;
        double minDistance = radius;

        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    // Проверяем, что объект соответствует переданному названию
                    if (NParser.isIt(gob, objectName)) {
                        double dist = gob.rc.dist(playerPos);
                        if (dist <= minDistance) {
                            nearestGob = gob;
                            minDistance = dist; // обновляем минимальное расстояние
                        }
                    }
                }
            }
        }

        return nearestGob;
    }
    public static Gob findNearestObject(Coord2d targetPos, double radius) throws InterruptedException {
        Gob nearestGob = null;
        double minDistance = radius;

        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    double dist = gob.rc.dist(targetPos);
                    if (dist <= minDistance) {
                        nearestGob = gob;
                        minDistance = dist; // обновляем минимальное расстояние
                    }
                }
            }
        }

        return nearestGob;
    }
    public static ArrayList<Gob> findSortedGobsInArea(Pair<Coord2d, Coord2d> space, NAlias name) throws InterruptedException {
        ArrayList<Gob> result = new ArrayList<>();
        Coord2d playerPos = NUtils.player().rc; // Позиция игрока

        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    // Проверяем, что объект находится в пределах заданной зоны
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y) {
                        // Проверяем, соответствует ли объект переданному имени
                        if (NParser.isIt(gob, name)) {
                            result.add(gob); // Добавляем объект в список
                        }
                    }
                }
            }
        }

        // Сортируем объекты по расстоянию до игрока (сначала ближайшие)
        result.sort(Comparator.comparingDouble(gob -> gob.rc.dist(playerPos)));

        return result; // Возвращаем отсортированный список объектов
    }
    static final Comparator<Gob> x_comp = new Comparator<Gob> () {
        @Override
        public int compare(
                Gob lhs,
                Gob rhs
        ) {
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            return (lhs.rc.x > rhs.rc.x) ? -1 : ((lhs.rc.x < rhs.rc.x) ? 1 : (lhs.rc.y > rhs.rc.y) ? -1 : (
                    lhs.rc.y < rhs.rc.y) ? 1 : 0);
        }
    };

    static final Comparator<Gob> y_comp = new Comparator<Gob> () {
        @Override
        public int compare(
                Gob lhs,
                Gob rhs
        ) {
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            return (lhs.rc.y > rhs.rc.y) ? -1 : ((lhs.rc.y < rhs.rc.y) ? 1 : (lhs.rc.x > rhs.rc.x) ? -1 : (
                    lhs.rc.x < rhs.rc.x) ? 1 : 0);
        }
    };

    static void sort(ArrayList<Gob> gobs)
    {
        if(!gobs.isEmpty())
        {
            Coord2d min = new Coord2d(gobs.get(0).rc.x,gobs.get(0).rc.y);
            Coord2d max = new Coord2d(gobs.get(0).rc.x,gobs.get(0).rc.y);
            for(Gob gob: gobs)
            {
                max.x = Math.max(gob.rc.x,max.x);
                max.y = Math.max(gob.rc.y,max.y);
                min.x = Math.min(gob.rc.x,min.x);
                min.y = Math.min(gob.rc.y,min.y);
            }
            if(Math.abs(max.y-min.y) > Math.abs(max.x - min.x))
                gobs.sort(x_comp);
            else
                gobs.sort(y_comp);
        }
    }

    public static ArrayList<Gob> findGobs(NArea area, NAlias name) throws InterruptedException
    {
        Pair<Coord2d,Coord2d> space = area.getRCArea();
        return findGobs(space,name);
    }

    public static ArrayList<Gob> findGobs(Pair<Coord2d,Coord2d> space, NAlias name) throws InterruptedException
    {
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name))
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static ArrayList<Coord2d> findTilesInArea (
            NAlias name,
            Pair<Coord2d,Coord2d> area_rc
    ) {
        ArrayList<Coord2d> result = new ArrayList<> ();
        boolean rev = false;
        for ( double x = area_rc.a.x ; x < area_rc.b.x ; x += 11 ) {
            ArrayList<Coord2d> line = new ArrayList<> ();
            for ( double y = area_rc.a.y ; y < area_rc.b.y ; y += 11 ) {
                Coord pltc = ( new Coord2d ( ( x ) / 11, ( y ) / 11 ) ).floor ();

                if ( NParser.isIt ( pltc, name ) ) {
                    line.add ( new Coord2d ( x, y ) );
                }
            }
            if(rev)
            {
                for(int i = line.size()-1; i >= 0; i--)
                {
                    result.add( line.get(i) );
                }
            }
            else
            {
                result.addAll(line);
            }
            rev = !rev;

        }
        return result;
    }

    public static ArrayList<Gob> findGobs(Area area, NAlias name) throws InterruptedException
    {
        Coord2d b = area.ul.mul(MCache.tilesz);
        Coord2d e = area.br.mul(MCache.tilesz).add(MCache.tilesz);
        Pair<Coord2d,Coord2d> space = new Pair<>(b,e);
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name))
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }



    public static ArrayList<Gob> findGobs(NArea area, NAlias name, int mattr) throws InterruptedException
    {
        Pair<Coord2d,Coord2d> space = area.getRCArea();
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name) && gob.ngob.getModelAttribute() == mattr)
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static Gob findGob(NArea area, NAlias name) throws InterruptedException {
        return findGob(area.getRCArea(),name);
    }

    public static Gob findGob(Pair<Coord2d,Coord2d> space, NAlias name) throws InterruptedException
    {
        NUtils.getUI().core.addTask(new FindPlayer());

        Gob result = null;
        double dist = 10000;
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name) && NUtils.player()!=null)
                        {
                            double new_dist;
                            if((new_dist = gob.rc.dist(NUtils.player().rc))<dist)
                            {
                                dist = new_dist;
                                result = gob;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    public static Gob findGob(NAlias name) throws InterruptedException
    {
        NUtils.getUI().core.addTask(new FindPlayer());
        return findGob(NUtils.player().rc, name, null, 10000);
    }

    public static Gob findGob(Coord2d coord2d, NAlias name, NAlias poses, double dist) throws InterruptedException
    {
        Gob result = null;
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if (NParser.isIt(gob, name) && NUtils.player() != null && gob.id!=NUtils.player().id)
                    {
                        if(poses!=null) {
                            if (gob.pose() != null) {
                                if (NParser.checkName(gob.pose(), poses)) {
                                    double new_dist;
                                    if ((new_dist = gob.rc.dist(coord2d)) < dist) {
                                        dist = new_dist;
                                        result = gob;
                                    }
                                }
                            }
                        }
                        else
                        {
                            double new_dist;
                            if ((new_dist = gob.rc.dist(coord2d)) < dist) {
                                dist = new_dist;
                                result = gob;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Gob> findGobs(Coord2d coord2d, NAlias name, NAlias poses, double dist) throws InterruptedException
    {

        ArrayList<Gob> result = new ArrayList<>();
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if (NParser.isIt(gob, name) && NUtils.player() != null)
                    {
                        if(poses!=null) {
                            if (gob.pose() != null) {
                                if (NParser.checkName(gob.pose(), poses)) {
                                    if(gob.rc.dist(coord2d)<dist)
                                        result.add(gob);
                                }
                            }
                        }
                        else
                        {
                            if(gob.rc.dist(coord2d)<dist)
                                result.add(gob);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Gob findGob(long gobid)
    {
        if(gobid == -1)
        {
            if(NUtils.getGameUI().map.placing!=null)
                return NUtils.getGameUI().map.placing.get();
            return null;
        }
        else
        {
            if(NUtils.getGameUI()!=null)
                return NUtils.getGameUI().ui.sess.glob.oc.getgob(gobid);
            return null;
        }
    }

    public static Gob findGob(Coord pos) {
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    // Только внутри тайла, без пересечений
                    if (gob.id!= NUtils.playerID() && gob.rc.x >=space.a.x && gob.rc.y >=space.a.y && gob.rc.x <=space.b.x && gob.rc.y <=space.b.y)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }

    public static Gob findGob(Coord2d pos) {
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if (gob.id!= NUtils.playerID() && gob.rc.dist(pos)<0.5)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }



    public static Gob findGob(Coord pos, NAlias exc){
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if(gob.ngob!=null && gob.ngob.name!=null && !NParser.checkName(gob.ngob.name,exc)) {
                    if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                        // Только внутри тайла, без пересечений
                        if (gob.id != NUtils.playerID() && gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y) {
                            return gob;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static Gob findLiftedbyPlayer() {
        long plid;
        Following fl;
        if ((plid = NUtils.playerID()) != -1) {
            synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
                for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                    if ((fl = gob.getattr(Following.class)) != null) {

                        if (fl.tgt == plid) {
                            return gob;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Gob findGob(Coord pos, NAlias crop, int stage) {
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    // Только внутри тайла, без пересечений
                    if (gob.id!= NUtils.playerID() && gob.rc.x >=space.a.x && gob.rc.y >=space.a.y && gob.rc.x <=space.b.x && gob.rc.y <=space.b.y && NParser.checkName(gob.ngob.name,crop) && gob.ngob.getModelAttribute()==stage)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }

    public static ArrayList<Gob> findGobs(Area area, NAlias name, int stage) {
        Coord2d b = area.ul.mul(MCache.tilesz);
        Coord2d e = area.br.mul(MCache.tilesz).add(MCache.tilesz);
        Pair<Coord2d,Coord2d> space = new Pair<>(b,e);
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (gob.ngob.name!=null && NParser.checkName(gob.ngob.name, name) && gob.ngob.getModelAttribute() == stage )
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static Coord2d getFreePlace(Pair<Coord2d,Coord2d> area, Gob placed) {
        NHitBox hitBox = placed.ngob.hitBox;

        // Список имен объектов, для которых нужно изменять хитбокс
        NAlias targetNames = new NAlias("kritter");
        NAlias trellis = new NAlias("trellis");

        // Проверяем, соответствует ли объект одному из имен
        try {
            if (NParser.isIt(placed, targetNames)) {
                double width = hitBox.end.y - hitBox.begin.y; // ширина хитбокса

                // Если ширина хитбокса меньше 6, увеличиваем ширину до 6 тайлов
                if (width < 6) {
                    double offset = (6 - width) ;
                    Coord2d newBegin = new Coord2d(hitBox.begin.x, hitBox.begin.y - offset);
                    Coord2d newEnd = new Coord2d(hitBox.end.x, hitBox.end.y + offset);

                    // Создаем новый хитбокс с увеличенной шириной
                    hitBox = new NHitBox(newBegin, newEnd);
                }
            } else if (NParser.isIt(placed, trellis)) {
                Coord2d newBegin = new Coord2d(hitBox.begin.x-0.5, -1.8);
                Coord2d newEnd = new Coord2d(hitBox.end.x+0.5, 1.8);

                // Создаем новый хитбокс с увеличенной шириной
                hitBox = new NHitBox(newBegin, newEnd);


            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Вызываем метод с возможной модификацией хитбокса
        return getFreePlaceMod(area, hitBox);
    }

    public static Coord2d getFreePlace(Pair<Coord2d,Coord2d> area, NHitBox hitBox) {
        Coord2d pos = null;


        ArrayList<NHitBoxD> significantGobs = new ArrayList<> ();
        NHitBoxD chekerOfArea = new NHitBoxD(area.a, area.b);

        NHitBoxD temporalGobBox = new NHitBoxD(hitBox.begin, hitBox.end, Coord2d.of(0),0);
        if(chekerOfArea.c[2].sub(chekerOfArea.c[0]).x < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).x ||
                chekerOfArea.c[2].sub(chekerOfArea.c[0]).y < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).y )
            return null;

        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                    if(gob.ngob.hitBox != null && gob.getattr(Following.class)==null  && gob.id!= NUtils.player().id){
                        NHitBoxD gobBox = new NHitBoxD(gob);
                        if (gobBox.intersects(chekerOfArea,true))
                            significantGobs.add(gobBox);
                }
            }
        }

        Coord inchMax = area.b.sub(area.a).floor();
        Coord margin =  hitBox.end.sub(hitBox.begin).floor(2,2);
        for (int i = margin.x; i <= inchMax.x - margin.x; i++)
        {
            for (int j = margin.y; j <= inchMax.y - margin.y; j++)
            {
                boolean passed = true;
                NHitBoxD testGobBox = new NHitBoxD(hitBox.begin, hitBox.end, area.a.add(i,j),0);
                for ( NHitBoxD significantHitbox : significantGobs )
                    if(significantHitbox.intersects(testGobBox,false))
                        passed = false;
                if(passed)
                    return Coord2d.of(testGobBox.rc.x, testGobBox.rc.y);
            }
        }
        return pos;
    }
    public static Coord2d getFreePlaceMod(Pair<Coord2d, Coord2d> area, NHitBox hitBox) {
        Coord2d pos = null;

        ArrayList<NHitBoxD> significantGobs = new ArrayList<>();
        NHitBoxD chekerOfArea = new NHitBoxD(area.a, area.b);

        NHitBoxD temporalGobBox = new NHitBoxD(hitBox.begin, hitBox.end, Coord2d.of(0), 0);
        if (chekerOfArea.c[2].sub(chekerOfArea.c[0]).x < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).x ||
                chekerOfArea.c[2].sub(chekerOfArea.c[0]).y < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).y)
            return null;

        // Список имен объектов, для которых нужно изменять хитбокс
        NAlias kritter = new NAlias("kritter");
        NAlias trellis = new NAlias("trellis");

        // Проверяем объекты в зоне и увеличиваем их хитбоксы, если они соответствуют целевым
        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    if (gob.ngob.hitBox != null && gob.getattr(Following.class) == null && gob.id != NUtils.player().id) {
                        NHitBox hitBoxOfGob = gob.ngob.hitBox;

                        // Если объект соответствует целевому имени и ширина его хитбокса меньше 6, увеличиваем его хитбокс
                        try {
                            if (NParser.isIt(gob, kritter)) {
                                double width = hitBoxOfGob.end.y - hitBoxOfGob.begin.y; // ширина хитбокса

                                // Если ширина хитбокса меньше 6, увеличиваем ширину до 6 тайлов
                                if (width < 6) {
                                    double offset = (6 - width);
                                    Coord2d newBegin = new Coord2d(hitBoxOfGob.begin.x, hitBoxOfGob.begin.y - offset);
                                    Coord2d newEnd = new Coord2d(hitBoxOfGob.end.x, hitBoxOfGob.end.y + offset);

                                    // Создаем новый хитбокс с увеличенной шириной
                                    hitBoxOfGob = new NHitBox(newBegin, newEnd);
                                }
                            } else if (NParser.isIt(gob, trellis)) {
                                Coord2d newBegin = new Coord2d(-5.5, -1.8);
                                Coord2d newEnd = new Coord2d(5.5, 1.8);

                                // Создаем новый хитбокс с увеличенной шириной
                                hitBoxOfGob = new NHitBox(newBegin, newEnd);


                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        NHitBoxD gobBox = new NHitBoxD(hitBoxOfGob.begin, hitBoxOfGob.end, gob.rc, 0);
                        if (gobBox.intersects(chekerOfArea, true))
                            significantGobs.add(gobBox);
                    }
                }
            }
        }

        Coord inchMax = area.b.sub(area.a).floor();
        Coord margin = hitBox.end.sub(hitBox.begin).floor(2, 2);
        for (int i = margin.x; i <= inchMax.x - margin.x; i++) {
            for (int j = margin.y; j <= inchMax.y - margin.y; j++) {
                boolean passed = true;
                NHitBoxD testGobBox = new NHitBoxD(hitBox.begin, hitBox.end, area.a.add(i, j), 0);
                for (NHitBoxD significantHitbox : significantGobs)
                    if (significantHitbox.intersects(testGobBox, false))
                        passed = false;
                if (passed)
                    return Coord2d.of(testGobBox.rc.x, testGobBox.rc.y);
            }
        }
        return pos;
    }


    public static ArrayList<Gob> findGobByPatterns(ArrayList<Pattern> qaPatterns, double dist) {
        ArrayList<Gob> result = new ArrayList<>();
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if(gob.ngob!=null && gob.ngob.name!=null) {
                        for(Pattern pattern : qaPatterns) {
                            if(pattern.matcher(gob.ngob.name).matches()) {
                                double new_dist;
                                if (gob.id != NUtils.playerID() && (new_dist = gob.rc.dist(NUtils.player().rc)) < dist) {
                                    if(!(Boolean)NConfig.get(NConfig.Key.q_visitor) || (!(NParser.checkName(gob.ngob.name, new NAlias("palisadebiggate","palisadegate"))) || gob.findol(Equed.class)==null)) {
                                        result.add(gob);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Gob> findGobs(NAlias alias) {
        ArrayList<Gob> result = new ArrayList<>();
        synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                    if (gob.ngob != null && gob.ngob.name != null && NParser.checkName(gob.ngob.name, alias))
                    {
                        result.add(gob);
                    }
                }
            }
        }
        return result;
    }
}
