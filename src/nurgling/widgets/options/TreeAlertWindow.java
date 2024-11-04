package nurgling.widgets.options;

import haven.*;

public class TreeAlertWindow extends Window {
    public TreeAlertWindow(String treeName, float quality) {
        super(Coord.z, "Дерево обнаружено");

        // Добавляем отступы для элементов, чтобы текст не накладывался
        Widget prev = add(new Label("Обнаружено дерево: " + treeName), Coord.z);
        prev = add(new Label("Качество: " + quality), prev.pos("bl").adds(0, 5));

        // Кнопка закрытия с отступом
        Button closeButton = new Button(100, "Закрыть") {
            @Override
            public void click() {
                TreeAlertWindow.this.reqdestroy();
            }
        };
        add(closeButton, prev.pos("bl").adds(0, 10));
        pack();
    }

    public void wdgmsg(Widget sender, String msg, Object... args) {
        if (sender == this && msg.equals("close")) {
            reqdestroy();
        } else {
            super.wdgmsg(sender, msg, args);
        }
    }
}
