package info.creepershift.wificharge.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {

    private static int packetID = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public PacketHandler(){}

    public static int nextID(){
        return packetID++;
    }

    public static void registerMessages(String channelName){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages(){
        //Register messages which are sent from the client to the server here:
        //INSTANCE.registerMessage(PacketSendKey.Handler.class, PacketSendKey.class, nextID(), Side.SERVER);
    }

}
