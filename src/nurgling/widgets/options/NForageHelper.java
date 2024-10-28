package nurgling.widgets.options;

import haven.*;
import java.awt.Color;
import java.util.List;

public class NForageHelper extends Window {
    private final Coord valc;
    private long prc = 0L, explore = 0L;
    private long perexp = 0L;

    public NForageHelper() {
        super(Coord.z, "Forage Helper");

        final Label lbl = new Label("Your Per * Exp: ");
        add(lbl, new Coord(450 / 2, 0).sub(lbl.sz.x / 2, 0));
        valc = lbl.c.add(lbl.sz.x, 0);

        final Tex name = Text.std.render("Name").tex();
        final Tex min = Text.std.render("Min").tex();
        final Tex seeall = Text.std.render("See All").tex();
        final Tex spring = Text.std.render("Spring").tex();
        final Tex summer = Text.std.render("Summer").tex();
        final Tex autumn = Text.std.render("Autumn").tex();
        final Tex winter = Text.std.render("Winter").tex();

        add(new Widget(new Coord(470, 30)) {
            @Override
            public void draw(GOut g) {
                g.chcolor(Color.BLACK); // Устанавливаем черный цвет для фона
                g.frect(Coord.z, sz);   // Рисуем прямоугольник для фона
                g.chcolor(Color.WHITE); // Устанавливаем белый цвет для текста

                g.aimage(name, new Coord(30, 15), 0, 0.5f);
                g.aimage(min, new Coord(150, 15), 0, 0.5f);
                g.aimage(seeall, new Coord(220, 15), 0, 0.5f);
                g.aimage(spring, new Coord(300, 15), 0, 0.5f);
                g.aimage(summer, new Coord(370, 15), 0, 0.5f);
                g.aimage(autumn, new Coord(440, 15), 0, 0.5f);
                g.aimage(winter, new Coord(510, 15), 0, 0.5f);

                g.chcolor(); // Сбрасываем цвет
            }
        }, new Coord(0, lbl.sz.y + 5));

        final List<ForagableData> forageables = ForageData.loadForageData();

        final Listbox<ForagableData> foragebox = new Listbox<ForagableData>(480, 20, 30) {
            @Override
            protected ForagableData listitem(int i) {
                return forageables.get(i);
            }

            @Override
            protected int listitems() {
                return forageables.size();
            }

            @Override
            protected void drawitem(GOut g, ForagableData item, int i) {
                // Чередование цвета фона строк
                g.chcolor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                g.frect(Coord.z, sz);
                g.chcolor(Color.BLACK); // Установка цвета текста в черный

                // Рисование текста с черным цветом
                g.atext(item.name, new Coord(35, 15), 0, 0.5);
                g.atext(String.valueOf(item.min_val), new Coord(150, 15), 0, 0.5);
                g.atext(String.valueOf(item.max_val), new Coord(220, 15), 0, 0.5);
                g.atext(item.spring ? "Yes" : "No", new Coord(300, 15), 0, 0.5);
                g.atext(item.summer ? "Yes" : "No", new Coord(370, 15), 0, 0.5);
                g.atext(item.autumn ? "Yes" : "No", new Coord(440, 15), 0, 0.5);
                g.atext(item.winter ? "Yes" : "No", new Coord(510, 15), 0, 0.5);

                g.chcolor(); // Сброс цвета для других элементов
            }

            @Override
            public void change(ForagableData item) {
                // Действие при выборе элемента, если нужно
            }
        };

        add(foragebox, new Coord(0, lbl.sz.y + 30 + 5));
        pack();
    }

    @Override
    public void cdraw(GOut g) {
        super.cdraw(g);
        g.atext(String.valueOf(perexp), valc, 0, 0.5);
    }

    @Override
    protected void added() {
        super.added();
        update();
    }

    private void update() {
        perexp = prc * explore;
    }

    public static class ForagableData {
        String name;
        String inv_res;
        int min_val;
        int max_val;
        String location;
        boolean spring;
        boolean summer;
        boolean autumn;
        boolean winter;

        public ForagableData(String name, String inv_res, int min_val, int max_val, String location,
                             boolean spring, boolean summer, boolean autumn, boolean winter) {
            this.name = name;
            this.inv_res = inv_res;
            this.min_val = min_val;
            this.max_val = max_val;
            this.location = location;
            this.spring = spring;
            this.summer = summer;
            this.autumn = autumn;
            this.winter = winter;
        }
    }
}
