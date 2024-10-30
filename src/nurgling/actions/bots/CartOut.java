package nurgling.actions.bots;

import haven.Coord;
import haven.Coord2d;
import haven.Gob;
import haven.Pair;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.tasks.IsMoving;
import nurgling.tasks.WaitPose;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import static haven.OCache.posres;


public class CartOut implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Select the target area
        SelectArea targetArea;
        NUtils.getGameUI().msg("Please, select output area.");
        (targetArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> targetZone = targetArea.getRCArea();

        Gob cart = Finder.findNearestObject(new NAlias("cart"), 1000);
        if (cart == null) {
            NUtils.getGameUI().msg("Cart not found.");
            return Results.ERROR("No cart found nearby.");
        }
        int n = 2;
        for(int i = 0; i < 6; i++){
            n = n<<1;
            if((cart.ngob.getModelAttribute() & n) !=0){
                new PathFinder(cart).run(gui);
                NUtils.activate(cart, i+2);
                NUtils.getUI().core.addTask(new WaitPose(NUtils.player(), "banzai"));
                Gob lifted = Finder.findLiftedbyPlayer();
                new FindPlaceAndAction(lifted, targetZone).run(gui);

                Coord2d shift = lifted.rc.sub(NUtils.player().rc).norm().mul(2);
                new GoTo(NUtils.player().rc.sub(shift)).run(gui);

            }
        }
        NUtils.getGameUI().msg("All objects moved successfully.");
        return Results.SUCCESS();
    }
}
