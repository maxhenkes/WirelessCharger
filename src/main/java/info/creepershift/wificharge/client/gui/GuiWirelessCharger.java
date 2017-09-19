package info.creepershift.wificharge.client.gui;

import info.creepershift.wificharge.Main;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import net.minecraft.client.gui.GuiScreen;

public class GuiWirelessCharger extends GuiScreen{

    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    public GuiWirelessCharger(TileWirelessCharger tileEntity){
        super();
        width = WIDTH;
        height = HEIGHT;
        Main.logger.info("GUI");
    }
}
