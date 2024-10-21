package nurgling.tools;

import nurgling.*;
import org.json.*;

import java.util.*;

public class VSpec
{
    public static HashMap<String,ArrayList<JSONObject>> categories = new HashMap<>();
    static {
        ArrayList<JSONObject> spices = new ArrayList<>();
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/kvann\",\"name\":\"Kvann\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-juniper\",\"name\":\"Juniper Berries\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/chives\",\"name\":\"Chives\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-laurel\",\"name\":\"Laurel Leaves\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/salvia\",\"name\":\"Sage\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/thyme\",\"name\":\"Thyme\"}"));
        spices.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/dill\",\"name\":\"Dill\"}"));
        categories.put("Spices", spices);

        ArrayList<JSONObject> rootVegetables = new ArrayList<>();
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/beet\",\"name\":\"Beetroot\"}"));
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/carrot\",\"name\":\"Carrot\"}"));
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/cattailroots\",\"name\":\"Cattail Roots\"}"));
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/cavebulb\",\"name\":\"Cavebulb\"}"));
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/oddtuber\",\"name\":\"Odd Tuber\"}"));
        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/turnip\",\"name\":\"Turnip\"}"));
//        rootVegetables.add(new JSONObject("{\"static\":\"gfx/invobjs/weirdbeetroot\",\"name\":\"Weird Beetroot\"}"));  // Missing resource
        categories.put("Tuber", rootVegetables);

        ArrayList<JSONObject> onions = new ArrayList<>();
        onions.add(new JSONObject("{\"static\":\"gfx/invobjs/yellowonion\",\"name\":\"Yellow Onion\"}"));
        onions.add(new JSONObject("{\"static\":\"gfx/invobjs/redonion\",\"name\":\"Red Onion\"}"));
        onions.add(new JSONObject("{\"static\":\"gfx/invobjs/small/leek\",\"name\":\"Leek\",\"x\":2,\"y\":1}"));
        onions.add(new JSONObject("{\"static\":\"gfx/invobjs/pickledonion\",\"name\":\"Pickled Onion\"}"));
        categories.put("Onion", onions);


        ArrayList<JSONObject> strings = new ArrayList<>();
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/flaxfibre\",\"name\":\"Flax Fibres\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/hempfibre\",\"name\":\"Hemp Fibres\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/spindlytaproot\",\"name\":\"Spindly Taproot\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/cattailfibre\",\"name\":\"Cattail Fibres\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/stingingnettle\",\"name\":\"Stinging Nettle\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/hidestrap\",\"name\":\"Hide Strap\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/strawstring\",\"name\":\"Straw Twine\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/barkcordage\",\"name\":\"Bark Cordage\"}"));
//        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/toughroot\",\"name\":\"Tough Root\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/reedtwine\",\"name\":\"Reed Twine\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/toadflax\",\"name\":\"Toadflax\"}"));
        strings.add(new JSONObject("{\"static\":\"gfx/invobjs/trollhair\",\"name\":\"Troll Hair\"}"));
        categories.put("String", strings);

        ArrayList<JSONObject> salads = new ArrayList<>();
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/beetleaves\",\"name\":\"Beetroot Leaves\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/driftkelp\",\"name\":\"Driftkelp\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/duskfern\",\"name\":\"Dusk Fern\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/greenkelp\",\"name\":\"Green Kelp\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-heartwood\",\"name\":\"Heartwood Leaves\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/lettuceleaf\",\"name\":\"Lettuce Leaf\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/marshmallow\",\"name\":\"Marsh-Mallow\"}"));
        salads.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/stingingnettle\",\"name\":\"Stinging Nettle\"}"));
        categories.put("Salad Greens", salads);

        ArrayList<JSONObject> seeds = new ArrayList<>();
