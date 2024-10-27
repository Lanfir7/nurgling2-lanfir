package nurgling.actions.bots;

import haven.Coord;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.tasks.WaitForBurnout;
import nurgling.tools.Container;
import nurgling.tools.Context;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;
import java.util.Arrays;

public class TarKilnAction implements Action {
    private static final NAlias tarkilnAlias = new NAlias("gfx/terobjs/tarkiln");
    private static final NAlias blockPiles = new NAlias("gfx/terobjs/stockpile-wblock");
    private static final NAlias blockItems = new NAlias(new ArrayList<>(Arrays.asList("Block")));
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        NUtils.getGameUI().msg("Starting kiln ash collection...");

        NArea kilnArea = NArea.findSpec(Specialisation.SpecName.tarKiln.toString());
        ArrayList<Gob> kilns = Finder.findGobs(kilnArea, tarkilnAlias);
        if (kilns.isEmpty()) {
            NUtils.getGameUI().msg("No tar kilns found!");
            return Results.FAIL();
        }

        ArrayList<Container> containers = new ArrayList<>();
        for (Gob kiln : kilns) {
            Container container = new Container();
            container.gob = kiln;
            container.cap = "Tar Kiln";
            container.initattr(Container.Space.class);
            container.initattr(Container.FuelLvl.class);
            container.getattr(Container.FuelLvl.class).setMaxlvl(80);
            container.getattr(Container.FuelLvl.class).setFueltype("block");
            containers.add(container);
        }
        ArrayList<Gob> lighted = new ArrayList<>();
        for (Container cont : containers) {
            lighted.add(cont.gob);
        }
        Results res = null;
        //while (res == null || res.IsSuccess()) {
            ArrayList<Container> forFuel = new ArrayList<>();
            for(Container container: containers) {
                Container.Space space = container.getattr(Container.Space.class);
                if(!space.isEmpty())
                    forFuel.add(container);
            }
            new FuelToContainers(forFuel).run(gui);

            ArrayList<Gob> flighted = new ArrayList<>();
            for (Container cont : forFuel) {
                flighted.add(cont.gob);
            }
            if (!new LightGob(flighted, 1).run(gui).IsSuccess())
                return Results.ERROR("I can't start a fire");

        //}

        NUtils.getGameUI().msg("Successfully kilns.");
        return Results.SUCCESS();
    }
}
