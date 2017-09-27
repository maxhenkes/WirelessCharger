package info.creepershift.wificharge.client.gui;

import info.creepershift.wificharge.block.tile.TilePersonalCharger;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import info.creepershift.wificharge.inventory.ContainerPersonalCharger;
import info.creepershift.wificharge.inventory.ContainerWirelessCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case 1:
                return new ContainerWirelessCharger(player, (TileWirelessCharger) te);
            case 2:
                return new ContainerPersonalCharger(player, (TilePersonalCharger) te);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case 1:
                return new GuiWirelessCharger(player, (TileWirelessCharger) te);
            case 2:
                return new GuiPersonalCharger(player, (TilePersonalCharger) te);
            default:
                return null;
        }
    }
}