//        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/wheat-malted\",\"name\":\"Malted Wheat\"}"));  // Resource missing
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-barley\",\"name\":\"Barley Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-carrot\",\"name\":\"Carrot Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-cucumber\",\"name\":\"Cucumber Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-flax\",\"name\":\"Flax Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-grape\",\"name\":\"Grape Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-hemp\",\"name\":\"Hemp Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-leek\",\"name\":\"Leek Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-lettuce\",\"name\":\"Lettuce Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-millet\",\"name\":\"Millet Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-pipeweed\",\"name\":\"Pipeweed Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-poppy\",\"name\":\"Poppy Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-pumpkin\",\"name\":\"Pumpkin Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-turnip\",\"name\":\"Turnip Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-wheat\",\"name\":\"Wheat Seed\"}"));
        categories.put("Crop Seeds", seeds);

        ArrayList<JSONObject> eggs = new ArrayList<>();
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-bullfinch\",\"name\":\"Bullfinch Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-chicken\",\"name\":\"Chicken Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-magpie\",\"name\":\"Magpie Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-rockdove\",\"name\":\"Rock Dove Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-woodgrouse\",\"name\":\"Wood Grouse Egg\"}"));
        categories.put("Egg", eggs);

        ArrayList<JSONObject> gelatinMaterials = new ArrayList<>();
        gelatinMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/caveslime\",\"name\":\"Cave Slime\"}"));
        gelatinMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/gelatin\",\"name\":\"Gelatin\"}"));
        categories.put("Gellant", gelatinMaterials);

        ArrayList<JSONObject> stuffings = new ArrayList<>();
        stuffings.add(new JSONObject("{\"static\":\"gfx/invobjs/stuffing-meat\",\"name\":\"Meat Stuffing\"}"));
        stuffings.add(new JSONObject("{\"static\":\"gfx/invobjs/stuffing-mushroom\",\"name\":\"Mushroom Stuffing\"}"));
        stuffings.add(new JSONObject("{\"static\":\"gfx/invobjs/stuffing-roe\",\"name\":\"Roe Stuffing\"}"));
        stuffings.add(new JSONObject("{\"static\":\"gfx/invobjs/stuffing-vegetable\",\"name\":\"Vegetable Stuffing\"}"));
        categories.put("Stuffing", stuffings);

        categories.put("Raw", new ArrayList<>());

        ArrayList<JSONObject> mushrooms = new ArrayList<>();
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/baybolete\",\"name\":\"Bay Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/bloatedbolete\",\"name\":\"Bloated Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/wblock-trombonechantrelle\",\"name\":\"Block of Trombone Chantrelle\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/champignon-small\",\"name\":\"Button Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/chantrelle\",\"name\":\"Chantrelles\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/champignon-medium\",\"name\":\"Cremini Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/fieldblewit\",\"name\":\"Field Blewits\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/giantpuffball\",\"name\":\"Giant Puffball\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/libertycap\",\"name\":\"Liberty Caps\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/oystermushroom\",\"name\":\"Oyster Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/parasolshroom\",\"name\":\"Parasol Mushroom\"}"));
        //mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/parboiledmorel\",\"name\":\"Parboiled Morels\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/champignon-large\",\"name\":\"Portobello Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/rubybolete\",\"name\":\"Ruby Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/snowtop\",\"name\":\"Snowtop\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/stalagoom\",\"name\":\"Stalagoom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/trollshrooms\",\"name\":\"Troll Mushrooms\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/yellowfoot\",\"name\":\"Yellowfeet\"}"));
        categories.put("Edible Mushroom", mushrooms);

        ArrayList<JSONObject> nuts = new ArrayList<>();
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/almond\",\"name\":\"Almond\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-beech\",\"name\":\"Beech Seed\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/carobfruit\",\"name\":\"Carob Pod\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/chestnut\",\"name\":\"Chestnut\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/hazelnut\",\"name\":\"Hazelnut\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-kingsoak\",\"name\":\"King's Oak Seed\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/walnut\",\"name\":\"Walnut\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-oak\",\"name\":\"Oak Seed\"}"));
        nuts.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-witherstand\",\"name\":\"Witherstand Seed\"}"));
        categories.put("Nuts", nuts);

        ArrayList<JSONObject> cones = new ArrayList<>();
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackpine\",\"name\":\"Black Pine Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-cedar\",\"name\":\"Cedar Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-dwarfpine\",\"name\":\"Dwarf Pine Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-fir\",\"name\":\"Fir Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-larch\",\"name\":\"Larch Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-pine\",\"name\":\"Pine Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-silverfir\",\"name\":\"Silverfir Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-spruce\",\"name\":\"Spruce Cone\"}"));
        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/stonepinecone\",\"name\":\"Stone Pine Cone\"}"));
