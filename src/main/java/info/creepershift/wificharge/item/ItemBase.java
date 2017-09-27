package info.creepershift.wificharge.item;

import info.creepershift.wificharge.inventory.CreativeTabCustom;
import net.minecraft.item.Item;

public abstract class ItemBase extends Item {

    public ItemBase() {
        setCreativeTab(CreativeTabCustom.customTab);
    }

}
