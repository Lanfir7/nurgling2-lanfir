package nurgling.widgets.bots;

import haven.*;
import nurgling.NConfig;
import nurgling.NFlowerMenu;
import nurgling.NUtils;
import nurgling.areas.NArea;
import nurgling.tools.VSpec;
import nurgling.widgets.IngredientContainer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Consumer;

public class NCategorySelectionWindow extends Window {

    private final List<Category> categories = new ArrayList<>();
    private final Map<String, List<Element>> categoryElements = new HashMap<>();
    private CategoryList categoryList;
    private ElementList elementList;
    private final int areaId;

    public NCategorySelectionWindow(int areaId) {
        super(UI.scale(new Coord(600, 400)), "Category Selection");

        this.areaId = areaId;
        // Инициализация категорий и элементов из VSpec
        Set<String> categoryNames = VSpec.categories.keySet();
        for (String categoryName : categoryNames) {
            categories.add(new Category(categoryName));
            List<Element> elements = new ArrayList<>();
            for (JSONObject obj : VSpec.categories.get(categoryName)) {
                String name = obj.getString("name");
                String resource = obj.getString("static");
                elements.add(new Element(name, resource));
            }
            categoryElements.put(categoryName, elements);
        }

        // Создаем список категорий слева
        categoryList = add(new CategoryList(UI.scale(new Coord(200, 300))), new Coord(10, 10));
        categoryList.setItems(categories);

        // Создаем список элементов справа
        elementList = add(new ElementList(UI.scale(new Coord(350, 300))), new Coord(220, 10));

        // Обработка выбора категории
        categoryList.setOnCategorySelected(category -> {
            if (category != null) {
                List<Element> elements = categoryElements.get(category.getName());
                if (elements != null) {
                    elementList.setItems(elements);

                    // Устанавливаем обработчик клика по элементам (ресурсам) справа
                    elementList.setOnElementSelected(element -> {
                        if (element != null) { // Проверка на null
                            addElementToArea(areaId, element);
                        }
                    });
                }
            }
        });

        pack();
    }
    private void addElementToArea(int areaId, Element element) {
        if (element == null) {
            NUtils.getGameUI().msg("addElementToArea: Element is null");
            return;
        }
        NArea selectedArea = NUtils.getArea(areaId);
        if (selectedArea != null) {
            JSONObject res = new JSONObject();
            res.put("name", element.getName());
            res.put("type", NArea.Ingredient.Type.CONTAINER.toString());
            res.put("static", element.getResource());
            JSONArray data;
            data = NUtils.getArea(areaId).jout;
            data.put(res);
            NUtils.getArea(areaId).lastUpdated = LocalDateTime.now(ZoneOffset.UTC).withNano(0);
            NConfig.needAreasUpdate();
            NUtils.getGameUI().areas.out_items.load(areaId);
            NUtils.getGameUI().msg("Element " + element.getName() + " added to area " + areaId);
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

        private NFlowerMenu menu; // Добавляем меню как поле класса

        public CategoryList(Coord sz) {
            super(sz, UI.scale(15));
        }

        @Override
        protected List<Category> items() {
            return internalCategories;
        }

        @Override
        protected Widget makeitem(Category item, int idx, Coord sz) {
            return new Label(item.getName());
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
            int slot = slotat(c); // Вычисляем индекс элемента на основе координаты клика

            // Проверяем, что индекс валиден и находится в пределах списка категорий
            if (slot >= 0 && slot < internalCategories.size()) {
                Category selectedCategory = internalCategories.get(slot); // Получаем выбранную категорию
                if (button == 3) { // Проверяем, если нажата правая кнопка мыши
                    showCategoryMenu(c, selectedCategory);
                } else if (button == 1) { // Левая кнопка мыши
                    if (onCategorySelected != null) {
                        onCategorySelected.accept(selectedCategory);
                    }
                    NUtils.getGameUI().msg("Selected category: " + selectedCategory.getName());
                }
            }
            return super.mousedown(c, button); // Вызываем стандартную обработку события
        }
        // Метод для отображения меню при правом клике
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
        @Override
        public void change(Category item) {
            super.change(item);
            if (onCategorySelected != null) {
                onCategorySelected.accept(item);
            }
        }
    }

    public class ElementList extends SListBox<Element, Widget> {
        private List<Element> internalElements = new ArrayList<>();

        private Consumer<Element> onElementSelected;
        public ElementList(Coord sz) {
            super(sz, UI.scale(64));
        }

        @Override
        protected List<Element> items() {
            return internalElements;
        }

        @Override
        protected Widget makeitem(Element item, int idx, Coord sz) {
            return new ElementWidget(item, sz);
        }

        public void setItems(List<Element> newElements) {
            internalElements.clear();
            if (newElements != null) {
                internalElements.addAll(newElements);
            }
        }

        public int itemh() {
            return UI.scale(64);
        }
        public void setOnElementSelected(Consumer<Element> onElementSelected) {
            this.onElementSelected = onElementSelected;
        }
        @Override
        public void change(Element item) {
            super.change(item);
            if (onElementSelected != null) {
                onElementSelected.accept(item);
            }
        }
        @Override
        public boolean mousedown(Coord c, int button) {
            int slot = slotat(c); // Используем slotat для вычисления индекса элемента
            if (slot >= 0 && slot < internalElements.size()) {
                Element selectedElement = internalElements.get(slot);
                if (selectedElement != null) {
                    addElementToArea(areaId, selectedElement);
                }
            }
            return super.mousedown(c, button);
        }
    }

    public class ElementWidget extends Widget {
        private final Element element;
        private final Tex icon;
        private final Label label;

        public ElementWidget(Element element, Coord sz) {
            super(sz);
            this.element = element;
            Tex loadedIcon;
            try {
                Resource res = Resource.remote().loadwait((element.getResource()));
                loadedIcon = res.layer(Resource.imgc).tex();
            } catch (Exception e) {
                loadedIcon = Resource.remote().loadwait("gfx/invobjs/default_icon").layer(Resource.imgc).tex();
            }
            this.icon = loadedIcon;
            add(new Img(icon), Coord.z);
            add(label = new Label(element.getName()), new Coord(32 + 10, 5));
            pack();
        }

        @Override
        public void draw(GOut g) {
            super.draw(g);

            //g.image(icon, Coord.z, new Coord(16,32));
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
