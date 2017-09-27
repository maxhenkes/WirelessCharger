package info.creepershift.wificharge.inventory;

import info.creepershift.wificharge.block.tile.TileEntityBase;
import info.creepershift.wificharge.block.tile.TilePersonalCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerPersonalCharger extends ContainerBase {

    private final TilePersonalCharger tilePersonalCharger;

    public ContainerPersonalCharger(final EntityPlayer player, TileEntityBase tile) {

        tilePersonalCharger = (TilePersonalCharger) tile;
        InventoryPlayer inventory = player.inventory;
        addPlayerSlots(inventory);
        addUpgradeSlots(((TilePersonalCharger) tile).getUpgradeStackHandler());
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tilePersonalCharger.canInteractWith(playerIn);
    }

}
