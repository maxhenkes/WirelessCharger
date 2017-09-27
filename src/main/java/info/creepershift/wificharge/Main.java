package info.creepershift.wificharge;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, version = Reference.VERSION, acceptedMinecraftVersions = "1.12,1.12.1, 1.12.2")
public class Main {

    @SidedProxy(clientSide = "info.creepershift.wificharge.ClientProxy", serverSide = "info.creepershift.wificharge.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Main instance;

    public static Logger logger;

    public static boolean teslaLoaded;
    public static boolean redstonefluxLoaded;
    public static boolean immersiveLoaded;
    public static boolean ic2Loaded;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        teslaLoaded = Loader.isModLoaded("tesla");
        redstonefluxLoaded = Loader.isModLoaded("redstoneflux");
        immersiveLoaded = Loader.isModLoaded("immersiveengineering");
        ic2Loaded = Loader.isModLoaded("ic2");
        proxy.preInit(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
