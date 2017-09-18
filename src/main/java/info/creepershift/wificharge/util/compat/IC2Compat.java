package info.creepershift.wificharge.util.compat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import info.creepershift.wificharge.config.Config;
import net.minecraft.item.ItemStack;

public class IC2Compat {

    public static boolean hasIC2(ItemStack stack) {

        return (stack.getItem() instanceof IElectricItem || stack.getItem() instanceof ISpecialElectricItem);
    }

    public static int chargeItem(ItemStack stack, int energy, boolean simulate, int tier, boolean ignoreChargeLimit) {

        /*
        Converts the input energy to EU,
        calculates the cost and returns the accepted energy as RF.
         */

        return 4 * (int) getManager(stack).charge(stack, energy / Config.conversionRate, tier, ignoreChargeLimit, simulate);
    }

    private static IElectricItemManager getManager(ItemStack stack) {

        if (stack.getItem() instanceof ISpecialElectricItem) {
            ISpecialElectricItem item = (ISpecialElectricItem) stack.getItem();
            return item.getManager(stack);
        }

        return ElectricItem.manager;
    }

}
