package nurgling.widgets;

import haven.*;
import nurgling.NConfig;
import nurgling.NMapView;
import nurgling.NUtils;
import nurgling.areas.NArea;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class NEditFolderWindow extends Window {
    private final NArea area;
    private TextEntry folderNameEntry;
    private ArrayList<String> folders;
    private Scrollport scrollport;
    private String selectedFolder;

    public NEditFolderWindow(NArea area) {
        super(UI.scale(new Coord(300, 200)), "Edit Folder");
        this.area = area;

        // Метка и текстовое поле для ввода имени новой папки
        add(new Label("Choose or create a folder:"), UI.scale(new Coord(10, 10)));

        // Поле для ввода имени новой папки
        folderNameEntry = add(new TextEntry(UI.scale(260), ""), new Coord(10, 40));
        folderNameEntry.settip("Enter new folder name");

        // Создаем область прокрутки для списка существующих папок
        scrollport = add(new Scrollport(new Coord(260, 80)), new Coord(10, 70));
        loadExistingFolders();

        // Кнопка для сохранения
        add(new Button(UI.scale(60), "Save") {
            @Override
            public void click() {
                changeFolder();
                NEditFolderWindow.this.hide();
            }
        }, new Coord(10, 160));
    }

    // Показать окно редактирования папки
    public static void show(NArea area) {
        NEditFolderWindow window = new NEditFolderWindow(area);
        NUtils.getGameUI().add(window, new Coord(300, 200));
        window.show();
    }

    // Загрузка списка существующих папок
    private void loadExistingFolders() {
        folders = new ArrayList<>();
        for (NArea area : ((NMapView) NUtils.getGameUI().map).glob.map.areas.values()) {
            String folder = area.path;
            if (!folders.contains(folder)) {
                folders.add(folder);
            }
        }
        updateFolderList();
    }

    // Обновление списка папок с помощью кнопок в Scrollport
    private void updateFolderList() {
        int y = 0;
        for (String folder : folders) {
            Button folderButton = new Button(UI.scale(240), folder) {
                @Override
                public void click() {
                    selectFolder(folder);
                }
            };
            scrollport.add(folderButton, new Coord(0, y));
            y += folderButton.sz.y + 2;
        }
    }

    // Выбор папки из списка
    private void selectFolder(String folder) {
        selectedFolder = folder;
        folderNameEntry.settext(folder);
    }

    // Изменение папки для выбранной зоны
    private void changeFolder() {
        String newFolderName = folderNameEntry.text().isEmpty() ? selectedFolder : folderNameEntry.text();
        if (newFolderName != null && !newFolderName.isEmpty()) {
            area.path = newFolderName;
            area.lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
            NConfig.needAreasUpdate();
            NUtils.getGameUI().areas.show();
            NUtils.getGameUI().msg("Area moved to folder: " + newFolderName);
        }
    }
    @Override
    public void wdgmsg(String msg, Object... args) {
        if (msg.equals("close")) {
            hide(); // Скрываем окно
        }
        super.wdgmsg(msg, args);
    }
}
