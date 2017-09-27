package info.creepershift.wificharge.item;

import info.creepershift.api.item.IUpgradeItem;
import info.creepershift.wificharge.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgradeRange extends ItemBase implements IUpgradeItem {

    public ItemUpgradeRange() {
        setUnlocalizedName(Reference.MODID + ".itemupgrade_range");
        setRegistryName("itemupgrade_range");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
