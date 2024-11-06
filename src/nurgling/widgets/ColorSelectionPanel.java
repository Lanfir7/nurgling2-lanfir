package nurgling.widgets;

import haven.*;
import haven.Button;
import haven.Label;
import haven.Window;

import java.awt.*;
import java.util.function.Consumer;

public class ColorSelectionPanel extends Window {
    private final TextEntry rEntry, gEntry, bEntry, aEntry;
    private final Label previewLabel;
    private final Consumer<Color> onSave;

    public ColorSelectionPanel(UI ui, Color initialColor, Consumer<Color> onSave) {
        super(new Coord(300, 200), "Выбор цвета", true); // Размер окна и заголовок
        this.onSave = onSave;

        // Заголовок
        add(new Label("Настройка цвета"), Coord.z);

        // Поле для ввода красного компонента
        add(new Label("Красный:"), new Coord(0, 30));
        rEntry = add(new TextEntry(50, Integer.toString(initialColor.getRed())), new Coord(60, 30));

        // Поле для ввода зеленого компонента
        add(new Label("Зеленый:"), new Coord(0, 60));
        gEntry = add(new TextEntry(50, Integer.toString(initialColor.getGreen())), new Coord(60, 60));

        // Поле для ввода синего компонента
        add(new Label("Синий:"), new Coord(0, 90));
        bEntry = add(new TextEntry(50, Integer.toString(initialColor.getBlue())), new Coord(60, 90));

        // Поле для ввода альфа-компонента (прозрачности)
        add(new Label("Прозрачность:"), new Coord(0, 120));
        aEntry = add(new TextEntry(50, Integer.toString(initialColor.getAlpha())), new Coord(60, 120));

        // Пример выбранного цвета
        previewLabel = add(new Label("Пример цвета"), new Coord(0, 150));
        updatePreview();

        // Кнопка для применения и сохранения цвета
        add(new Button(100, "Сохранить") {
            @Override
            public void click() {
                Color selectedColor = getSelectedColor();
                if (selectedColor != null) {
                    onSave.accept(selectedColor);
                    ui.destroy(ColorSelectionPanel.this);
                }
            }
        }, new Coord(0, 180));

        pack();
    }

    private void updatePreview() {
        Color selectedColor = getSelectedColor();
        if (selectedColor != null) {
            previewLabel.setcolor(selectedColor);
        }
    }

    private Color getSelectedColor() {
        try {
            int r = Integer.parseInt(rEntry.text());
            int g = Integer.parseInt(gEntry.text());
            int b = Integer.parseInt(bEntry.text());
            int a = Integer.parseInt(aEntry.text());

            // Ограничение значений компонентов от 0 до 255
            r = Math.max(0, Math.min(255, r));
            g = Math.max(0, Math.min(255, g));
            b = Math.max(0, Math.min(255, b));
            a = Math.max(0, Math.min(255, a));

            return new Color(r, g, b, a);
        } catch (NumberFormatException e) {
            // Если не удалось преобразовать ввод в целое число, возвращаем null
            return null;
        }
    }
}
