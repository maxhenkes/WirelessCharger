package info.creepershift.wificharge.util.compat;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxContainerItem;
import net.minecraft.item.ItemStack;

public class IECompat {

    public static boolean hasIE(ItemStack stack) {

        return stack.getItem() instanceof IFluxContainerItem;
    }

    public static int chargeItem(ItemStack stack, int energy, boolean simulate) {

        IFluxContainerItem item = (IFluxContainerItem) stack.getItem();
        return item.receiveEnergy(stack, energy, simulate);
    }


}
