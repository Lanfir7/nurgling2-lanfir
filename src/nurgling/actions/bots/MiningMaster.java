package nurgling.actions.bots;

import haven.*;
import nurgling.NGItem;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.Action;
import nurgling.actions.Results;
import nurgling.tools.NAlias;
import nurgling.widgets.NEquipory;
import nurgling.widgets.bots.MiningMonitorWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiningMaster implements Action {

    private static final NAlias stones = new NAlias(new ArrayList<>(List.of(
            "Alabaster", "Apatite", "Zincspar", "Soapstone", "Sunstone", "Sodalite", "Black Coal",
            "Graywacke", "Slate", "Schist", "Chert", "Wine Glance", "Serpentine", "Arkose", "Basalt",
            "Bat Rock", "Black Ore", "Bloodstone", "Breccia", "Cassiterite", "Cat Gold", "Chalcopyrite",
            "Cinnabar", "Diabase", "Diorite", "Direvein", "Dolomite", "Dross", "Eclogite", "Feldspar",
            "Flint", "Fluorospar", "Gabbro", "Galena", "Gneiss", "Granite", "Greenschist", "Heavy Earth",
            "Horn Silver", "Hornblende", "Iron Ochre", "Jasper", "Korund", "Kyanite", "Lead Glance",
            "Leaf Ore", "Limestone", "Malachite", "Marble", "Meteorite", "Mica", "Microlite", "Olivine",
            "Orthoclase", "Peacock Ore", "Pegmatite", "Porphyry", "Pumice", "Quarryartz", "Quartz",
            "Rhyolite"
    )));

    private double topStoneQuality;
    private String topStoneName;
    private double topCatGoldQuality;
    private double topQuarryQuality;
    private int stoneCount;
    private double toolQuality;

    private MiningMonitorWindow monitorWindow;

    @Override
    public Results run(NGameUI gui) throws InterruptedException {
        // Reset all tracking variables at the start
        resetData();

        // Create the monitor window and add it to the UI
        monitorWindow = new MiningMonitorWindow();
        gui.add(monitorWindow, new Coord(300, 200));

        try {
            // Main loop
            while (true) {

                // Check if the window is closed
                if (monitorWindow != null && !monitorWindow.isRunning()) {
                    // Clean up and exit
                    monitorWindow.reqdestroy();
                    return Results.SUCCESS();
                }

                // Update the mining tool quality
                toolQuality = getMiningToolQuality(gui);

                // Get the main inventory
                Inventory inv = gui.maininv;
                if (inv == null) {
                    NUtils.getGameUI().msg("No main inventory found");
                    Thread.sleep(500);
                    continue;
                }

                boolean foundStone = false;

                // Get the items in the inventory
                for (Widget w = inv.child; w != null; w = w.next) {
                    if (w instanceof WItem) {
                        WItem witem = (WItem) w;
                        GItem gitem = witem.item;

                        // Get the item name
                        String itemName = ((NGItem) gitem).name();
                        // Check if the item matches the stones alias
                        if (itemName != null){
                        if (stones.check(itemName)) {
                            foundStone = true;

                            // Get the quality of the item
                            float itemQuality = ((NGItem) gitem).quality;
                            if (itemQuality > 1) {
                                double adjustedQuality = match(itemQuality);
                                double iq = Math.round(itemQuality * 10.0) / 10.0;
                                double caveq = adjustedQuality;

                                // Process based on the type of stone
                                if (itemName.contains("Quarryartz")) {
                                    // For Quarry Stone, use adjusted quality for display
                                    // Compare original quality (iq) with topQuarryQuality
                                    if (caveq > topQuarryQuality) {
                                        topQuarryQuality = caveq;
                                        // Update the monitor window
                                        monitorWindow.updateTopQuarry(caveq, iq);
                                    }
                                } else if (itemName.contains("Cat Gold")) {
                                    // For Cat Gold, use original item quality
                                    if (iq > topCatGoldQuality) {
                                        topCatGoldQuality = iq;
                                        monitorWindow.updateTopCatGold(topCatGoldQuality);
                                    }
                                } else {
                                    // For other stones, use original item quality
                                    if (caveq > topStoneQuality) {
                                        topStoneQuality = caveq;
                                        topStoneName = itemName;
                                        // Update the monitor window
                                        monitorWindow.updateTopStone(topStoneName, topStoneQuality);
                                    }
                                }

                                // Increment the count
                                stoneCount++;
                                monitorWindow.updateStoneCount(stoneCount);

                                // Discard the item
                                NUtils.drop(witem);

                                // Optional: Wait for item to be dropped
                                Thread.sleep(100);
                            }
                        }
                        }
                    }
                }

                // Update status in the monitor window
                String status = foundStone ? "Processing stones..." : "Waiting for stones...";
                monitorWindow.updateStatus(status);

                // Sleep for a short period to avoid busy waiting
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            // Clean up
            if (monitorWindow != null) {
                monitorWindow.reqdestroy();
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (monitorWindow != null) {
                monitorWindow.reqdestroy();
            }
            return Results.ERROR(e.getMessage());
        }
    }

    // Method to reset all tracking variables
    private void resetData() {
        topStoneQuality = 0.0;
        topStoneName = "None";
        topCatGoldQuality = 0.0;
        topQuarryQuality = 0.0;
        stoneCount = 0;
        toolQuality = 0.0;
    }

    private double getMiningToolQuality(NGameUI gui) throws InterruptedException {
        // Get equipment inventory
        Equipory equip = NUtils.getEquipment();
        if (equip == null) {
            return 0.0;
        }

        // Hands slots
        WItem leftHand = NUtils.getEquipment().findItem(NEquipory.Slots.HAND_LEFT.idx);
        WItem rightHand = NUtils.getEquipment().findItem(NEquipory.Slots.HAND_RIGHT.idx);

        // Check if the items are mining tools
        double toolQuality = 0.0;
        if (leftHand != null) {
            String itemName = ((NGItem) leftHand.item).name();
            if (itemName != null && (itemName.contains("Pickaxe") || itemName.contains("Stone Axe") || itemName.contains("Tinker's Throwing Axe"))) {
                float itemQuality = ((NGItem) leftHand.item).quality;
                if (itemQuality > toolQuality) {
                    toolQuality = itemQuality;
                }
            }
        }
        if (rightHand != null) {
            String itemName = ((NGItem) rightHand.item).name();
            if (itemName != null && (itemName.contains("Pickaxe") || itemName.contains("Stone Axe") || itemName.contains("Tinker's Throwing Axe"))) {
                float itemQuality = ((NGItem) rightHand.item).quality;
                if (itemQuality > toolQuality) {
                    toolQuality = itemQuality;
                }
            }
        }
        return toolQuality;
    }

    private double match(double q) {
        if (toolQuality > q) {
            return Math.round(q * 10.0) / 10.0;
        } else {
            return Math.round((q - toolQuality + q) * 10.0) / 10.0;
        }
    }
}
