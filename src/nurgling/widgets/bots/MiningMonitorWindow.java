package nurgling.widgets.bots;

import haven.*;
import nurgling.NGameUI;

public class MiningMonitorWindow extends Window {

    private double topStoneQuality = 0.0;
    private String topStoneName = "None";
    private double topCatGoldQuality = 0.0;
    private double topQuarryQuality = 0.0;
    private String topQuarryText = "";
    private int stoneCount = 0;

    private Label lblTopStone;
    private Label lblTopCatGold;
    private Label lblTopQuarry;
    private Label lblStoneCount;
    private Label lblStatus;
    private Button resetButton;

    private boolean isRunning = true;

    public MiningMonitorWindow() {
        super(new Coord(300, 170), "Mining Monitor");
        createMonitorWindow();
    }

    // Method to create the window with labels and reset button
    private void createMonitorWindow() {
        lblTopStone = new Label("Top Stone: None Q: 0.0");
        this.add(lblTopStone, new Coord(10, 10));

        lblTopCatGold = new Label("Top Cat Gold Q: 0.0");
        this.add(lblTopCatGold, new Coord(10, 30));

        lblTopQuarry = new Label("Top Quarry: Q: 0.0");
        this.add(lblTopQuarry, new Coord(10, 50));

        lblStoneCount = new Label("Stones Processed: 0");
        this.add(lblStoneCount, new Coord(10, 70));

        lblStatus = new Label("Status: Running");
        this.add(lblStatus, new Coord(10, 90));

        // Add the reset button at the bottom
        resetButton = new Button(100, "Reset") {
            @Override
            public void click() {
                resetData();
            }
        };
        this.add(resetButton, new Coord(10, 110));

        pack();
    }

    public void updateTopStone(String stoneName, double quality) {
        topStoneName = stoneName;
        topStoneQuality = quality;
        lblTopStone.settext("Top Stone: " + topStoneName + " Q: " + quality);
    }

    public void updateTopCatGold(double quality) {
        topCatGoldQuality = quality;
        lblTopCatGold.settext("Top Cat Gold Q: " + quality);
    }

    public void updateTopQuarry(double caveq, double iq) {
        topQuarryQuality = iq;
        topQuarryText = caveq + " = " + iq + " - Quarryartz";
        lblTopQuarry.settext("Top Quarry: " + topQuarryText);
    }

    public void updateStoneCount(int count) {
        stoneCount = count;
        lblStoneCount.settext("Stones Processed: " + stoneCount);
    }

    public void updateStatus(String status) {
        lblStatus.settext("Status: " + status);
    }

    // Method to reset all data
    private void resetData() {
        topStoneQuality = 0.0;
        topStoneName = "None";
        topCatGoldQuality = 0.0;
        topQuarryQuality = 0.0;
        topQuarryText = "";
        stoneCount = 0;

        lblTopStone.settext("Top Stone: None Q: 0.0");
        lblTopCatGold.settext("Top Cat Gold Q: 0.0");
        lblTopQuarry.settext("Top Quarry: Q: 0.0");
        lblStoneCount.settext("Stones Processed: 0");
        lblStatus.settext("Status: Data reset.");
    }

    public boolean isRunning() {
        return isRunning;
    }

    // Handler for window close action
    @Override
    public void wdgmsg(String msg, Object... args) {
        if (msg.equals("close")) {
            isRunning = false; // Stop the action when window is closed
            hide(); // Hide the window
        }
        super.wdgmsg(msg, args);
    }
}
