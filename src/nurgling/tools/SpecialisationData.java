package nurgling.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SpecialisationData {
    public final static HashMap<String, ArrayList<String>> data = new HashMap<>();
    static
    {
        ArrayList<String> crops = new ArrayList<>(Arrays.asList("Flax", "Turnip", "Carrot", "Hemp"));
        data.put("crop",crops);
        data.put("seed",crops);
        ArrayList<String> fuel = new ArrayList<>(Arrays.asList("Branch", "Coal", "Block"));
        data.put("fuel",fuel);
        ArrayList<String> barrel = new ArrayList<>(Arrays.asList("Quicksilver"));
        data.put("barrel",barrel);
    }
}
