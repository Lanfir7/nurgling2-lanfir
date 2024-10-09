package nurgling.tasks;

import haven.Gob;
import nurgling.NUtils;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

public class FilledPile implements NTask
{

    final Gob gob;
    final NAlias items;
    final int size;
    final int oldSize;
    Integer th = -1; // Для фильтрации по качеству

    public FilledPile(Gob gob, NAlias items, int size, int oldSize)
    {
        this.gob = gob;
        this.items = items;
        this.size = size;
        this.oldSize = oldSize;
    }
    public FilledPile(Gob gob, NAlias items, int size, int oldSize, Integer th)
    {
        this.gob = gob;
        this.items = items;
        this.size = size;
        this.oldSize = oldSize;
        this.th = th;
    }
    @Override
    public boolean check()
    {
        try {
            if (th == -1) {
                return NUtils.getGameUI().getInventory().getItems(items).size() == oldSize - size;
            } else {
                return NUtils.getGameUI().getInventory().getItems(items, th).size() == oldSize - size;
            }
        }
        catch (InterruptedException ignore)
        {
        }
        return false;
    }
}
