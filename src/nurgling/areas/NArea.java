package nurgling.areas;

import haven.*;
import static haven.MCache.cmaps;
import haven.render.sl.*;
import nurgling.*;
import nurgling.tools.*;
import nurgling.widgets.Specialisation;
import org.json.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class NArea
{
    public long gid = Long.MIN_VALUE;
    public String path = "";
    public static class Specialisation
    {
        public String name;
        public String subtype = null;

        public Specialisation(String name, String subtype) {
            this.name = name;
            this.subtype = subtype;
        }

        public Specialisation(String name) {
            this.name = name;
        }
    }

    public static NArea findIn(String name)
    {
        double dist = 10000;
        NArea res = null;
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids)
            {
                if(id>0) {
                    if (NUtils.getGameUI().map.glob.map.areas.get(id).containIn(name)) {
                        NArea test = NUtils.getGameUI().map.glob.map.areas.get(id);
                        Pair<Coord2d, Coord2d> testrc = test.getRCArea();
                        if(test.getRCArea()!=null) {
                            double testdist;
                            if ((testdist = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc))) < dist) {
                                res = test;
                                dist = testdist;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public static ArrayList<NArea> findAllIn(NAlias name)
    {
        ArrayList<NArea> results = new ArrayList<>();
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids)
            {
                if(id>0) {
                    if (NUtils.getGameUI().map.glob.map.areas.get(id).containIn(name)) {
                        NArea test = NUtils.getGameUI().map.glob.map.areas.get(id);
                        if(test.getRCArea()!=null) {
                            results.add(test);
                        }
                    }
                }
            }
        }
        return results;
    }

    private static class TestedArea
    {
        NArea area;
        double th;

        public TestedArea(NArea area, double th) {
            this.area = area;
            this.th = th;
        }
    }

    static Comparator<TestedArea> ta_comp = new Comparator<TestedArea>(){
        @Override
        public int compare(TestedArea o1, TestedArea o2)
        {
            return Double.compare(o1.th, o2.th);
        }
    };

    public static NArea findOut(NAlias name, double th) {
        double dist = 10000;
        NArea res = null;

        ArrayList<TestedArea> areas = new ArrayList<>();
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids) {
                if (id > 0) {
                    NArea cand = NUtils.getGameUI().map.glob.map.areas.get(id);
                    if (cand.isVisible() && cand.containOut(name, th)) {
                        areas.add(new TestedArea(cand, th));
                    }
                }
            }
        }

        areas.sort(ta_comp);

        double tth = 1;
        for (TestedArea area : areas)
        {
            if(area.th<=th) {
                res = area.area;
                tth = area.th;
            }
        }

        ArrayList<NArea> targets = new ArrayList<>();
        for(TestedArea area :areas)
        {
            if(area.th ==tth)
                targets.add(area.area);
        }

        if(targets.size()>1) {
            for (NArea test: targets) {
                Pair<Coord2d, Coord2d> testrc = test.getRCArea();
                double testdist;
                if ((testdist = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc))) < dist) {
                    res = test;
                    dist = testdist;
                }
            }
        }
        return res;
    }


    public static NArea findOut(String name, double th) {
        double dist = 10000;
        NArea res = null;

        ArrayList<TestedArea> areas = new ArrayList<>();
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids) {
                if (id > 0) {
                    NArea cand = NUtils.getGameUI().map.glob.map.areas.get(id);
                    if (cand.isVisible() && cand.containOut(name, th)) {
                        areas.add(new TestedArea(cand, th));
                    }
                }
            }
        }

        areas.sort(ta_comp);

        double tth = 1;
        for (TestedArea area : areas)
        {
            if(area.th<=th) {
                res = area.area;
                tth = area.th;
            }
        }

        ArrayList<NArea> targets = new ArrayList<>();
        for(TestedArea area :areas)
        {
            if(area.th ==tth)
                targets.add(area.area);
        }

        if(targets.size()>1) {
            for (NArea test: targets) {
                Pair<Coord2d, Coord2d> testrc = test.getRCArea();
                double testdist;
                if ((testdist = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc))) < dist) {
                    res = test;
                    dist = testdist;
                }
            }
        }
        return res;
    }

    public static TreeMap<Integer,NArea> findOuts(NAlias name)
    {
        TreeMap<Integer,NArea> areas = new TreeMap<>();
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids) {
                if (id > 0)
                    if (NUtils.getGameUI().map.glob.map.areas.get(id).containOut(name) ) {
                        NArea cand = NUtils.getGameUI().map.glob.map.areas.get(id);
                        if(cand.getRCArea()!=null) {
                            for (int i = 0; i < cand.jout.length(); i++) {
                                if (NParser.checkName((String) ((JSONObject) cand.jout.get(i)).get("name"), name)) {
                                    Integer th = (((JSONObject) cand.jout.get(i)).has("th")) ? ((Integer) ((JSONObject) cand.jout.get(i)).get("th")) : 1;
                                    areas.put(th, cand);
                                }
                            }
                        }
                    }
            }
        }
        return areas;
    }

    private boolean containIn(String name)
    {
        for (int i = 0; i < jin.length(); i++)
        {
            if(((String) ((JSONObject)jin.get(i)).get("name")).equals(name))
                return true;
        }
        return false;
    }

    private boolean containIn(NAlias name)
    {
        for (int i = 0; i < jin.length(); i++)
        {
            if(NParser.checkName((String) ((JSONObject)jin.get(i)).get("name"),name))
                return true;
        }
        return false;
    }


    private boolean containOut(String name, double th)
    {
        for (int i = 0; i < jout.length(); i++) {
            if (((String) ((JSONObject) jout.get(i)).get("name")).equals(name))
                if (((JSONObject) jout.get(i)).has("th") && ((Integer) ((JSONObject) jout.get(i)).get("th")) > th)
                    return true;
                else
                    return true;
        }
        return false;
    }

    private boolean containOut(NAlias name, double th)
    {
        for (int i = 0; i < jout.length(); i++) {
            if (NParser.checkName((String) ((JSONObject) jout.get(i)).get("name"),name))
                if (((JSONObject) jout.get(i)).has("th") && ((Integer) ((JSONObject) jout.get(i)).get("th")) > th)
                    return true;
                else
                    return true;
        }
        return false;
    }

    private boolean containOut(String name)
    {
        for (int i = 0; i < jout.length(); i++) {
            if (((String) ((JSONObject) jout.get(i)).get("name")).equals(name))
                    return true;
        }
        return false;
    }

    private boolean containOut(NAlias name)
    {
        for (int i = 0; i < jout.length(); i++) {
            if (NParser.checkName((String) ((JSONObject) jout.get(i)).get("name"),name))
                return true;
        }
        return false;
    }

    public static NArea findSpec(NArea.Specialisation spec)
    {
        if(spec.subtype==null)
            return findSpec(spec.name);
        else
            return findSpec(spec.name, spec.subtype);
    }

    public static NArea findSpec(String name)
    {
        double dist = 10000;
        NArea res = null;
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids)
            {
                if(id>=0) {
                    for (NArea.Specialisation s : NUtils.getGameUI().map.glob.map.areas.get(id).spec) {
                        if (s.name.equals(name)) {
                            NArea test = NUtils.getGameUI().map.glob.map.areas.get(id);
                            if(test.isVisible()) {
                                Pair<Coord2d, Coord2d> testrc = test.getRCArea();
                                if(testrc != null) {
                                    double testdist;
                                    if ((testdist = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc))) < dist) {
                                        res = test;
                                        dist = testdist;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public static NArea findSpec(String name, String sub)
    {
        double dist = 10000;
        NArea res = null;
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null)
        {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for(Integer id : nids)
            {
                if(id>=0) {
                    for (NArea.Specialisation s : NUtils.getGameUI().map.glob.map.areas.get(id).spec) {
                        if (s.name.equals(name) && s.subtype != null && s.subtype.toLowerCase().equals(sub.toLowerCase())) {
                            NArea test = NUtils.getGameUI().map.glob.map.areas.get(id);
                            Pair<Coord2d,Coord2d> testrc = test.getRCArea();
                            if(testrc!=null) {
                                double testdist;
                                if ((testdist = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc))) < dist) {
                                    res = test;
                                    dist = testdist;
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
    public static ArrayList<NArea> findAllSpecs(String name, String sub) {
        ArrayList<NArea> result = new ArrayList<>();
        double searchRadius = 1000; // Радиус поиска в 1000

        if (NUtils.getGameUI() != null && NUtils.getGameUI().map != null) {
            Set<Integer> nids = NUtils.getGameUI().map.nols.keySet();
            for (Integer id : nids) {
                if (id >= 0) {
                    NArea area = NUtils.getGameUI().map.glob.map.areas.get(id);
                    for (NArea.Specialisation s : area.spec) {
                        if (s.name.equals(name) && s.subtype != null && s.subtype.toLowerCase().equals(sub.toLowerCase())) {
                            Pair<Coord2d, Coord2d> testrc = area.getRCArea();
                            if (testrc != null) {
                                // Проверяем, находится ли зона в пределах радиуса поиска
                                double distance = (testrc.a.dist(NUtils.player().rc) + testrc.b.dist(NUtils.player().rc)) / 2;
                                if (distance <= searchRadius) {
                                    result.add(area); // Добавляем зону в результат
                                }
                            }
                        }
                    }
                }
            }
        }
        return result; // Возвращаем список зон
    }

    public static class VArea
    {
        public Area area;
        public boolean isVis = false;

        public VArea(Area area)
        {
            this.area = area;
        }


    }

    public static class Space
    {
        private final int max = 100;
        private final int min = 0;

        public HashMap<Long,VArea> space = new HashMap<>();
        public Space()
        {}

        public Space(Coord sc, Coord ec)
        {
            Coord begin = new Coord(Math.min(sc.x, ec.x), Math.min(sc.y, ec.y));
            Coord end = new Coord(Math.max(sc.x, ec.x), Math.max(sc.y, ec.y));
            Coord bd = begin.div(cmaps);
            Coord ed = end.div(cmaps);
            Coord bm = begin.mod(cmaps);
            Coord em = end.mod(cmaps).add(1,1);
            if (bd.equals(ed.x,ed.y))
            {
                MCache.Grid grid = NUtils.getGameUI().map.glob.map.grids.get(bd);
                space.put(grid.id, new VArea(new Area(bm, em)));
            }
            else
            {
                if (bd.x != ed.x && bd.y != ed.y)
                {
                    MCache.Grid grid = NUtils.getGameUI().map.glob.map.grids.get(bd);
                    space.put(grid.id, new VArea(new Area(bm, new Coord(max,max))));
                    grid = NUtils.getGameUI().map.glob.map.grids.get(new Coord(bd.x, ed.y));
                    space.put(grid.id, new VArea(new Area(new Coord(bm.x, min), new Coord(max, em.y))));
                    grid = NUtils.getGameUI().map.glob.map.grids.get(new Coord(ed.x, bd.y));
                    space.put(grid.id, new VArea(new Area(new Coord(min, bm.y), new Coord(em.x, max))));
                    grid = NUtils.getGameUI().map.glob.map.grids.get(ed);
                    space.put(grid.id, new VArea(new Area(new Coord(min, min), em)));
                }
                else if (bd.x != ed.x)
                {
                    MCache.Grid grid = NUtils.getGameUI().map.glob.map.grids.get(bd);
                    space.put(grid.id, new VArea(new Area(bm, new Coord(max, em.y))));
                    grid = NUtils.getGameUI().map.glob.map.grids.get(new Coord(ed.x, bd.y));
                    space.put(grid.id, new VArea(new Area(new Coord(min, bm.y), em)));
                }
                else
                {
                    MCache.Grid grid = NUtils.getGameUI().map.glob.map.grids.get(bd);
                    space.put(grid.id, new VArea(new Area(bm, new Coord(em.x, max))));
                    grid = NUtils.getGameUI().map.glob.map.grids.get(new Coord(bd.x, ed.y));
                    space.put(grid.id, new VArea(new Area(new Coord(bm.x, min), em)));
                }
            }
        }
    }

    public NArea(String name)
    {
        this.name = name;
    }
    public void updateFromJSON(JSONObject obj) {
        try {
             // Если id передается как Integer
            if (obj.has("last_updated")) {
                this.lastUpdated = LocalDateTime.parse(obj.getString("last_updated"));
            } else {
                // Если времени обновления нет, устанавливаем текущее время
                this.lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
            }
            if (obj.has("name")) {
                this.name = obj.getString("name");
            }

            if (obj.has("uuid")) {
                this.uuid = obj.getString("uuid");
            }

            if(obj.has("path"))
            {
                this.path = obj.getString("path");
            }
            else if(obj.has("dir"))
            {
                this.path = "/" + obj.getString("dir");
            }

            if (obj.has("color")) {
                JSONObject color = obj.getJSONObject("color");
                this.color = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"), color.getInt("a"));
            }

            if (obj.has("space")) {
                JSONArray jareas = obj.getJSONArray("space");
                space.space.clear();
                grids_id.clear();

                for (int i = 0; i < jareas.length(); i++) {
                    JSONObject jarea = jareas.getJSONObject(i);
                    Long gridId = jarea.get("id") instanceof Integer ? ((Integer) jarea.get("id")).longValue() : jarea.getLong("id");
                    Coord begin = new Coord(jarea.getInt("begin_x"), jarea.getInt("begin_y"));
                    Coord end = new Coord(jarea.getInt("end_x"), jarea.getInt("end_y"));
                    space.space.put(gridId, new VArea(new Area(begin, end)));
                    grids_id.add(gridId);
                }
            }

            if (obj.has("in")) {
                jin = obj.getJSONArray("in");
            }

            if (obj.has("out")) {
                jout = obj.getJSONArray("out");
            }

            if (obj.has("spec")) {
                jspec = obj.getJSONArray("spec");
                spec.clear();
                for (int i = 0; i < jspec.length(); i++) {
                    JSONObject specObj = jspec.getJSONObject(i);
                    String name = specObj.getString("name");
                    String subtype = specObj.has("subtype") ? specObj.getString("subtype") : null;
                    spec.add(new Specialisation(name, subtype));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public NArea(JSONObject obj, int localId) {
        try {
            this.id = localId;  // Присваиваем новый локальный ID
            this.uuid = obj.getString("uuid");  // Получаем UUID зоны
            this.name = obj.getString("name");  // Получаем имя зоны
            if(obj.has("path"))
            {
                this.path = obj.getString("path");
            }
            else if(obj.has("dir"))
            {
                this.path = "/" + obj.getString("dir");
            }
            if (obj.has("last_updated")) {
                this.lastUpdated = LocalDateTime.parse(obj.getString("last_updated"));
            } else {
                // Если времени обновления нет, устанавливаем текущее время
                this.lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
            }

            if (obj.has("color")) {
                JSONObject color = obj.getJSONObject("color");
                this.color = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"), color.getInt("a"));
            }

            space = new Space();
            JSONArray jareas = obj.getJSONArray("space");
            for (int i = 0; i < jareas.length(); i++) {
                JSONObject jarea = jareas.getJSONObject(i);

                // Приведение типа id к Long
                Long areaId = jarea.get("id") instanceof Integer ? ((Integer) jarea.get("id")).longValue() : jarea.getLong("id");

                Coord begin = new Coord(jarea.getInt("begin_x"), jarea.getInt("begin_y"));
                Coord end = new Coord(jarea.getInt("end_x"), jarea.getInt("end_y"));
                space.space.put(areaId, new VArea(new Area(begin, end)));
                grids_id.add(areaId);
            }

            if (obj.has("in")) {
                jin = obj.getJSONArray("in");
            }
            if (obj.has("out")) {
                jout = obj.getJSONArray("out");
            }
            if (obj.has("spec")) {
                jspec = obj.getJSONArray("spec");
                for (int i = 0; i < jspec.length(); i++) {
                    JSONObject specObj = jspec.getJSONObject(i);
                    String specName = specObj.getString("name");
                    String subtype = specObj.has("subtype") ? specObj.getString("subtype") : null;
                    spec.add(new Specialisation(specName, subtype));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();  // Обрабатываем ошибки парсинга JSON
        }
    }

    public NArea(JSONObject obj)
    {
        this.name = (String) obj.get("name");
        this.id = (Integer) obj.get("id");
        if(obj.has("uuid")){
            this.uuid = obj.getString("uuid");
        }
        if (obj.has("last_updated")) {
            this.lastUpdated = LocalDateTime.parse(obj.getString("last_updated"));
        } else {
            // Если времени обновления нет, устанавливаем текущее время
            this.lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
        }
        if(obj.has("path"))
        {
            this.path = obj.getString("path");
        }
        else if(obj.has("dir"))
        {
            this.path = "/" + obj.getString("dir");
        }
        if(obj.has("color"))
        {
            JSONObject color = (JSONObject) obj.get("color");
            if (color != null)
            {
                this.color = new Color((Integer) color.get("r"), (Integer) color.get("g"), (Integer) color.get("b"), (Integer) color.get("a"));
            }
        }
        space = new Space();
        JSONArray jareas = (JSONArray) obj.get("space");
        for (int i = 0; i < jareas.length(); i++)
        {
            JSONObject jarea = (JSONObject) jareas.get(i);
            space.space.put((Long) jarea.get("id"), new VArea(new Area(new Coord((Integer) jarea.get("begin_x"), (Integer) jarea.get("begin_y")), new Coord((Integer) jarea.get("end_x"), (Integer) jarea.get("end_y")))));
            grids_id.add((Long)jarea.get("id"));
        }
        if(obj.has("in"))
        {
            jin = (JSONArray) obj.get("in");
        }
        if(obj.has("out"))
        {
            jout = (JSONArray) obj.get("out");
        }
        if(obj.has("spec"))
        {
            jspec = (JSONArray) obj.get("spec");
            for(int i = 0 ; i < jspec.length(); i++) {

                String name = (String) ((JSONObject) jspec.get(i)).get("name");
                if (((JSONObject) jspec.get(i)).has("subtype")) {
                    spec.add(new Specialisation(name, (String) ((JSONObject) jspec.get(i)).get("subtype")));
                }
                else
                {
                    spec.add(new Specialisation(name));
                }
            }
        }
    }
    public Space space;
    public String name;
    public int id;

    public LocalDateTime lastUpdated;
    public String uuid = null;
    public final ArrayList<Long> grids_id = new ArrayList<>();

    public ArrayList<Specialisation> spec = new ArrayList<>();
    public boolean inWork = false;
    public Color color = generateRandomColor();
    private Color generateRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        int a = 56;
        return new Color(r, g, b, a);
    }
    public Area getArea()
    {
        Coord begin = null;
        Coord end = null;
        for (Long id : space.space.keySet())
        {
            MCache.Grid grid = NUtils.getGameUI().map.glob.map.findGrid(id);
            if(grid!=null)
            {
                Area area = space.space.get(id).area;
                Coord b = area.ul.add(grid.ul);
                Coord e = area.br.add(grid.ul);
                begin = (begin != null) ? new Coord(Math.min(begin.x, b.x), Math.min(begin.y, b.y)) : b;
                end = (end != null) ? new Coord(Math.max(end.x, e.x), Math.max(end.y, e.y)) : e;
            }
        }
        return new Area(begin,end);
    }

    public Pair<Coord2d,Coord2d> getRCArea()
    {
        if(isVisible())
        {
            Coord begin = null;
            Coord end = null;

            for (Long id : space.space.keySet())
            {
                MCache.Grid grid = NUtils.getGameUI().map.glob.map.findGrid(id);
                if(grid==null)
                    return null;
                Area area = space.space.get(id).area;
                Coord b = area.ul.add(grid.ul);
                Coord e = area.br.add(grid.ul);
                begin = (begin != null) ? new Coord(Math.min(begin.x, b.x), Math.min(begin.y, b.y)) : b;
                end = (end != null) ? new Coord(Math.max(end.x, e.x), Math.max(end.y, e.y)) : e;
            }
            if (begin != null) {
                if (NUtils.player()!=null && begin.mul(MCache.tilesz).dist(NUtils.player().rc) > 1000 && end.mul(MCache.tilesz).dist(NUtils.player().rc) > 1000) {
                    return null;
                }
                return new Pair<Coord2d, Coord2d>(begin.mul(MCache.tilesz), end.sub(1, 1).mul(MCache.tilesz).add(MCache.tilesz));
            }
        }
        return null;
    }

    public void tick(double dt)
    {
        if(NUtils.getGameUI()!=null && NUtils.getGameUI().map!=null && NUtils.getGameUI().map.nols.get(id)==null && !inWork)
        {
            NUtils.getGameUI().map.addCustomOverlay(id);
        }
    }

    private boolean isVisible() {
        for (Long id : space.space.keySet()) {
            for (MCache.Grid g : NUtils.getGameUI().map.glob.map.grids.values()) {
                if (g.id == id)
                    return true;
            }
        }
        return false;
    }

    public JSONObject toJson()
    {
        JSONObject res = new JSONObject();
        res.put("name", name);
        res.put("id", id);
        res.put("uuid", uuid);
        res.put("last_updated", lastUpdated);
        res.put("path", path);
        JSONObject jcolor = new JSONObject();
        jcolor.put("r", color.getRed());
        jcolor.put("g", color.getGreen());
        jcolor.put("b", color.getBlue());
        jcolor.put("a", color.getAlpha());
        res.put("color", jcolor);
        JSONArray jspaces = new JSONArray();
        for(long id : space.space.keySet())
        {
            JSONObject jspace = new JSONObject();
            jspace.put("id", id);
            jspace.put("begin_x", space.space.get(id).area.ul.x);
            jspace.put("begin_y", space.space.get(id).area.ul.y);
            jspace.put("end_x", space.space.get(id).area.br.x);
            jspace.put("end_y", space.space.get(id).area.br.y);
            jspaces.put(jspace);
        }
        res.put("space",jspaces);
        res.put("in",jin);
        res.put("out",jout);
        JSONArray jspec = new JSONArray();
        for(Specialisation s: spec)
        {
            JSONObject obj = new JSONObject();
            obj.put("name", s.name);
            if(s.subtype!=null)
                obj.put("subtype", s.subtype);
            jspec.put(obj);
        }
        res.put("spec",jspec);
        this.jspec = jspec;
        return res;
    }
    
    public JSONArray jin = new JSONArray();
    public JSONArray jspec = new JSONArray();
    public JSONArray jout = new JSONArray();

    public static class Ingredient
    {
        public static enum Type
        {
            BARTER,
            CONTAINER,
            PILE
        }

        public Type type;

        String name;


        public int th = -1;
        public Ingredient(Type type, String name)
        {
            this.type = type;
            this.name = name;
        }

        public Ingredient(Type type, String name, int th)
        {
            this(type,name);
            this.th = th;
        }
    }

    public Ingredient getInput(String name)
    {
        for (int i = 0; i < jin.length(); i++)
        {
            JSONObject obj = (JSONObject)jin.get(i);
            if(((String)((JSONObject)jin.get(i)).get("name")).equals(name))
            {
                NArea.Ingredient.Type type = (obj.has("type")) ?
                        type = NArea.Ingredient.Type.valueOf((String) obj.get("type")) :
                        Ingredient.Type.CONTAINER;
                return new Ingredient(type,name);
            }
        }
        return null;
    }

    public Ingredient getOutput(String name) {
        for (int i = 0; i < jout.length(); i++)
        {
            JSONObject obj = (JSONObject)jout.get(i);
            if(((String)((JSONObject)jout.get(i)).get("name")).equals(name))
            {
                NArea.Ingredient.Type type = (obj.has("type")) ?
                        type = NArea.Ingredient.Type.valueOf((String) obj.get("type")) :
                        Ingredient.Type.CONTAINER;
                if(((JSONObject)jout.get(i)).has("th"))
                {
                    return new Ingredient(type,name, (Integer)((JSONObject)jout.get(i)).get("th"));
                }
                return new Ingredient(type,name);
            }
        }
        return null;
    }



    public ArrayList<Coord2d> getTiles(NAlias name){
        ArrayList<Coord2d> tiles = new ArrayList<>();
        Pair<Coord2d,Coord2d> range = getRCArea();
        Coord2d pos = new Coord2d(range.a.x,range.a.y);
        while ( pos.x < range.b.x ) {
            while ( pos.y < range.b.y ) {
                Coord pltc = ( new Coord2d ( pos.x / MCache.tilesz.x, pos.y / MCache.tilesz.y ) ).floor ();
                Resource res_beg = NUtils.getGameUI().ui.sess.glob.map.tilesetr ( NUtils.getGameUI().ui.sess.glob.map.gettile ( pltc ) );
                if ( NParser.checkName ( res_beg.name, name ) ) {
                    tiles.add(new Coord2d(pos.x, pos.y));
                }
                pos.y += MCache.tilesz.y;
            }
            pos.y = range.a.y;
            pos.x += MCache.tilesz.x;
        }
        return tiles;
    }

}
