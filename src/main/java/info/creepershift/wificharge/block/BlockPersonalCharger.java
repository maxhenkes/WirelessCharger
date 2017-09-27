package info.creepershift.wificharge.block;

import info.creepershift.wificharge.Reference;
import info.creepershift.wificharge.block.tile.TilePersonalCharger;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockPersonalCharger extends BlockBase implements ITileEntityProvider {

    public BlockPersonalCharger() {
        super(Material.ROCK);
        setUnlocalizedName(Reference.MODID + ".personalcharger");
        setRegistryName("personalcharger");
        setHardness(3);
        setHarvestLevel("pickaxe", 0);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePersonalCharger();
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TilePersonalCharger && !worldIn.isRemote) {
            ((TilePersonalCharger) tile).setRedstone(worldIn.isBlockPowered(pos));
            if (placer instanceof EntityPlayerMP) {
                ((TilePersonalCharger) tile).setPlayer((EntityPlayerMP) placer);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {

        return side != null && side != EnumFacing.DOWN && side != EnumFacing.UP;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TilePersonalCharger) {
            if (!worldIn.isRemote) {
                ((TilePersonalCharger) tile).setRedstone(worldIn.isBlockPowered(pos));
            }
        }
    }

}