//        cones.add(new JSONObject("{\"static\":\"gfx/invobjs/unusuallylargehopcone\",\"name\":\"Unusually Large Hop Cone\"}"));  // Resource missing
        categories.put("Decent-sized Conifer Cone", cones);

        ArrayList<JSONObject> berries = new ArrayList<>();
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackberrybush\",\"name\":\"Blackberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackcurrant\",\"name\":\"Blackcurrant Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/blueberry\",\"name\":\"Blueberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/candleberry\",\"name\":\"Candleberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/cherry\",\"name\":\"Cherry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-dogrose\",\"name\":\"Dog Rose Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-elderberrybush\",\"name\":\"Elderberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-gooseberrybush\",\"name\":\"Gooseberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/lingon\",\"name\":\"Lingonberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/mulberry\",\"name\":\"Mulberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-raspberrybush\",\"name\":\"Raspberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-redcurrant\",\"name\":\"Redcurrant Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sandthorn\",\"name\":\"Seaberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackthorn\",\"name\":\"Sloan Berries Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/strawberry\",\"name\":\"Strawberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/woodstrawberry\",\"name\":\"Wood Strawberry\"}"));
//        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackberrybush-yester\",\"name\":\"Yesteryear's Blackberry Seed\"}"));  // Missing resource
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackcurrant-yester\",\"name\":\"Yesteryear's Blackcurrant Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/cherry-yester\",\"name\":\"Yesteryear's Cherry\"}"));
//        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-dogrose-yester\",\"name\":\"Yesteryear's Dog Rose Seed\"}"));  // Missing resource
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-elderberrybush-yester\",\"name\":\"Yesteryear's Elderberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-gooseberrybush-yester\",\"name\":\"Yesteryear's Gooseberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/mulberry-yester\",\"name\":\"Yesteryear's Mulberry\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-raspberrybush-yester\",\"name\":\"Yesteryear's Raspberry Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-redcurrant-yester\",\"name\":\"Yesteryear's Redcurrant Seed\"}"));
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sandthorn-yester\",\"name\":\"Yesteryear's Seaberry Seed\"}"));
//        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-blackthorn-yester\",\"name\":\"Yesteryear's Sloan Berries Seed\"}"));  // Missing resource
        berries.add(new JSONObject("{\"static\":\"gfx/invobjs/woodstrawberry-yester\",\"name\":\"Yesteryear's Wood Strawberry\"}"));
        categories.put("Berries", berries);

        ArrayList<JSONObject> fruits = new ArrayList<>();
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/cherry\",\"name\":\"Cherry\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/fig\",\"name\":\"Fig\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/grapes\",\"name\":\"Grapes\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/lemon\",\"name\":\"Lemon\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/medlar\",\"name\":\"Medlar\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/mulberry\",\"name\":\"Mulberry\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/orange\",\"name\":\"Orange\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/pear\",\"name\":\"Pear\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/persimmon\",\"name\":\"Persimmon\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/plum\",\"name\":\"Plum\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/quince\",\"name\":\"Quince\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-raspberrybush\",\"name\":\"Raspberry Bush Seed\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/apple\",\"name\":\"Red Apple\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sandthorn\",\"name\":\"Seaberry Seed\"}"));
//        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sorb\",\"name\":\"Sorb Apple Seed\"}"));  // Missing resource
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/cherry-yester\",\"name\":\"Yesteryear's Cherry\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/fig-yester\",\"name\":\"Yesteryear's Fig\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/lemon-yester\",\"name\":\"Yesteryear's Lemon\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/medlar-yester\",\"name\":\"Yesteryear's Medlar\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/mulberry-yester\",\"name\":\"Yesteryear's Mulberry\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/orange-yester\",\"name\":\"Yesteryear's Orange\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/pear-yester\",\"name\":\"Yesteryear's Pear\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/persimmon-yester\",\"name\":\"Yesteryear's Persimmon\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/plum-yester\",\"name\":\"Yesteryear's Plum\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/quince-yester\",\"name\":\"Yesteryear's Quince\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-raspberrybush-yester\",\"name\":\"Yesteryear's Raspberry Bush Seed\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/apple-yester\",\"name\":\"Yesteryear's Red Apple\"}"));
        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sandthorn-yester\",\"name\":\"Yesteryear's Seaberry Seed\"}"));
