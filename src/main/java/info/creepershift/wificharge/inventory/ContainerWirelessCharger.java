package info.creepershift.wificharge.inventory;

import info.creepershift.wificharge.block.tile.TileEntityBase;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerWirelessCharger extends ContainerBase {

    private final TileWirelessCharger tileWirelessCharger;

    public ContainerWirelessCharger(final EntityPlayer player, TileEntityBase tile) {

        tileWirelessCharger = (TileWirelessCharger) tile;
        InventoryPlayer inventory = player.inventory;
        addPlayerSlots(inventory);
        addUpgradeSlots(((TileWirelessCharger) tile).getUpgradeStackHandler());
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileWirelessCharger.canInteractWith(playerIn);
    }

}
