package nurgling.widgets;

import haven.*;
import nurgling.*;
import nurgling.areas.*;
import nurgling.overlays.NAreaLabel;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class NEditAreaName extends Window
{
    private final TextEntry te;

    public NEditAreaName()
    {
        super(UI.scale(new Coord(260, 50)), "Edit name"); // Увеличиваем высоту окна для новой кнопки
        prev = add(te = new TextEntry(UI.scale(200), ""));

        add(new Button(UI.scale(60), "Save")
        {
            @Override
            public void click()
            {
                super.click();
                saveName(te.text());
            }
        }, prev.pos("ur").adds(5, -6));

//        // Добавляем кнопку для автоматического задания имени
//        add(new Button(UI.scale(120), "Auto-Name") {
//            @Override
//            public void click() {
//                autoRename();
//            }
//        }, prev.pos("bl").adds(0, 10)); // Размещаем кнопку ниже поля ввода
    }

    private void saveName(String newName) {
        NEditAreaName.this.hide();
        if(!newName.isEmpty())
        {
            ((NMapView) NUtils.getGameUI().map).changeAreaName(area.id, newName);
            item.text.settext(newName);
            area.lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
            NConfig.needAreasUpdate();
            Gob dummy = ((NMapView) NUtils.getGameUI().map).dummys.get(area.gid);
            if(dummy != null) {
                NAreaLabel tl = (NAreaLabel) dummy.findol(NAreaLabel.class).spr;
                tl.update();
            }
        }
    }

    // Метод для автоматического изменения имени зоны
    private void autoRename() {
        if (area != null) {
            // Получаем первый элемент из списков jin или jout
            String newName = area.jin.length() > 0 ? ((JSONObject) area.jin.get(0)).getString("name") :
                    area.jout.length() > 0 ? ((JSONObject) area.jout.get(0)).getString("name") : "Unnamed Area";

            // Устанавливаем новое имя и сохраняем его
            te.settext(newName);
            saveName(newName);
        } else {
            NUtils.getGameUI().msg("Зона не выбрана.");
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

    public static void changeName(NArea area, NAreasWidget.AreaItem item)
    {
        NUtils.getGameUI().nean.show();
        NUtils.getGameUI().nean.raise();
        NUtils.getGameUI().nean.area = area;
        NUtils.getGameUI().nean.item = item;
        NUtils.getGameUI().nean.te.settext(area.name);
    }

    public NArea area;
    NAreasWidget.AreaItem item;
}
