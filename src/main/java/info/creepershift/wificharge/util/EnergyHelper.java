package info.creepershift.wificharge.util;

import info.creepershift.wificharge.Main;
import info.creepershift.wificharge.config.Config;
import info.creepershift.wificharge.util.compat.FluxCompat;
import info.creepershift.wificharge.util.compat.IC2Compat;
import info.creepershift.wificharge.util.compat.IECompat;
import info.creepershift.wificharge.util.compat.TeslaCompat;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHelper {


    private EnergyHelper() {
    }

    /**
     *
     * @param stack Inputstack
     * @param energy Energy the item should receive
     * @param simulate If true will only simulate
     * @return The amount of energy accepted
     */
    public static int chargeItem(ItemStack stack, int energy, boolean simulate) {

        return chargeItem(stack, energy, simulate, 4, false);
    }

    /**
     *
     * @param stack Inputstack
     * @param energy Energy the item should receive
     * @param simulate If true will only simulate
     * @param tier IC2 charge tier
     * @param ignoreChargeLimit IC2 ignore charge limit
     * @return The amount of energy accepted
     */
    public static int chargeItem(ItemStack stack, int energy, boolean simulate, int tier, boolean ignoreChargeLimit) {

        if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
            return cap.receiveEnergy(energy, simulate);
        } else if (Main.teslaLoaded && TeslaCompat.hasTesla(stack)) {
            return TeslaCompat.chargeItem(stack, energy, simulate);
        } else if (Main.redstonefluxLoaded && FluxCompat.hasFlux(stack)) {
            return FluxCompat.chargeItem(stack, energy, simulate);
        } else if (Main.immersiveLoaded && IECompat.hasIE(stack)) {
            return IECompat.chargeItem(stack, energy, simulate);
        } else if (Main.ic2Loaded && Config.allowIC2 &&  IC2Compat.hasIC2(stack)) {
            return IC2Compat.chargeItem(stack, energy, simulate, tier, ignoreChargeLimit);
        }
        return 0;
    }

    /**
    @param stack Inputstack
    @return If item can be charged
     */
    public static boolean isItemValid(ItemStack stack) {

        return stack.hasCapability(CapabilityEnergy.ENERGY, null) ||
                (Main.teslaLoaded && TeslaCompat.hasTesla(stack)) ||
                (Main.redstonefluxLoaded && FluxCompat.hasFlux(stack)) ||
                (Main.immersiveLoaded && IECompat.hasIE(stack)) ||
                (Main.ic2Loaded && Config.allowIC2 && IC2Compat.hasIC2(stack));

    }

}
