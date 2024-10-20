package nurgling.widgets;

import haven.*;
import nurgling.*;
import nurgling.tasks.GetItem;
import nurgling.tasks.WaitItemSpr;
import nurgling.tools.NAlias;
import nurgling.tools.NParser;

import java.awt.*;
import java.util.*;
import java.util.List;

public class NEquipory extends Equipory
{
    public NEquipory(long gobid)
    {
        super(gobid);
    }

    private Text percExpText;
    private int perceptionValue = 0;
    private int explorationValue = 0;
    public enum Slots {
        HEAD(0),       //00: Headgear
        ACCESSORY(1),  //01: Main Accessory
        SHIRT(2),      //02: Shirt
        ARMOR_BODY(3), //03: Torso Armor
        GLOVES(4),     //04: Gloves
        BELT(5),       //05: Belt
        HAND_LEFT(6),  //06: Left Hand
        HAND_RIGHT(7), //07: Right Hand
        RING_LEFT(8),  //08: Left Hand Ring
        RING_RIGHT(9), //09: Right Hand Ring
        ROBE(10),      //10: Cloaks & Robes
        BACK(11),      //11: Backpack
        PANTS(12),     //12: Pants
        ARMOR_LEG(13), //13: Armor
        CAPE(14),      //14: Cape
        BOOTS(15),     //15: Shoes
        STORE_HAT(16), //16: Hat from store
        EYES(17),      //17: Eyes
        MOUTH(18);     //18: Mouth

        public final int idx;
        Slots(int idx) {
            this.idx = idx;
        }
    }

    public WItem[] quickslots = new NWItem[ecoords.length];
    private void updatePercExpText() {
        // Получаем значение Perception и Exploration из окна BAttrWnd и SAttrWnd
        GameUI gui = getparent(GameUI.class);
        if (gui != null && gui.chrwdg != null) {
            // Получаем Perception
            if (gui.chrwdg.battr != null) {
                for (BAttrWnd.Attr attr : gui.chrwdg.battr.attrs) {
                    if (attr.nm.equals("prc")) {
                        perceptionValue = attr.attr.comp;
                        break;
                    }
                }
            }

            // Получаем Exploration
            if (gui.chrwdg.sattr != null) {
                for (SAttrWnd.SAttr attr : gui.chrwdg.sattr.attrs) {
                    if (attr.nm.equals("explore")) {
                        explorationValue = attr.attr.comp;
                        break;
                    }
                }
            }

            // Обновляем текст Perc*Exp
            int percExp = perceptionValue * explorationValue;
            percExpText = Text.render("Perc*Exp: " + percExp, Color.WHITE);
        }
    }
    public int[] getTotalArmor() {
        int hardArmor = 0;
        int softArmor = 0;

        // Проход по слотам экипировки
        for (Slots slot : Slots.values()) {
            WItem item = quickslots[slot.idx];  // Получаем предмет в слоте
            if (item != null) {
                NGItem gitem = (NGItem) item.item;  // Используем наш класс NGItem
                hardArmor += gitem.hardArmor;  // Суммируем жёсткую броню
                softArmor += gitem.softArmor;  // Суммируем мягкую броню
            }
        }

        return new int[] {hardArmor, softArmor};
    }
    @Override
    public void addchild (
            Widget child,
            Object... args
    ) {
        if ( child instanceof NGItem ) {
            add ( child );
            NGItem g = ( NGItem ) child;
            WItem[] v = new NWItem[args.length];
            for ( int i = 0 ; i < args.length ; i++ ) {
                int ep = ( Integer ) args[i];
                v[i] = quickslots[ep] = add ( new NWItem(g), ecoords[ep].add ( 1, 1 ) );
            }
            wmap.put ( g, Arrays.asList ( v.clone () ) );
        }
        else {
            super.addchild ( child, args );
        }
    }

    @Override
    public void cdestroy ( Widget w ) {
        if ( w instanceof GItem ) {
            GItem i = ( GItem ) w;
            for ( WItem v : wmap.remove ( i ) ) {
                ui.destroy ( v );
                for ( int qsi = 0 ; qsi < ecoords.length ; qsi++ ) {
                    if ( quickslots[qsi] == v ) {
                        /// Снимаемый предмет удаляется из массива
                        quickslots[qsi] = null;
                        break;
                    }
                }
            }
        }
    }

    public WItem findItem(int id) throws InterruptedException {
        if (quickslots[id] != null) {
            NUtils.getUI().core.addTask(new WaitItemSpr(quickslots[id]));
            return quickslots[id];
        }
        return null;
    }

    public WItem findBucket (String content) throws InterruptedException {
        if (quickslots[Slots.HAND_RIGHT.idx] != null) {
            NUtils.getUI().core.addTask(new WaitItemSpr(quickslots[Slots.HAND_RIGHT.idx]));
            if (NParser.checkName("Bucket", ((NGItem) quickslots[Slots.HAND_RIGHT.idx].item).name())) {
                if (((NGItem) quickslots[Slots.HAND_RIGHT.idx].item).content() == null || NParser.checkName(((NGItem) quickslots[Slots.HAND_RIGHT.idx].item).content().name(), content))
                    return quickslots[Slots.HAND_RIGHT.idx];
            }
        }
        if (quickslots[Slots.HAND_LEFT.idx] != null) {
            if (NParser.checkName("Bucket", ((NGItem) quickslots[Slots.HAND_LEFT.idx].item).name())) {
                if (((NGItem) quickslots[Slots.HAND_LEFT.idx].item).content() == null || NParser.checkName(((NGItem) quickslots[Slots.HAND_LEFT.idx].item).content().name(), content))
                    return quickslots[Slots.HAND_LEFT.idx];
            }
        }
        return null;
    }
    @Override
    public void draw(GOut g) {
        // Вызов базовой отрисовки (элементы из Equipory)
        super.draw(g);
        // Обновляем текст Perc*Exp при каждой отрисовке
            updatePercExpText();

        // Получаем итоговые значения брони (жесткой и мягкой)
        int[] totalArmor = getTotalArmor();
        int hardArmor = totalArmor[0];
        int softArmor = totalArmor[1];
        // Рисуем текст с Perc*Exp внизу окна NEquipory
        if (percExpText != null) {
            Coord textCoord = new Coord(sz.x - percExpText.sz().x - 50, sz.y - percExpText.sz().y -5);
            g.image(percExpText.tex(), textCoord);
        }

        // Рисуем итоговые значения брони чуть выше текста Perc*Exp
        g.atext(String.format("Armor Class: %d/%d", hardArmor, softArmor), new Coord(sz.x - 50, sz.y - 30), 1, 0);
    }
}
