package nurgling.actions.bots;

import haven.Coord;
import haven.Resource;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.Build;
import nurgling.actions.Results;
import nurgling.tools.NAlias;
import nurgling.tools.VSpec;

public class BuildTrellis implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        Build.Command command = new Build.Command();
        command.name = "Trellis";

        NUtils.getGameUI().msg("Please, select build area");
        SelectArea buildarea = new SelectArea(Resource.loadsimg("baubles/buildArea"));
        buildarea.run(NUtils.getGameUI());

        NUtils.getGameUI().msg("Please, select area for block");
        SelectArea blockarea = new SelectArea(Resource.loadsimg("baubles/blockIng"));
        blockarea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,2),blockarea.getRCArea(),new NAlias("Block"),3));

        NUtils.getGameUI().msg("Please, select area for strings");
        SelectArea stringarea = new SelectArea(Resource.loadsimg("baubles/stringsIng"));
        stringarea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,1),stringarea.getRCArea(), VSpec.getNamesInCategory("String"),1));


        new Build(command, buildarea.getRCArea()).run(gui);
        return Results.SUCCESS();
    }
}
