package nurgling.actions;

import haven.*;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.tasks.*;
import nurgling.tools.NAlias;

import java.util.ArrayList;

public class TransferToBarrel implements Action{

    Gob barrel;
    NAlias items;

    int th = 9500;

    double total = 0;
    public TransferToBarrel(Gob barrel, NAlias items) {
        this.barrel = barrel;
        this.items = items;
    }

    public TransferToBarrel(Gob barrel, NAlias items, int th) {
        this(barrel, items);
        this.th = th;
    }

    @Override
    public Results run(NGameUI gui) throws InterruptedException {

        if(barrel==null){
            return Results.ERROR("NULL BARREL");
        }
        new PathFinder( barrel ).run (gui);
        if ( !(new OpenTargetContainer (  "Barrel",barrel ).run ( gui ).isSuccess) ) {
            return Results.ERROR("OPEN FAIL");
        }
        Pair<Double, String> barrelConts= gui.getBarrelContentMod(new NAlias(""));
        double barrelCont;
        if (barrelConts.b.equals("kg")){
            barrelCont = barrelConts.a * 100;
        }else{
            barrelCont = barrelConts.a;
        }
        NUtils.getGameUI().msg("В бочке: " + barrelCont + " из: " + th);
        total+=barrelCont;
        if (barrelCont > -1 && barrelCont < th) {

            ArrayList<WItem> witems = gui.getInventory().getItems(items);
            ArrayList<WItem> targetItems = new ArrayList<>();
            int sum = 0;
            for (WItem item : witems) {
                if (sum + barrelCont > th) {
                    break;
                }

                boolean added = false;
                for (ItemInfo inf : item.item.info) {
                    if (inf instanceof GItem.Amount) {
                        sum += ((GItem.Amount) inf).itemnum();
                        targetItems.add(item);
                        added = true;
                        if (sum + barrelCont > th) {
                            break;
                        }
                    }
                }
                if (!added) {
                    double pileAmount = ((NGItem) item.item).getPileAmount();
                    if (pileAmount > 0) {
                        sum += pileAmount * 100;
                        targetItems.add(item);
                        if (sum + barrelCont > th) {
                            break;
                        }
                    }
                }
            }
            total+=sum;

            if(!targetItems.isEmpty()) {
                NUtils.takeItemToHand(targetItems.get(0));
                if(witems.size() == targetItems.size()) {
                    if(barrelCont == 0)
                    {
                        NUtils.activateItem(barrel, true);
                        if (targetItems.size()>1) {
                            NUtils.getUI().core.addTask(new NotThisInHand(NUtils.getGameUI().vhand));
                        }
                    }
                    NUtils.dropsame(barrel);
                    NUtils.getUI().core.addTask(new WaitItems(NUtils.getGameUI().getInventory(), items, 0));
                }
                else
                {
                    for (int i = 0; i < targetItems.size(); i++) {
                        NUtils.activateItem(barrel, true);
                        if (i + 1 < targetItems.size()) {
                            NUtils.getUI().core.addTask(new NotThisInHand(NUtils.getGameUI().vhand));
                        }
                    }
                    NUtils.getUI().core.addTask(new WaitItems(NUtils.getGameUI().getInventory(), items, witems.size() - targetItems.size() - 1));


                    if (NUtils.getGameUI().vhand != null ) {
                        NUtils.getUI().core.addTask(new WaitItemInHand());
                        gui.getInventory().dropOn(gui.getInventory().findFreeCoord(NUtils.getGameUI().vhand));
                    }
                }
            }
        }
        return Results.SUCCESS();
    }

    public boolean isFull()
    {
        return total>th;
    }
}
