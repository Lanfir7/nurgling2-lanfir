package nurgling.actions.bots;

import haven.Coord;
import haven.Coord2d;
import haven.Gob;
import haven.Pair;
import nurgling.NGameUI;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.tools.Container;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;
import nurgling.widgets.Specialisation;

import java.util.ArrayList;

public class GreenTeaAction implements Action {

    NAlias raw = new NAlias("Fresh Tea Leaves");
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        NArea.Specialisation rtables = new NArea.Specialisation(Specialisation.SpecName.htable.toString());

        ArrayList<NArea.Specialisation> req = new ArrayList<>();
        req.add(rtables);
        ArrayList<NArea.Specialisation> opt = new ArrayList<>();
        if(new Validator(req, opt).run(gui).IsSuccess()) {

            NArea npile_area = NArea.findIn(raw.getDefault());
            Pair<Coord2d,Coord2d> pile_area = npile_area!=null?npile_area.getRCArea():null;
            if(pile_area==null)
            {
                return Results.ERROR("Fresh Tea Leaves not found");
            }

            ArrayList<Container> containers = new ArrayList<>();

            for (Gob htable : Finder.findGobs(NArea.findSpec(Specialisation.SpecName.htable.toString()),
                    new NAlias("gfx/terobjs/htable"))) {
                Container cand = new Container();
                cand.gob = htable;
                cand.cap = "Herbalist Table";

                cand.initattr(Container.Space.class);

                containers.add(cand);
            }


            new FreeContainers(containers, new NAlias("Green Tea Leaves", "Black Tea Leaves", "Cured Pipeweed")).run(gui);
            new FillContainersFromPiles(containers, pile_area, raw).run(gui);

            return Results.SUCCESS();
        }
        return Results.FAIL();
    }
}
