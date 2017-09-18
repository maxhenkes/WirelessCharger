package info.creepershift.wificharge.util.compat;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

public class FluxCompat {

    public static boolean hasFlux(ItemStack stack) {

        return stack.getItem() instanceof IEnergyContainerItem;
    }

    public static int chargeItem(ItemStack stack, int energy, boolean simulate) {

        IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
        return item.receiveEnergy(stack, energy, simulate);
    }


}
