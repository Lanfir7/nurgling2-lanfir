package nurgling.actions.bots;

import haven.Coord;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*; // Import your ForageHelperWnd class
import nurgling.widgets.options.NForageHelper;

public class TarKilnAction implements Action {
    public Results run(NGameUI gui) throws InterruptedException {
        NUtils.getGameUI().msg("Starting kiln ash collection...");

        // Instantiate and add the ForageHelperWnd window to the GUI
        NForageHelper forageHelperWindow = new NForageHelper();
        gui.add(forageHelperWindow, new Coord(100, 100)); // Position at (100, 100) or wherever you prefer

        return Results.SUCCESS();
    }
}