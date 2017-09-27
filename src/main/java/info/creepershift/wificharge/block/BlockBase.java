package info.creepershift.wificharge.block;

import info.creepershift.wificharge.inventory.CreativeTabCustom;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockBase extends Block {

    public BlockBase(Material materialIn) {
        super(materialIn);
        setCreativeTab(CreativeTabCustom.customTab);
    }
}
