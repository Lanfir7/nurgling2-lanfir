package nurgling.widgets;

import haven.*;
import haven.Label;
import haven.Window;
import nurgling.*;
import nurgling.areas.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class Specialisation extends Window
{

    private NArea area = null;

    public Specialisation()
    {
        super(UI.scale(200,500), "Specialisation");
        add(new SpecialisationList(UI.scale(200,500)));
    }
    public enum SpecName
    {
        smelter, ore, fuel,
        kiln, toFire,
        tarKiln,
        water,
        boiler,
        swill, trough, crop, seed,
        cows,sheeps,pigs,goats,
        deadkritter,
        ovens,
        gardenpot, barrel,
        leafs, htable,
        rawhides, dframe,
        ttub, tanning, readyHides,
        logs, smokshed
    }

    private static ArrayList<SpecialisationItem> specialisation = new ArrayList<>();

    static {
        for (SpecName spec : SpecName.values()) {
            specialisation.add(new SpecialisationItem(spec.toString()));
        }
    }

    public class SpecialisationList extends SListBox<SpecialisationItem, Widget> {
        SpecialisationList(Coord sz) {
            super(sz, UI.scale(15));
        }

        @Override
        public void change(SpecialisationItem item)
        {
            super.change(item);
        }

        protected List<SpecialisationItem> items() {return new ArrayList<>(specialisation);}

        @Override
        public void resize(Coord sz) {
            super.resize(new Coord(sz.x, sz.y));
        }

        protected Widget makeitem(SpecialisationItem item, int idx, Coord sz) {
            return(new ItemWidget<SpecialisationItem>(this, sz, item) {
                {
                    //item.resize(new Coord(searchF.sz.x - removei[0].sz().x  + UI.scale(4), item.sz.y));
                    add(item);
                }

                public boolean mousedown(Coord c, int button) {
                    super.mousedown(c, button);

                    String value = item.text.text();
                    boolean isFound = false;
                    for(NArea.Specialisation s: area.spec)
                    {
                        if(s.name.equals(item.text.text()))
                            isFound = true;
                    }
                    if(!isFound)
                    {
                        area.spec.add(new NArea.Specialisation(value));
                        NConfig.needAreasUpdate();
                        NUtils.getGameUI().areas.loadSpec(area.id);
                        Specialisation.this.hide();
                    }
                    else
                    {
                        NUtils.getGameUI().error("Specialisation already selected.");
                    }
                    return(true);
                }
            });
        }

        @Override
        public void wdgmsg(String msg, Object... args)
        {
            super.wdgmsg(msg, args);
        }

        Color bg = new Color(30,40,40,160);

        @Override
        public void draw(GOut g)
        {
            g.chcolor(bg);
            g.frect(Coord.z, g.sz());
            super.draw(g);
        }


    }

    @Override
    public void wdgmsg(String msg, Object... args)
    {
        if(msg.equals("close"))
        {
            hide();
        }
        else
        {
            super.wdgmsg(msg, args);
        }
    }

    public static class SpecialisationItem extends Widget
    {
        Label text;


        public SpecialisationItem(String text)
        {
            this.text = add(new Label(text));
            pack();
        }
    }

    public static void selectSpecialisation(NArea area)
    {
        NUtils.getGameUI().spec.show();
        NUtils.getGameUI().setfocus(NUtils.getGameUI().spec);
        NUtils.getGameUI().spec.raise();
        NUtils.getGameUI().spec.area = area;
    }
}
