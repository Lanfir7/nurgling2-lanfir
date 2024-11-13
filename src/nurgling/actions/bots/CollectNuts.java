package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import haven.Resource;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.CollectFromGob;
import nurgling.actions.Results;
import nurgling.actions.TransferToPiles;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.tools.VSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectNuts implements Action {

    NAlias ntrees = new NAlias(new ArrayList<String>(List.of(
            "gfx/terobjs/trees/almondtree",
            "gfx/terobjs/trees/beech",
            "gfx/terobjs/trees/carobtree",
            "gfx/terobjs/trees/chestnuttree",
            "gfx/terobjs/trees/hazel",
            "gfx/terobjs/trees/kingsoak",
            "gfx/terobjs/trees/oak",
            "gfx/terobjs/trees/walnuttree",
            "gfx/terobjs/bushes/witherstand"
    )),new ArrayList<String>(Arrays.asList("log", "oldtrunk", "stump")));
    NAlias actions = new NAlias(new ArrayList<String>(List.of(
            "Pick acorn",
            "Pick walnut",
            "Pick chestnut",
            "Pick fruit"
    )));

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        SelectArea insa;
        NUtils.getGameUI().msg("Please select area with trees or bushes");
        (insa = new SelectArea()).run(gui);

        SelectArea outsa;
        NUtils.getGameUI().msg("Please select area for piles");
        (outsa = new SelectArea()).run(gui);

        ArrayList<Gob> trees = Finder.findGobs(insa.getRCArea(),ntrees);
        trees.sort(NUtils.d_comp);
//        for(Gob tree : trees)
//        {
//            new CollectFromGob(tree, actions, "gfx/borka/treepickan", new Coord(1,1), VSpec.getNamesInCategory("Nuts"),outsa.getRCArea()).run(gui);
//        }
        new TransferToPiles(outsa.getRCArea(), VSpec.getNamesInCategory("Nuts") ).run(gui);
        return Results.SUCCESS();
    }
}