//        fruits.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-sorb-yester\",\"name\":\"Yesteryear's Sorb Apple Seed\"}"));  // Missing resource
        categories.put("Fruit", fruits);

        ArrayList<JSONObject> fruitOrBerry = new ArrayList<>();
        fruitOrBerry.addAll(fruits);
        fruitOrBerry.addAll(berries);
        categories.put("Fruit or Berry", fruitOrBerry);

        ArrayList<JSONObject> flours = new ArrayList<>();
        flours.add(new JSONObject("{\"static\":\"gfx/invobjs/flour-barleyflour\",\"name\":\"Barley Flour\"}"));
        flours.add(new JSONObject("{\"static\":\"gfx/invobjs/flour-milletflour\",\"name\":\"Millet Flour\"}"));
        flours.add(new JSONObject("{\"static\":\"gfx/invobjs/flour-wheatflour\",\"name\":\"Wheat Flour\"}"));
        categories.put("Flour", flours);

        ArrayList<JSONObject> bugs = new ArrayList<>();
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ants-empress\",\"name\":\"Ant Empress\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ants-larvae\",\"name\":\"Ant Larvae\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ants-pupae\",\"name\":\"Ant Pupae\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ants-queen\",\"name\":\"Ant Queen\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ants-soldiers\",\"name\":\"Ant Soldiers\"}"));
//        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/aphids\",\"name\":\"Aphids\"}"));  // Resource missing
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/beelarvae\",\"name\":\"Bee Larvae\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/brimstonebutterfly\",\"name\":\"Brimstone Butterfly\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/cavecentipede\",\"name\":\"Cave Centipede\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/cavemoth\",\"name\":\"Cave Moth\"}"));
//        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/emeralddragonfly\",\"name\":\"Emerald Dragonfly\"}"));  // Resource missing
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/firefly\",\"name\":\"Firefly\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/grasshopper\",\"name\":\"Grasshopper\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/grub\",\"name\":\"Grub\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/itsybitsyspider\",\"name\":\"Itsy Bitsy Spider\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/ladybug\",\"name\":\"Ladybug\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/monarchbutterfly\",\"name\":\"Monarch Butterfly\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/moonmoth\",\"name\":\"Moonmoth\"}"));
//        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/rubydragonfly\",\"name\":\"Ruby Dragonfly\"}"));  // Resource missing
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/sandflea\",\"name\":\"Sand Flea\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/silkmoth-f\",\"name\":\"Silkmoth\"}"));  // Resource missing
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/silkworm\",\"name\":\"Silkworm\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/springbumblebee\",\"name\":\"Springtime Bumblebee\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/stagbeetle\",\"name\":\"Stag Beetle\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/beethatstung\",\"name\":\"The Bee That Stung\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/waterstrider\",\"name\":\"Waterstrider\"}"));
        bugs.add(new JSONObject("{\"static\":\"gfx/invobjs/woodworm\",\"name\":\"Woodworm\"}"));
        categories.put("Bug", bugs);

        ArrayList<JSONObject> leaves = new ArrayList<>();
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/beetleaves\",\"name\":\"Beetroot Leaves\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/tea-black\",\"name\":\"Black Tea Leaves\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-conkertree\",\"name\":\"Conker Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/duskfern\",\"name\":\"Dusk Fern\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-fig\",\"name\":\"Fig Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/tea-fresh\",\"name\":\"Fresh Tea Leaves\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/greenkelp\",\"name\":\"Green Kelp\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/tea-green\",\"name\":\"Green Tea Leaves\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-heartwood\",\"name\":\"Heartwood Leaves\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-laurel\",\"name\":\"Laurel Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/lettuceleaf\",\"name\":\"Lettuce Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-maple\",\"name\":\"Maple Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-mulberrytree\",\"name\":\"Mulberry Leaf\"}"));
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/perfectautumnleaf\",\"name\":\"Perfect Autumn Leaf\"}"));  // Missing resource
        leaves.add(new JSONObject("{\"static\":\"gfx/invobjs/leaf-swamplily\",\"name\":\"Swamplily Leaf Shred\"}"));
        categories.put("Leaf", leaves);

        ArrayList<JSONObject> fats = new ArrayList<>();
        //fats.add(new JSONObject("{\"static\":\"gfx/invobjs/lard\",\"name\":\"Lard\"}"));
        fats.add(new JSONObject("{\"static\":\"gfx/invobjs/animalfat-r\",\"name\":\"Rendered Animal Fat\"}"));
        fats.add(new JSONObject("{\"static\":\"gfx/invobjs/butter\",\"name\":\"Butter\"}"));
        categories.put("Solid Fat", fats);

        ArrayList<JSONObject> tea = new ArrayList<>();
        tea.add(new JSONObject("{\"static\":\"gfx/invobjs/tea-green\",\"name\":\"Green Tea Leaves\"}"));
        tea.add(new JSONObject("{\"static\":\"gfx/invobjs/tea-black\",\"name\":\"Black Tea Leaves\"}"));
        categories.put("Cured Tea", tea);

        ArrayList<JSONObject> snails = new ArrayList<>();
        snails.add(new JSONObject("{\"static\":\"gfx/invobjs/forestsnail\",\"name\":\"Forest Snail\"}"));
        snails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/lakesnail\",\"name\":\"Lake Snail\"}"));
        categories.put("Snail", snails);

        ArrayList<JSONObject> cleanCarcasses = new ArrayList<>();
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/adder-clean\",\"name\":\"Clean Adder Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/hedgehog-clean\",\"name\":\"Clean Hedgehog Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/mole-clean\",\"name\":\"Clean Mole Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/rabbit-clean\",\"name\":\"Clean Rabbit Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/squirrel-clean\",\"name\":\"Clean Squirrel Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/stoat-clean\",\"name\":\"Clean Stoat Carcass\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bat-clean\",\"name\":\"Cleaned Bat\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bogturtle-cleaned\",\"name\":\"Cleaned Bog Turtle\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bullfinch-cleaned\",\"name\":\"Cleaned Bullfinch\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/chicken-cleaned\",\"name\":\"Cleaned Chicken\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/eagleowl-cleaned\",\"name\":\"Cleaned Eagle Owl\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/goldeneagle-cleaned\",\"name\":\"Cleaned Golden Eagle\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/magpie-cleaned\",\"name\":\"Cleaned Magpie\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/mallard-cleaned\",\"name\":\"Cleaned Mallard\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/pelican-cleaned\",\"name\":\"Cleaned Pelican\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/ptarmigan-cleaned\",\"name\":\"Cleaned Ptarmigan\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/quail-cleaned\",\"name\":\"Cleaned Quail\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/rockdove-cleaned\",\"name\":\"Cleaned Rock Dove\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/seagull-cleaned\",\"name\":\"Cleaned Seagull\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/swan-cleaned\",\"name\":\"Cleaned Swan\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/woodgrouse-m-cleaned\",\"name\":\"Cleaned Wood Grouse Cock\"}"));
        cleanCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/woodgrouse-f-cleaned\",\"name\":\"Cleaned Wood Grouse Hen\"}"));
        categories.put("Clean Animal Carcass", cleanCarcasses);

        ArrayList<JSONObject> boneMaterials = new ArrayList<>();
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/adderskeleton\",\"name\":\"Adder Skeleton\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/antchitin\",\"name\":\"Ant Chitin\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/beartooth\",\"name\":\"Bear Tooth\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/beechitin\",\"name\":\"Bee Chitin\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/billygoathorn\",\"name\":\"Billygoat Horn\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/boartusk\",\"name\":\"Boar Tusk\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/bogturtleshell\",\"name\":\"Bog Turtle Shell\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/borewormbeak\",\"name\":\"Boreworm Beak\"}"));
//        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/cachalottooth\",\"name\":\"Cachalot Tooth\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/cavelousechitin\",\"name\":\"Cave Louse Chitin\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/crabshell\",\"name\":\"Crabshell\"}"));
//        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/flipperbones\",\"name\":\"Flipper Bones\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/lynxclaws\",\"name\":\"Lynx Claws\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/mammothtusk\",\"name\":\"Mammoth Tusk\"}"));
//        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/molespawbone\",\"name\":\"Mole's Pawbone\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/antlers-moose\",\"name\":\"Moose Antlers\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/orcatooth\",\"name\":\"Orca Tooth\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/antlers-reddeer\",\"name\":\"Red Deer Antlers\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/antlers-reindeer\",\"name\":\"Reindeer Antlers\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/antlers-roedeer\",\"name\":\"Roe Deer Antlers\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/trollskull\",\"name\":\"Troll Skull\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/trolltusks\",\"name\":\"Troll Tusks\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/trollbone\",\"name\":\"Trollbone\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/walrustusk\",\"name\":\"Walrus Tusk\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/whalebone\",\"name\":\"Whale Bone Material\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/wildgoathorn\",\"name\":\"Wildgoat Horn\"}"));
        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/wishbone\",\"name\":\"Wishbone\"}"));
