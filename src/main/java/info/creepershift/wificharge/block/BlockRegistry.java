package info.creepershift.wificharge.block;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {

    @GameRegistry.ObjectHolder("wificharge:wirelesscharger")
    public static BlockWirelessCharger blockWirelessCharger;

    @GameRegistry.ObjectHolder("wificharge:personalcharger")
    public static BlockPersonalCharger blockPersonalCharger;

}
