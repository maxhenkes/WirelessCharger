package info.creepershift.wificharge.client.gui;

import info.creepershift.wificharge.Reference;
import info.creepershift.wificharge.block.tile.ForgeEnergyImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class EnergyDisplay extends Gui {

    private ForgeEnergyImpl storage;
    private int x;
    private int y;

    private final ResourceLocation location = new ResourceLocation(Reference.MODID, "textures/gui/gui_energy.png");

    public EnergyDisplay(int x, int y, ForgeEnergyImpl storage) {
        this.setData(x, y, storage);
    }

    public void setData(int x, int y, ForgeEnergyImpl storage) {
        this.x = x;
        this.y = y;
        this.storage = storage;
    }

    public void draw() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(location);

        int barX = this.x;
        int barY = this.y;

        this.drawTexturedModalRect(barX, barY, 16, 0, 26, 74);

        if (this.storage.getEnergyStored() > 0) {
            int i = this.storage.getEnergyStored() * 64 / this.storage.getMaxEnergyStored();

            float[] color = getWheelColor(mc.world.getTotalWorldTime() % 256);
            GlStateManager.color(color[0] / 255F, color[1] / 255F, color[2] / 255F);
            this.drawTexturedModalRect(barX + 5, barY + 69 - i, 0, 0, 16, i);
            GlStateManager.color(1F, 1F, 1F);
        }
    }

    public void drawOverlay(int mouseX, int mouseY) {
        if (this.isMouseOver(mouseX, mouseY)) {
            Minecraft mc = Minecraft.getMinecraft();

            List<String> text = new ArrayList<String>();
            text.add(this.getOverlayText());
            GuiUtils.drawHoveringText(text, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + 18 && mouseY < this.y + 85;
    }

    private String getOverlayText() {
        NumberFormat format = NumberFormat.getInstance();
        return String.format("%s/%s RF", format.format(this.storage.getEnergyStored()), format.format(this.storage.getMaxEnergyStored()));
    }

    public static float[] getWheelColor(float pos) {
        if (pos < 85.0f) {
            return new float[]{pos * 3.0F, 255.0f - pos * 3.0f, 0.0f};
        }
        if (pos < 170.0f) {
            return new float[]{255.0f - (pos -= 85.0f) * 3.0f, 0.0f, pos * 3.0f};
        }
        return new float[]{0.0f, (pos -= 170.0f) * 3.0f, 255.0f - pos * 3.0f};
    }

}
