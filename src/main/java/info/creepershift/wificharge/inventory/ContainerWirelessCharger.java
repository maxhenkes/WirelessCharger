package info.creepershift.wificharge.inventory;

import info.creepershift.wificharge.block.tile.TileEntityBase;
import info.creepershift.wificharge.block.tile.TileWirelessCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerWirelessCharger extends Container {

    private final TileWirelessCharger tileWirelessCharger;

    public ContainerWirelessCharger(final EntityPlayer player, TileEntityBase tile) {

        tileWirelessCharger = (TileWirelessCharger) tile;
        InventoryPlayer inventory = player.inventory;
        addPlayerSlots(inventory);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileWirelessCharger.canInteractWith(playerIn);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 84;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

}
