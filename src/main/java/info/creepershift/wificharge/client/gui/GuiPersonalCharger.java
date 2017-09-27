package info.creepershift.wificharge.client.gui;

import info.creepershift.wificharge.Reference;
import info.creepershift.wificharge.block.tile.TileEntityBase;
import info.creepershift.wificharge.block.tile.TilePersonalCharger;
import info.creepershift.wificharge.inventory.ContainerWirelessCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPersonalCharger extends GuiBase {

    public static final int WIDTH = 256;
    public static final int HEIGHT = 256;
    private final TilePersonalCharger tilePersonalCharger;
    private EnergyDisplay energy;
    private static final ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/gui_wireless.png");

    public GuiPersonalCharger(EntityPlayer player, TileEntityBase tile){
        super(new ContainerWirelessCharger(player, tile));
        tilePersonalCharger = (TilePersonalCharger) tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.energy = new EnergyDisplay(this.guiLeft + 5, this.guiTop +5 , this.tilePersonalCharger.storage);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        String range = "Bound to Player: " + tilePersonalCharger.getPlayerName();
        this.fontRenderer.drawString(range, this.guiLeft + this.xSize/2 - this.fontRenderer.getStringWidth(range)/2, this.guiTop + 8, 4210752);
        this.energy.drawOverlay(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        this.energy.draw();
    }

}
