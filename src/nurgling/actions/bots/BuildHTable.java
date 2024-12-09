package nurgling.actions.bots;

import haven.Coord;
import haven.Resource;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.Build;
import nurgling.actions.Results;
import nurgling.tools.NAlias;

public class BuildHTable implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        Build.Command command = new Build.Command();
        command.name = "Herbalist Table";

        NUtils.getGameUI().msg("Please, select build area");
        SelectArea buildarea = new SelectArea(Resource.loadsimg("baubles/buildArea"));
        buildarea.run(NUtils.getGameUI());

        NUtils.getGameUI().msg("Please, select area for board");
        SelectArea brancharea = new SelectArea(Resource.loadsimg("baubles/boardIng"));
        brancharea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(4,1),brancharea.getRCArea(),new NAlias("Board"),4));

        NUtils.getGameUI().msg("Please, select area for block");
        SelectArea bougharea = new SelectArea(Resource.loadsimg("baubles/blockIng"));
        bougharea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,2),bougharea.getRCArea(),new NAlias("Block"),4));

        NUtils.getGameUI().msg("Please, select area for strings");
        SelectArea stringarea = new SelectArea(Resource.loadsimg("baubles/stringsIng"));
        stringarea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,1),stringarea.getRCArea(),new NAlias("Flax Fibres", "Grass Twine", "Hemp Fibres", "Spindly Taproot", "Cattail Fibres", "Stinging Nettle", "Hide Strap", "Straw Twine", "Bark Cordage"),8));

        new Build(command, buildarea.getRCArea()).run(gui);
        return Results.SUCCESS();
    }
}
