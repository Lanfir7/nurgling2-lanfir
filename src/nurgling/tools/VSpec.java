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

        ArrayList<JSONObject> tubers = new ArrayList<>();
        tubers.add(new JSONObject("{\"static\":\"gfx/invobjs/beet\",\"name\":\"Beetroot\"}"));
        tubers.add(new JSONObject("{\"static\":\"gfx/invobjs/carrot\",\"name\":\"Carrot\"}"));
        tubers.add(new JSONObject("{\"static\":\"gfx/invobjs/turnip\",\"name\":\"Turnip\"}"));
        categories.put("Tuber", tubers);

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
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-turnip\",\"name\":\"Turnip Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-carrot\",\"name\":\"Carrot Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-wheat\",\"name\":\"Wheat Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-hemp\",\"name\":\"Hemp Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-flax\",\"name\":\"Flax Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-poppy\",\"name\":\"Poppy Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-barley\",\"name\":\"Barley Seed\"}"));
        seeds.add(new JSONObject("{\"static\":\"gfx/invobjs/seed-pumpkin\",\"name\":\"Pumpkin Seed\"}"));
        categories.put("Crop Seeds", seeds);

        ArrayList<JSONObject> eggs = new ArrayList<>();
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-chicken\",\"name\":\"Chicken Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-goose\",\"name\":\"Goose Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-peacock\",\"name\":\"Peacock Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-turkey\",\"name\":\"Turkey Egg\"}"));
        eggs.add(new JSONObject("{\"static\":\"gfx/invobjs/egg-duck\",\"name\":\"Duck Egg\"}"));
        categories.put("Egg", eggs);

        categories.put("Raw", new ArrayList<>());

        ArrayList<JSONObject> mushrooms = new ArrayList<>();
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/baybolete\",\"name\":\"Bay Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/bloatedbolete\",\"name\":\"Bloated Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/wblock-trombonechantrelle\",\"name\":\"Block of Trombone Chantrelle\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/buttonmushroom\",\"name\":\"Button Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/chantrelles\",\"name\":\"Chantrelles\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/cremini\",\"name\":\"Cremini Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/fieldblewits\",\"name\":\"Field Blewits\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/giantpuffball\",\"name\":\"Giant Puffball\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/libertycaps\",\"name\":\"Liberty Caps\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/oystermushroom\",\"name\":\"Oyster Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/parasolmushroom\",\"name\":\"Parasol Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/parboiledmorels\",\"name\":\"Parboiled Morels\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/portobello\",\"name\":\"Portobello Mushroom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/rubybolete\",\"name\":\"Ruby Bolete\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/snowtop\",\"name\":\"Snowtop\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/stalagoom\",\"name\":\"Stalagoom\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/trollmushrooms\",\"name\":\"Troll Mushrooms\"}"));
        mushrooms.add(new JSONObject("{\"static\":\"gfx/invobjs/herbs/yellowfeet\",\"name\":\"Yellowfeet\"}"));
        categories.put("Edible Mushroom", mushrooms);

        ArrayList<JSONObject> fats = new ArrayList<>();
        //fats.add(new JSONObject("{\"static\":\"gfx/invobjs/lard\",\"name\":\"Lard\"}"));
        fats.add(new JSONObject("{\"static\":\"gfx/invobjs/beeswax\",\"name\":\"Beeswax\"}"));
        fats.add(new JSONObject("{\"static\":\"gfx/invobjs/butter\",\"name\":\"Butter\"}"));
        categories.put("Solid Fat", fats);

        ArrayList<JSONObject> birdCarcasses = new ArrayList<>();
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-chicken\",\"name\":\"Clean Chicken Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-duck\",\"name\":\"Clean Duck Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-goose\",\"name\":\"Clean Goose Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-peacock\",\"name\":\"Clean Peacock Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-pigeon\",\"name\":\"Clean Pigeon Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-swan\",\"name\":\"Clean Swan Carcass\"}"));
        birdCarcasses.add(new JSONObject("{\"static\":\"gfx/invobjs/bird-turkey\",\"name\":\"Clean Turkey Carcass\"}"));
        categories.put("Clean Bird Carcass", birdCarcasses);


    }

    public static HashMap<NStyle.Container, Integer> chest_state = new HashMap<>();
    static
    {
        chest_state.put(NStyle.Container.FREE, 3);
        chest_state.put(NStyle.Container.FULL, 28);
    }
}
