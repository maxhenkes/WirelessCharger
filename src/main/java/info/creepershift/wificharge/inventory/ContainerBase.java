package info.creepershift.wificharge.inventory;

import info.creepershift.api.machine.IUpgradableContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerBase extends Container implements IUpgradableContainer {

    @Override
    public void addUpgradeSlots(ItemStackHandler inventory) {

        int x = 152;
        int y = 8;

        // Add our own slots
        int slotIndex = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(inventory, slotIndex, x, y));
            slotIndex++;
            y += 18;
        }

    }


    public void addPlayerSlots(IInventory playerInventory) {
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
