package nurgling.actions.bots;

import haven.Coord2d;
import haven.Gob;
import haven.Pair;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiftAndMoveObjects implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Select the source area
        SelectArea sourceArea;
        NUtils.getGameUI().msg("Please, select input area.");
        (sourceArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> sourceZone = sourceArea.getRCArea();

        // Select the target area
        SelectArea targetArea;
        NUtils.getGameUI().msg("Please, select output area.");
        (targetArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> targetZone = targetArea.getRCArea();

        // Find all objects (Gobs) in the source area
        ArrayList<Gob> objectsToMove;
        NAlias lifted = new NAlias(new ArrayList<String>(List.of("")),new ArrayList<String>(Arrays.asList("body")));
        while (!(objectsToMove = Finder.findGobs(sourceZone, lifted)).isEmpty()) {
            // Filter out objects that cannot be reached
            ArrayList<Gob> availableObjects = new ArrayList<>();
            for (Gob currGob : objectsToMove) {
                if (PathFinder.isAvailable(currGob)) {
                    availableObjects.add(currGob);
                }
            }

            // If no available objects are found, return an error
            if (availableObjects.isEmpty()) {
                return Results.ERROR("Cannot reach any object.");
            }

            // Sort objects by proximity to the player
            availableObjects.sort(NUtils.d_comp);
            Gob objectToMove = availableObjects.get(0);  // Pick the closest object

            // Lift the object
            new LiftObject(objectToMove).run(gui);

            // Move to the target area and place the object
            new FindPlaceAndAction(objectToMove, targetZone).run(gui);

            Coord2d shift = objectToMove.rc.sub(NUtils.player().rc).norm().mul(2);
            new GoTo(NUtils.player().rc.sub(shift)).run(gui);

            //NUtils.getGameUI().msg("Moved object: " + objectToMove.id);
        }

        NUtils.getGameUI().msg("All objects moved successfully.");
        return Results.SUCCESS();
    }
}
