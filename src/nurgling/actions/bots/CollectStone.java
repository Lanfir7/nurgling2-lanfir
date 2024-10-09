package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectStone implements Action {

    NAlias ntrees = new NAlias(new ArrayList<String>(List.of("gfx/terobjs/bumlings")),new ArrayList<String>(Arrays.asList("log", "oldtrunk", "stump")));

    NAlias stones = new NAlias("Alabaster",
        "Apatite",
        "Zincspar",
        "Soapstone",
        "Sunstone",
        "Sodalite",
        "Black Coal",
        "Graywacke",
        "Slate",
        "Schist",
        "Chert",
        "Wine Glance",
        "Serpentine",
        "Arkose",
        "Basalt",
        "Bat Rock",
        "Black Ore",
        "Bloodstone",
        "Breccia",
        "Cassiterite",
        "Cat Gold",
        "Chalcopyrite",
        "Cinnabar",
        "Diabase",
        "Diorite",
        "Direvein",
        "Dolomite",
        "Dross",
        "Eclogite",
        "Feldspar",
        "Flint",
        "Fluorospar",
        "Gabbro",
        "Galena",
        "Gneiss",
        "Granite",
        "Greenschist",
        "Heavy Earth",
        "Horn Silver",
        "Hornblende",
        "Iron Ochre",
        "Jasper",
        "Korund",
        "Kyanite",
        "Lead Glance",
        "Leaf Ore",
        "Limestone",
        "Malachite",
        "Marble",
        "Meteorite",
        "Mica",
        "Microlite",
        "Olivine",
        "Orthoclase",
        "Peacock Ore",
        "Pegmatite",
        "Porphyry",
        "Pumice",
        "Quarryartz",
        "Quartz",
        "Rhyolite");
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        if(!new StackOff().run(gui).IsSuccess())
            return Results.ERROR("Не получилось выключить стаки");

        SelectArea insa;
        NUtils.getGameUI().msg("Please select area with bumlings");
        (insa = new SelectArea()).run(gui);

        SelectArea outsa;
        NUtils.getGameUI().msg("Please select area for piles");
        (outsa = new SelectArea()).run(gui);

        ArrayList<Gob> trees = Finder.findGobs(insa.getRCArea(), ntrees);
        trees.sort(NUtils.d_comp);
        for(Gob tree : trees)
        {
            //String pickAction = tree.getres().name.contains("tree") ? "gfx/borka/chipping" : "gfx/borka/bushpickan";
            new CollectFromGob(tree,"Chip stone", "gfx/borka/chipping" ,new Coord(1,1), stones, outsa.getRCArea()).run(gui);
        }
        new TransferToPiles(outsa.getRCArea(), stones ).run(gui);
        return Results.SUCCESS();
    }
}
