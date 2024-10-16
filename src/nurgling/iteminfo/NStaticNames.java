package nurgling.iteminfo;

import haven.*;
import haven.resutil.FoodInfo;
import nurgling.NGItem;
import nurgling.NUtils;
import nurgling.tools.NAlias;
import nurgling.tools.NSearchItem;
import nurgling.widgets.NCharacterInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static haven.BAttrWnd.Constipations.tflt;
import static haven.CharWnd.iconfilter;
import static haven.PUtils.convolvedown;
public class NStaticNames {


    public class RawMeat {
        public NAlias stones = new NAlias(new ArrayList<>(List.of(
                "Alabaster", "Apatite", "Arkose", "Basalt", "Bat Rock", "Black Coal", "Black Ore",
                "Bloodstone", "Breccia", "Cassiterite", "Cat Gold", "Chalcopyrite", "Chert", "Cinnabar",
                "Diabase", "Diorite", "Direvein", "Dolomite", "Dross", "Eclogite", "Feldspar", "Flint",
                "Fluorospar", "Gabbro", "Galena", "Gneiss", "Granite", "Graywacke", "Greenschist",
                "Heavy Earth", "Horn Silver", "Hornblende", "Iron Ochre", "Jasper", "Korund", "Kyanite",
                "Lava Rock", "Lead Glance", "Leaf Ore", "Limestone", "Malachite", "Marble", "Meteorite",
                "Mica", "Microlite", "Obsidian", "Olivine", "Orthoclase", "Peacock Ore", "Pegmatite",
                "Porphyry", "Pumice", "Quarryartz", "Quartz", "Rhyolite", "Rock Crystal", "Sandstone",
                "Schist", "Schrifterz", "Serpentine", "Shard of Conch", "Silvershine", "Slag", "Slate",
                "Soapstone", "Sodalite", "Sunstone", "Wine Glance", "Zincspar"
        )));
    }
}