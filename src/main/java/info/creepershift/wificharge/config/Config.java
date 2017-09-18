package info.creepershift.wificharge.config;

import info.creepershift.wificharge.CommonProxy;
import info.creepershift.wificharge.Main;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    //CATEGORIES
    private static final String CATEGORY_GENERAL = "general";

    //VALUES
    public static int wirelessRangeEnergyRate = 66;
    public static int wirelessRange = 30;
    public static int conversionRate = 4;
    public static int blockCapacity = 5000000;
    public static int maxOutput = 250000;
    public static int maxInput = 250000;

    public static boolean wirelessRangeEnergy = true;
    public static boolean ignoreBlocks = true;
    public static boolean allowIC2 = false;
    public static boolean isPerformance = false;


    /*
    TODO:
    Add more config categories.
     */


    public static void readConfig() {
        Configuration config = CommonProxy.config;

        try {
            config.load();
            initGeneralConfig(config);

        } catch (Exception e1) {
            Main.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration config) {

        config.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        wirelessRangeEnergy = config.getBoolean("wirelessRangeEnergy", CATEGORY_GENERAL, wirelessRangeEnergy, "Set to false to disable wireless charger to draw additional power the further you are away");
        wirelessRangeEnergyRate = config.getInt("wirelessRangeEnergyModifier", CATEGORY_GENERAL, wirelessRangeEnergyRate, 1, 5000,"The modifier that determines the charge cost, (cost * (1+(MODIFIER/1000)*range)). Cost is always 1:1 when the above is disabled.");
        wirelessRange = config.getInt("wirelessRange", CATEGORY_GENERAL, wirelessRange, 1, 128,"Charge radius");
        ignoreBlocks = config.getBoolean("ignoreBlocks", CATEGORY_GENERAL, ignoreBlocks, "Only charge items and ignore blocks? Fixes energy blocks in your inventory being charged and unable to be used for crafting in some mods.");
        allowIC2 = config.getBoolean("allowIC2", CATEGORY_GENERAL, allowIC2, "Allow IC2 items to be charged with RF. Does nothing without IC2");
        conversionRate = config.getInt("conversionRate", CATEGORY_GENERAL, conversionRate, 1, Integer.MAX_VALUE,"How much RF is needed to create 1 EU");
        blockCapacity = config.getInt("blockCapacity", CATEGORY_GENERAL, blockCapacity, 1, Integer.MAX_VALUE, "Internal Storage of the charger in RF");
        maxOutput = config.getInt("maxOutput", CATEGORY_GENERAL, maxOutput, 1, Integer.MAX_VALUE, "Max output of the charger, determines the absolute maximum items can receive per tick (Items might only allow less rf/t)");
        maxInput = config.getInt("maxInput", CATEGORY_GENERAL, maxInput, 1, Integer.MAX_VALUE, "Max input of the charger in RF/t");
        isPerformance = config.getBoolean("isPerformance", CATEGORY_GENERAL, isPerformance, "Enable this if you have many many wireless chargers on a server and they are causing lag. Should be fine for regular servers and single player. Only enable this if you experience lag coming from the chargers. This setting will reduce the charge from once per tick to once per second. It will try to offset the reduced charge by charging 20x the amount every second but most items cannot recieve that much power, so charge times will be about 20 times slower!");


    }


}
