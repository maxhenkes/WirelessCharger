package info.creepershift.wificharge.config;

import info.creepershift.wificharge.CommonProxy;
import info.creepershift.wificharge.Main;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    //CATEGORIES
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_WIRELESS_CHARGER = "wirelesscharger";
    private static final String CATEGORY_PERSONAL_CHARGER = "personalcharger";


    //VALUES
    public static int wirelessRangeEnergyRate = 66;
    public static int wirelessRange = 30;
    public static int conversionRate = 4;
    public static int blockCapacity = 5000000;
    public static int maxOutput = 250000;
    public static int maxInput = 250000;
    public static int dimensionRate = 50;
    public static int rangeRate = 5;
    public static int personalMaxOutput = 100000;
    public static int personalMaxInput = 100000;
    public static int personalCapacity = 2000000;

    public static boolean wirelessRangeEnergy = true;
    public static boolean ignoreBlocks = true;
    public static boolean allowIC2 = false;
    public static boolean isPerformance = false;
    public static boolean transferDimension = true;
    public static boolean dimensionCost = true;
    public static boolean personalRangeCost = true;
    public static boolean rangeHardLimit = true;


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

        /*
        General Category
         */

        config.addCustomCategoryComment(CATEGORY_GENERAL, "general");
        allowIC2 = config.getBoolean("allowIC2", CATEGORY_GENERAL, allowIC2, "Allow IC2 items to be charged with RF. Does nothing without IC2");
        conversionRate = config.getInt("conversionRate", CATEGORY_GENERAL, conversionRate, 1, Integer.MAX_VALUE, "How much RF is needed to create 1 EU");
        ignoreBlocks = config.getBoolean("ignoreBlocks", CATEGORY_WIRELESS_CHARGER, ignoreBlocks, "Only charge items and ignore blocks? Fixes energy blocks in your inventory being charged and unable to be used for crafting in some mods.");


        /*
        Wireless Charger
         */

        config.addCustomCategoryComment(CATEGORY_WIRELESS_CHARGER, "Wireless Charger");
        wirelessRangeEnergy = config.getBoolean("wirelessRangeEnergy", CATEGORY_WIRELESS_CHARGER, wirelessRangeEnergy, "Set to false to disable wireless charger to draw additional power the further you are away");
        wirelessRangeEnergyRate = config.getInt("wirelessRangeEnergyModifier", CATEGORY_WIRELESS_CHARGER, wirelessRangeEnergyRate, 1, 5000, "The modifier that determines the charge cost, (cost * (1+(MODIFIER/1000)*range)). Cost is always 1:1 when the above is disabled.");
        wirelessRange = config.getInt("wirelessRange", CATEGORY_WIRELESS_CHARGER, wirelessRange, 1, 128, "Charge radius");
        blockCapacity = config.getInt("blockCapacity", CATEGORY_WIRELESS_CHARGER, blockCapacity, 1, Integer.MAX_VALUE, "Internal Storage of the charger in RF");
        maxOutput = config.getInt("maxOutput", CATEGORY_WIRELESS_CHARGER, maxOutput, 1, Integer.MAX_VALUE, "Max output of the charger, determines the absolute maximum items can receive per tick (Items might only allow less rf/t)");
        maxInput = config.getInt("maxInput", CATEGORY_WIRELESS_CHARGER, maxInput, 1, Integer.MAX_VALUE, "Max input of the charger in RF/t");
        isPerformance = config.getBoolean("isPerformance", CATEGORY_WIRELESS_CHARGER, isPerformance, "Enable this if you have many many wireless chargers on a server and they are causing lag. Should be fine for regular servers and single player. Only enable this if you experience lag coming from the chargers. This setting will reduce the charge from once per tick to once per second. It will try to offset the reduced charge by charging 20x the amount every second but most items cannot recieve that much power, so charge times will be about 20 times slower!");

        /*
        Personal Charger
         */

        config.addCustomCategoryComment(CATEGORY_PERSONAL_CHARGER, "Personal Charger");
        transferDimension = config.getBoolean("transferDimension", CATEGORY_PERSONAL_CHARGER, transferDimension, "Allow the personal charger to charge your items in another dimension.");
        dimensionCost = config.getBoolean("dimensionCost", CATEGORY_PERSONAL_CHARGER, dimensionCost, "Extra cost when charging in another dimension?");
        personalRangeCost = config.getBoolean("personalRangeCost", CATEGORY_PERSONAL_CHARGER, personalRangeCost, "Calculate extra cost based on range to the personal charger?");
        dimensionRate = config.getInt("dimensionRate", CATEGORY_PERSONAL_CHARGER, dimensionRate, 1, Integer.MAX_VALUE, "Increase cost by % when in another dimension. Range cost is not applied in another dimension.");
        rangeRate = config.getInt("rangeRate", CATEGORY_PERSONAL_CHARGER, rangeRate, 1, Integer.MAX_VALUE, "The modifier that determines the charge cost, (cost * (1+(MODIFIER/1000) * (range/100))) Charge cost will only increase for every 100 blocks.");
        rangeHardLimit = config.getBoolean("rangeHardLimit", CATEGORY_PERSONAL_CHARGER, rangeHardLimit, "Disable this if you want to increase the charge cost to a point where the charger will not be able to charge the item anymore because the charge cost is higher than the maxOutput after a huge range");
        personalMaxOutput = config.getInt("personalMaxOutput", CATEGORY_PERSONAL_CHARGER, personalMaxOutput, 1, Integer.MAX_VALUE, "Personal Charger internal storage in RF");
        personalMaxInput = config.getInt("personalMaxInput", CATEGORY_PERSONAL_CHARGER, personalMaxInput, 1, Integer.MAX_VALUE, "Personal Charger max output in RF/t");
        personalCapacity = config.getInt("personalCapacity", CATEGORY_PERSONAL_CHARGER, personalCapacity, 1, Integer.MAX_VALUE, "Personal Charger max input in RF/t");

    }

}
