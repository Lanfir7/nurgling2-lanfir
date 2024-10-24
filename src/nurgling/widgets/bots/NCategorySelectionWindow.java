package nurgling.widgets.bots;

import haven.*;
import haven.Label;
import haven.Window;
import nurgling.*;
import nurgling.areas.NArea;
import nurgling.tools.VSpec;
import nurgling.widgets.NAreasWidget;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class NCategorySelectionWindow extends Window {
    private TextEntry searchBox;
    private final List<Category> categories = new ArrayList<>();
    private final List<Element> allElements = new ArrayList<>();
    private final Map<String, List<Element>> categoryElements = new HashMap<>();
    private CategoryList categoryList;
    private ElementList elementList;
    private final int areaId;
    private final String type;

    public NCategorySelectionWindow(int areaId, String type) {
        super(UI.scale(new Coord(600, 400)), "Category Selection");
        searchBox = add(new TextEntry(UI.scale(195), "") {
            @Override
            public void changed() {
                updateElementList(); // Обновляем список при вводе в поисковой строке
            }
        }, new Coord(5, UI.scale(5)));

        this.areaId = areaId;
        this.type = type;
        String areaName = NUtils.getArea(areaId).name;
        Font boldFont = new Font("SansSerif", Font.BOLD, 20);

        Text.Foundry boldFoundry = new Text.Foundry(boldFont, Color.WHITE);
        Text.Foundry strokeFoundry = new Text.Foundry(boldFont, Color.BLACK);
        String headerText = String.format("Editing Zone: %s (%s)", areaName, type.equalsIgnoreCase("in") ? "Take" : "Put");

        Tex strokeTex1 = strokeFoundry.render(headerText).tex();
        Tex strokeTex2 = strokeFoundry.render(headerText).tex();
        Tex headerTex = boldFoundry.render(headerText).tex();

        add(new Img(strokeTex1), new Coord(218, 2)); // Смещаем вверх-влево
//        add(new Img(strokeTex2), new Coord(222, 4)); // Смещаем вниз-вправо
        add(new Img(headerTex), new Coord(220, 3));

        // Инициализация категорий и элементов из VSpec
        Set<String> categoryNames = VSpec.categories.keySet();
        for (String categoryName : categoryNames) {
            categories.add(new Category(categoryName));
            List<Element> elements = new ArrayList<>();
            for (JSONObject obj : VSpec.categories.get(categoryName)) {
                String name = obj.getString("name");
                String resource = obj.getString("static");
                Element element = new Element(name, resource);
                elements.add(element);
                allElements.add(element); // Добавляем все элементы в общий список
            }
            elements.sort(Comparator.comparing(Element::getName));
            categoryElements.put(categoryName, elements);
        }
        categories.sort(Comparator.comparing(Category::getName));

        // Создаем список категорий слева
        categoryList = add(new CategoryList(UI.scale(new Coord(200, 280))), new Coord(10, 30));
        categoryList.setItems(categories);

        // Создаем список элементов справа
        elementList = add(new ElementList(UI.scale(new Coord(350, 280))), new Coord(220, 30));

        categoryList.setOnCategorySelected(category -> {
            List<Element> elements = categoryElements.get(category.getName());
            elementList.setItems(elements);
        });
        pack();
    }

    // Метод для обновления списка элементов справа
    private void updateElementList() {
        String searchText = searchBox.text().toLowerCase(); // Получаем текст из поисковой строки

        if (searchText.isEmpty()) {
            // Если строка поиска пустая, отображаем элементы выбранной категории
            Category selectedCategory = categoryList.getSelected();
            if (selectedCategory != null) {
                List<Element> elements = categoryElements.get(selectedCategory.getName());
                elementList.setItems(elements);
            }
        } else {
            // Если введен текст, фильтруем элементы по поисковому запросу
            Set<Element> filteredElements = new TreeSet<>(Comparator.comparing(Element::getName)); // Используем TreeSet для сортировки и удаления дубликатов

            for (Element element : allElements) {
                if (element.getName().toLowerCase().contains(searchText)) {
                    filteredElements.add(element); // Добавляем только уникальные элементы
                }
            }

            // Преобразуем Set обратно в List для отображения в элементе UI
            elementList.setItems(new ArrayList<>(filteredElements));
        }
    }
    private void addElementToArea(int areaId, Element element, String el) {
        if (element == null) {
            NUtils.getGameUI().msg("addElementToArea: Element is null");
            return;
        }
        NArea selectedArea = NUtils.getArea(areaId);
        if (selectedArea != null) {
            JSONArray data;
            if (el.equalsIgnoreCase("in")) {
                data = NUtils.getArea(areaId).jin; // Входящие элементы
            } else {
                data = NUtils.getArea(areaId).jout; // Исходящие элементы
            }
            boolean find = false;
            for (int i = 0; i < data.length(); i++)
            {
                if (((JSONObject) data.get(i)).get("name").equals(element.getName()))
                {
                    find = true;
                    break;
                }
            }
            if(!find) {
                JSONObject res = new JSONObject();
                res.put("name", element.getName());
                res.put("type", NArea.Ingredient.Type.CONTAINER.toString());
                res.put("static", element.getResource());
                data.put(res);
                NUtils.getArea(areaId).lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
                NConfig.needAreasUpdate();
                NUtils.getGameUI().areas.out_items.load(areaId);
                NUtils.getGameUI().areas.in_items.load(areaId);
                NUtils.getGameUI().msg("Element " + element.getName() + " added to area " + areaId);
            }
        }
    }
    private void addElementToArea(int areaId, Element element) {
        if (element == null) {
            NUtils.getGameUI().msg("addElementToArea: Element is null");
            return;
        }
        NArea selectedArea = NUtils.getArea(areaId);
        if (selectedArea != null) {
            JSONArray data;
            if (type.equalsIgnoreCase("in")) {
                data = NUtils.getArea(areaId).jin; // Входящие элементы
            } else {
                data = NUtils.getArea(areaId).jout; // Исходящие элементы
            }
            boolean find = false;
            for (int i = 0; i < data.length(); i++)
            {
                if (((JSONObject) data.get(i)).get("name").equals(element.getName()))
                {
                    find = true;
                    break;
                }
            }
            if(!find) {
                JSONObject res = new JSONObject();
                res.put("name", element.getName());
                res.put("type", NArea.Ingredient.Type.CONTAINER.toString());
                res.put("static", element.getResource());
                data.put(res);
                NUtils.getArea(areaId).lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
                NConfig.needAreasUpdate();
                NUtils.getGameUI().areas.out_items.load(areaId);
                NUtils.getGameUI().areas.in_items.load(areaId);
                NUtils.getGameUI().msg("Element " + element.getName() + " added to area " + areaId);
            }
        }
    }

    private void addAllElementsToArea(int areaId, String categoryName) {
        List<Element> elements = categoryElements.get(categoryName);
        if (elements != null) {
            for (Element element : elements) {
                addElementToArea(areaId, element);
            }
        }
    }

    // Класс для представления категории
    public static class Category {
        private final String name;

        public Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Класс для представления элемента в категории
    public static class Element {
        private final String name;
        private final String resource;

        public Element(String name, String resource) {
            this.name = name;
            this.resource = resource;
        }

        public String getName() {
            return name;
        }

        public String getResource() {
            return resource;
        }
    }

    public class CategoryList extends SListBox<Category, Widget> {
        private final List<Category> internalCategories = new ArrayList<>();
        private Consumer<Category> onCategorySelected;
        private Category selected; // Переменная для хранения выбранной категории
        private NFlowerMenu menu;

        public CategoryList(Coord sz) {
            super(sz, UI.scale(15));
        }
        @Override
        public void change(Category item) {
            selected = item; // Сохраняем выбранную категорию
            super.change(item);
        }

        @Override
        protected List<Category> items() {
            return internalCategories;
        }

        @Override
        protected Widget makeitem(Category item, int idx, Coord sz) {
            return new ItemWidget<Category>(this, sz, item) {
                {
                    add(new Label(item.getName()));
                }

                @Override
                public void draw(GOut g) {
                    // Проверяем, является ли элемент выбранным, и если да, изменяем его цвет
                    if (selected == item) {
                        g.chcolor(200, 200, 255, 255); // Подсвечиваем синим фоном
                    } else {
                        g.chcolor();
                    }
                    super.draw(g);
                }

                public boolean mousedown(Coord c, int button) {
                    boolean psel = sel == item;
                    super.mousedown(c, button);
                    if (!psel) {
                        String value = item.getName();
                    }
                    return true;
                }
            };
        }

        public Category getSelected() {
            return selected;
        }

        public void setItems(List<Category> newCategories) {
            internalCategories.clear();
            internalCategories.addAll(newCategories);
        }

        public void setOnCategorySelected(Consumer<Category> onCategorySelected) {
            this.onCategorySelected = onCategorySelected;
        }

        public int itemh() {
            return UI.scale(20);
        }

        @Override
        public boolean mousedown(Coord c, int button) {
            int slot = slotat(c);

            if (slot >= 0 && slot < internalCategories.size()) {
                Category selectedCategory = internalCategories.get(slot);
                this.selected = selectedCategory; // Сохраняем выбранную категорию
                onCategorySelected.accept(selectedCategory);
                if (button == 3) {
                    showCategoryMenu(c, selectedCategory);
                } else if (button == 1) {
                    if (onCategorySelected != null) {
                        onCategorySelected.accept(selectedCategory);
                    }
                }
            }
            return super.mousedown(c, button);
        }

        private void showCategoryMenu(Coord c, Category selectedCategory) {
            if (menu == null) {
                menu = new NFlowerMenu(new String[]{"Добавить к зоне всю группу"}) {
                    @Override
                    public void nchoose(NPetal option) {
                        if (option != null && "Добавить к зоне всю группу".equals(option.name)) {
                            addAllElementsToArea(areaId, selectedCategory.getName());
                        }
                        uimsg("cancel");
                    }

                    @Override
                    public boolean mousedown(Coord c, int button) {
                        if (super.mousedown(c, button)) {
                            nchoose(null);
                        }
                        return true;
                    }

                    @Override
                    public void destroy() {
                        menu = null;
                        super.destroy();
                    }
                };
                ui.root.add(menu, c.add(UI.scale(25, 38)));
            }
        }
    }

    public class ElementList extends SListBox<Element, Widget> {
        private List<Element> internalElements = new ArrayList<>();
        private Element selectedElement;
        private Consumer<Element> onElementSelected;
        public ElementList(Coord sz) {
            super(sz, UI.scale(32));
        }

        @Override
        protected List<Element> items() {
            return internalElements;
        }

        @Override
        protected Widget makeitem(Element item, int idx, Coord sz) {
            return new ItemWidget<Element>(this, sz, item) {
                {
                    // Загружаем иконку элемента и добавляем к виджету
                    Tex icon = loadIcon(item);
                    int itemHeight = Math.max(icon.sz().y, 64); // Минимальная высота строки
                    Coord itemSize = new Coord(sz.x, itemHeight);
                    add(new ElementWidget(item, itemSize, icon), Coord.z);
                }

                @Override
                public void draw(GOut g) {
                    // Проверяем, является ли элемент выбранным, и если да, изменяем его цвет
                    if (selectedElement == item) {
                        g.chcolor(200, 200, 255, 255); // Подсвечиваем синим фоном
                    } else {
                        g.chcolor();
                    }
                    super.draw(g);
                }

                public boolean mousedown(Coord c, int button) {
                    boolean psel = sel == item;
                    super.mousedown(c, button);
                    if (!psel) {
                        // Здесь мы устанавливаем элемент как выбранный
                        change(item); // Обрабатываем выбор элемента
                    }
                    return true;
                }
            };
        }

        public void setItems(List<Element> newElements) {
            internalElements.clear();
            if (newElements != null) {
                internalElements.addAll(newElements);
            }
            reset(); // Обновляем список для перерисовки
        }

        public void setOnElementSelected(Consumer<Element> onElementSelected) {
            this.onElementSelected = onElementSelected;
        }

        @Override
        public void change(Element item) {
            super.change(item);
            selectedElement = item;
            if (onElementSelected != null) {
                onElementSelected.accept(item);
            }
        }

        @Override
        public boolean mousedown(Coord c, int button) {
            int slot = slotat(c);
            if (slot >= 0 && slot < internalElements.size()) {
                Element selectedElement = internalElements.get(slot);
                if (selectedElement != null) {
                    addElementToArea(areaId, selectedElement);
                }
            }
            return super.mousedown(c, button);
        }
    }

    private Tex loadIcon(Element item) {
        Tex loadedIcon;
        try {
            Resource res = Resource.remote().loadwait(item.getResource());
            loadedIcon = res.layer(Resource.imgc).tex();
        } catch (Exception e) {
            loadedIcon = Resource.remote().loadwait("gfx/invobjs/default_icon").layer(Resource.imgc).tex();
        }
        return loadedIcon;
    }

    public class ElementWidget extends Widget {
        private final Element element;
        private final Tex icon;
        private final Label label;
        private final IButton addToInputButton; // Кнопка для добавления в IN
        private final IButton addToOutputButton;

        public ElementWidget(Element element, Coord sz, Tex icon) {
            super(sz);
            this.element = element;
            this.icon = icon;
            int desiredHeight = 32;
            double scale = (double) desiredHeight / icon.sz().y;
            int scaledWidth = (int) (icon.sz().x * scale);
// Инициализация кнопки для добавления в IN
            add(label = new Label(element.getName()), new Coord(64, 16));
            addToInputButton = add(new IButton(NStyle.cloud[0].back,NStyle.cloud[1].back,NStyle.cloud[2].back){
                                       @Override
                                       public void click() {
                                           addToInput();
                                       }
            }, new Coord(64 + 210, 0));
            addToInputButton.tooltip = Text.render("Добавить в IN").tex();

            // Инициализация кнопки для добавления в OUT
            addToOutputButton = add(new IButton(NStyle.addfolder[0].back,NStyle.addfolder[1].back,NStyle.addfolder[2].back){
                @Override
                public void click() {
                    addToOutput();
                }
            }, new Coord(64 + 250, 0));
            addToOutputButton.tooltip = Text.render("Добавить в OUT").tex();
            pack();
        }
        // Метод для добавления в IN
        private void addToInput() {
            NUtils.getGameUI().msg("Adding " + element.getName() + " to IN...");
            addElementToArea(areaId, element, "in");
        }

        // Метод для добавления в OUT
        private void addToOutput() {
            NUtils.getGameUI().msg("Adding " + element.getName() + " to OUT...");
            addElementToArea(areaId, element, "out");
        }
        @Override
        public void draw(GOut g) {
            super.draw(g);
            int desiredHeight = 32;
            double scale = (double) desiredHeight / icon.sz().y;
            int scaledWidth = (int) (icon.sz().x * scale);

            g.image(icon, Coord.z, UI.scale(new Coord(scaledWidth, desiredHeight)));
        }
    }

    @Override
    public void wdgmsg(String msg, Object... args) {
        if (msg.equals("close")) {
            hide();
        }
        super.wdgmsg(msg, args);
    }
}
