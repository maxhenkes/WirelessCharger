package info.creepershift.wificharge;

import info.creepershift.wificharge.block.BlockPersonalCharger;
import info.creepershift.wificharge.block.BlockRegistry;
import info.creepershift.wificharge.block.BlockWirelessCharger;
import info.creepershift.wificharge.block.tile.TilePersonalCharger;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import info.creepershift.wificharge.config.Config;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockWirelessCharger());
        event.getRegistry().register(new BlockPersonalCharger());

        GameRegistry.registerTileEntity(TileWirelessCharger.class, Reference.MODID + "_wirelesschargertile");
        GameRegistry.registerTileEntity(TilePersonalCharger.class, Reference.MODID + "_personalchargertile");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(BlockRegistry.blockWirelessCharger).setRegistryName(BlockRegistry.blockWirelessCharger.getRegistryName()));
        event.getRegistry().register(new ItemBlock(BlockRegistry.blockPersonalCharger).setRegistryName(BlockRegistry.blockPersonalCharger.getRegistryName()));
    }

    public void preInit(FMLPreInitializationEvent event) {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "wificharge.cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent event) {
    }


    public void postInit(FMLPostInitializationEvent event) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
