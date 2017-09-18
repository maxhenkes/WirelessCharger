package info.creepershift.wificharge.util.compat;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;

public class TeslaCompat {

    public static boolean hasTesla(ItemStack stack) {

        return stack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
    }

    public static int chargeItem(ItemStack stack, int energy, boolean simulate) {
        ITeslaConsumer cap = stack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
        return (int) cap.givePower(energy, simulate);
    }

}
