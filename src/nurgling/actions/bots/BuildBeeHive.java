package nurgling.actions.bots;

import haven.Coord;
import haven.Resource;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.Build;
import nurgling.actions.Results;
import nurgling.tools.NAlias;

public class BuildBeeHive implements Action {
    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        Build.Command command = new Build.Command();
        command.name = "Bee Skep";

        NUtils.getGameUI().msg("Please, select build area");
        SelectArea buildarea = new SelectArea(Resource.loadsimg("baubles/buildArea"));
        buildarea.run(NUtils.getGameUI());

        NUtils.getGameUI().msg("Please, select area for board");
        SelectArea brancharea = new SelectArea(Resource.loadsimg("baubles/boardIng"));
        brancharea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(4,1),brancharea.getRCArea(),new NAlias("Board"),2));

        NUtils.getGameUI().msg("Please, select area for block");
        SelectArea bougharea = new SelectArea(Resource.loadsimg("baubles/blockIng"));
        bougharea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,2),bougharea.getRCArea(),new NAlias("Block"),2));

        NUtils.getGameUI().msg("Please, select area for straw");
        SelectArea strawarea = new SelectArea(Resource.loadsimg("baubles/blockIng"));
        strawarea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,1),strawarea.getRCArea(),new NAlias("Straw"),10));

        NUtils.getGameUI().msg("Please, select area for bee larvae");
        SelectArea larvaearea = new SelectArea();
        larvaearea.run(NUtils.getGameUI());
        command.ingredients.add(new Build.Ingredient(new Coord(1,1),larvaearea.getRCArea(),new NAlias("Bee Larvae"),3));

        new Build(command, buildarea.getRCArea()).run(gui);
        return Results.SUCCESS();
    }
}