//        boneMaterials.add(new JSONObject("{\"static\":\"gfx/invobjs/wolfsclaw\",\"name\":\"Wolf's Claw\"}"));
        categories.put("Bone Material", boneMaterials);

        ArrayList<JSONObject> cleanedBirds = new ArrayList<>();
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/bullfinch-cleaned\",\"name\":\"Cleaned Bullfinch\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/chicken-cleaned\",\"name\":\"Cleaned Chicken\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/goldeneagle-cleaned\",\"name\":\"Cleaned Golden Eagle\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/magpie-cleaned\",\"name\":\"Cleaned Magpie\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/mallard-cleaned\",\"name\":\"Cleaned Mallard\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/pelican-cleaned\",\"name\":\"Cleaned Pelican\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/ptarmigan-cleaned\",\"name\":\"Cleaned Ptarmigan\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/quail-cleaned\",\"name\":\"Cleaned Quail\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/rockdove-cleaned\",\"name\":\"Cleaned Rock Dove\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/seagull-cleaned\",\"name\":\"Cleaned Seagull\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/swan-cleaned\",\"name\":\"Cleaned Swan\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/woodgrouse-m-cleaned\",\"name\":\"Cleaned Wood Grouse Cock\"}"));
        cleanedBirds.add(new JSONObject("{\"static\":\"gfx/invobjs/woodgrouse-f-cleaned\",\"name\":\"Cleaned Wood Grouse Hen\"}"));
        categories.put("Clean Bird Carcass", cleanedBirds);

        ArrayList<JSONObject> mollusksAndSnails = new ArrayList<>();
        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/goosebarnacle\",\"name\":\"Gooseneck Barnacle\"}"));
        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/lakesnail\",\"name\":\"Lake Snail\"}"));
        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/oyster\",\"name\":\"Oyster\"}"));
//        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/razorclam\",\"name\":\"Razor Clam\"}"));
        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/mussels\",\"name\":\"River Pearl Mussel\"}"));
        mollusksAndSnails.add(new JSONObject("{\"static\":\"gfx/invobjs/roundclam\",\"name\":\"Round Clam\"}"));
        categories.put("Edible Seashell", mollusksAndSnails);

    }

    public static HashMap<NStyle.Container, Integer> chest_state = new HashMap<>();
    static
    {
        chest_state.put(NStyle.Container.FREE, 3);
        chest_state.put(NStyle.Container.FULL, 28);
    }
}
