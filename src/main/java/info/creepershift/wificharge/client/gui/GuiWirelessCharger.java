package info.creepershift.wificharge.client.gui;

import info.creepershift.wificharge.Reference;
import info.creepershift.wificharge.block.tile.TileEntityBase;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import info.creepershift.wificharge.inventory.ContainerWirelessCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWirelessCharger extends GuiBase {

    public static final int WIDTH = 256;
    public static final int HEIGHT = 256;
    private final TileWirelessCharger tileWirelessCharger;
    private static final ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/gui_wireless.png");

    public GuiWirelessCharger(EntityPlayer player, TileEntityBase tile){
        super(new ContainerWirelessCharger(player, tile));
        tileWirelessCharger = (TileWirelessCharger) tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
