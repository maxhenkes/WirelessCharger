package info.creepershift.wificharge.inventory;

import info.creepershift.api.machine.IUpgradableContainer;
import net.minecraft.inventory.Container;
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
}
