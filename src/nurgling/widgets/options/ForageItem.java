package nurgling.widgets.options;

public class ForageItem {
    private final String name;
    private final String invRes;
    private final int minVal;
    private final int maxVal;
    private final String location;
    private final int spring;
    private final int summer;
    private final int autumn;
    private final int winter;

    public ForageItem(String name, String invRes, int minVal, int maxVal, String location,
                      int spring, int summer, int autumn, int winter) {
        this.name = name;
        this.invRes = invRes;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.location = location;
        this.spring = spring;
        this.summer = summer;
        this.autumn = autumn;
        this.winter = winter;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getInvRes() {
        return invRes;
    }

    public int getMinVal() {
        return minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public String getLocation() {
        return location;
    }

    public int getSpring() {
        return spring;
    }

    public int getSummer() {
        return summer;
    }

    public int getAutumn() {
        return autumn;
    }

    public int getWinter() {
        return winter;
    }
}
