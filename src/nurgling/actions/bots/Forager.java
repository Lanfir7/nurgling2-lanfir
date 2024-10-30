package nurgling.actions.bots;
import haven.Coord;
import haven.Gob;
import haven.Coord2d;
import haven.Pair;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.CollectFromGob;
import nurgling.actions.GoTo;
import nurgling.actions.Results;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Forager implements Action {
    NAlias plants = new NAlias(new ArrayList<>(List.of(
            "Yellowfeet", "Yarrow", "Wintergreen", "Wild Windsown Weed", "Waybroad",
            "Washed-up Bladderwrack", "Uncommon Snapdragon", "Thyme", "Thorny Thistle",
            "Tangled Bramble", "Swamplily", "Strawberry", "Stinging Nettle", "Stalagoom",
            "Spirited Mandrake Root", "Spindly Taproot", "Sleighbell", "Sage", "Rustroot",
            "Royal Toadstool", "River Pearl Mussel", "Razor Clam", "Rainbowpad",
            "Rainbow Shell", "Perfect Autumn Leaf", "Peculiar Flotsam", "Parasol Mushroom",
            "Oyster", "Morels", "Mistletoe", "Marsh-Mallow", "Mandrake Root", "Lingonberries",
            "Lamp Stalk", "Lady's Mantle", "Kvann", "Indigo Cap", "Heartsease", "Green Kelp",
            "Gray Clay", "Gooseneck Barnacle", "Glimmermoss", "Giant Puffball", "Frogspawn",
            "Frog's Crown", "Four-Leaf Clover", "Field Blewits", "Elven Lights", "Edelweiss",
            "Dusk Fern", "Dill", "Dewy Lady's Mantle", "Dandelion", "Common Starfish",
            "Coltsfoot", "Clover", "Chives", "Chiming Bluebell", "Chantrelles", "Cavebulb",
            "Cave Lantern", "Cave Coral", "Cave Clay", "Cattail", "Candleberry", "Camomile",
            "Blueberries", "Blood Stern"
    )));
    NAlias plantsRes = new NAlias(new ArrayList<>(List.of("gfx/terobjs/herbs")),new ArrayList<String>(Arrays.asList("gfx/terobjs/herbs/mistletoe","gfx/terobjs/herbs/oystermushroom")));
    Random random = new Random();
    NAlias hostileAnimals = new NAlias(new ArrayList<>(List.of("adder", "bear", "wolf", "boar", "badger", "beaver", "lynx", "mammoth", "moose", "troll", "walrus", "wolverine", "wolverine")));

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        while (true) {
            int offsetX = -100 + random.nextInt(201);
            int offsetY = -100 + random.nextInt(201);

            // Вычисление новой цели для движения
            Coord2d playerPos = NUtils.player().rc;
            Coord2d newTarget = playerPos.add(offsetX, offsetY);
            ArrayList<Gob> nearbyHostiles = Finder.findGobs(new Pair<>(new Coord2d(playerPos.x - 300, playerPos.y - 300), new Coord2d(playerPos.x + 300, playerPos.y + 300)), hostileAnimals);
            boolean isSafe = true;
            for (Gob hostile : nearbyHostiles) {
                if (hostile.rc.dist(playerPos) < 50) {
                    isSafe = false;
                    break;
                }
            }

            if (!isSafe) {
                NUtils.getGameUI().msg("Враждебные животные поблизости! Отхожу подальше.");
                newTarget = playerPos.add(-offsetX, -offsetY); // Отойти в противоположную сторону
            }
            new GoTo(newTarget).run(gui);

            ArrayList<Gob> nearbyPlants = Finder.findGobs(new Pair<>(new Coord2d(playerPos.x - 500, playerPos.y - 500), new Coord2d(playerPos.x + 500, playerPos.y + 500)), plantsRes);
            nearbyPlants.sort(NUtils.d_comp);
            for (Gob plant : nearbyPlants) {

                new CollectFromGob(plant, "Pick", null, new Coord(1, 1), plants, null).run(gui);
            }
        }
    }
}
