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

public class LiftAndMoveObjects implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Select the first source area
        SelectArea firstSourceArea;
        NUtils.getGameUI().msg("Please, select first area.");
        (firstSourceArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> firstSourceZone = firstSourceArea.getRCArea();

        // Select the second area (target area initially)
        SelectArea secondArea;
        NUtils.getGameUI().msg("Please, select second area.");
        (secondArea = new SelectArea()).run(gui);
        Pair<Coord2d, Coord2d> secondZone = secondArea.getRCArea();

        // Infinite loop to keep moving objects back and forth
        while (true) {
            // Move objects from the first area to the second area
            moveObjectsBetweenZones(gui, firstSourceZone, secondZone);
            // Move objects from the second area back to the first area
            moveObjectsBetweenZones(gui, secondZone, firstSourceZone);
        }
    }

    private void moveObjectsBetweenZones(NGameUI gui, Pair<Coord2d, Coord2d> sourceZone, Pair<Coord2d, Coord2d> targetZone) throws InterruptedException {
        ArrayList<Gob> objectsToMove;
        while (!(objectsToMove = Finder.findGobs(sourceZone, new NAlias(""))).isEmpty()) {
            // Filter out objects that cannot be reached
            ArrayList<Gob> availableObjects = new ArrayList<>();
            for (Gob currGob : objectsToMove) {
                if (PathFinder.isAvailable(currGob)) {
                    availableObjects.add(currGob);
                }
            }

            // If no available objects are found, break the loop and switch zones
            if (availableObjects.isEmpty()) {
                NUtils.getGameUI().msg("No more objects to move in this zone.");
                break;
            }

            // Sort objects by proximity to the player
            availableObjects.sort(NUtils.d_comp);
            Gob objectToMove = availableObjects.get(0);  // Pick the closest object

            // Lift the object
            new LiftObject(objectToMove).run(gui);

            // Move to the target area and place the object
            new FindPlaceAndAction(objectToMove, targetZone).run(gui);

            // Step back a bit after placing the object to avoid collision issues
            Coord2d shift = objectToMove.rc.sub(NUtils.player().rc).norm().mul(2);
            new GoTo(NUtils.player().rc.sub(shift)).run(gui);

            NUtils.getGameUI().msg("Moved object: " + objectToMove.id);
        }
    }
}
