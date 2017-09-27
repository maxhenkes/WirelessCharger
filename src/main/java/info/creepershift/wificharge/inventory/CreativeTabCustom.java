package info.creepershift.wificharge.inventory;

import info.creepershift.wificharge.item.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCustom {

    public static CreativeTabs customTab;

    private CreativeTabCustom(){}

    public static void registerTab(){

        customTab = new CreativeTabs("creativetab") {
            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {
                return new ItemStack(ItemRegistry.itemUpgradeRange);
            }
        };
    }

}
