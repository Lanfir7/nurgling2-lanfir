package nurgling.tasks;

import haven.*;
import static haven.OCache.posres;
import nurgling.*;
import static nurgling.actions.PathFinder.pfmdelta;
import nurgling.tools.*;

public class IsMoving implements NTask
{

    Coord2d coord;

    public IsMoving(Coord2d coord)
    {
        this.coord = coord;
    }

    @Override
    public boolean check()
    {
        if (NUtils.getGameUI() != null && NUtils.getGameUI().map != null && NUtils.getGameUI().map.player() != null)
        {
            if (NUtils.getGameUI().map.player().rc.dist(coord) <= pfmdelta)
                return true;
            Drawable drawable = (Drawable) NUtils.getGameUI().map.player().getattr(Drawable.class);
            if (drawable != null)
            {
                String pose;
                return drawable instanceof Composite && (pose = ((Composite) drawable).current_pose) != null && NParser.checkName(pose, "borka/walking", "borka/running");
            }
        }
        return false;
    }
}