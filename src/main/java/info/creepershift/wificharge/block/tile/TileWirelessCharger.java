package info.creepershift.wificharge.block.tile;

import info.creepershift.wificharge.config.Config;
import info.creepershift.wificharge.util.EnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class TileWirelessCharger extends TileEntityBase implements ITickable {

    private final ForgeEnergyImpl storage = new ForgeEnergyImpl(Config.blockCapacity, Config.maxInput, Config.maxOutput);
    private int counter = 0;
    private boolean hasRedstone = false;

    public TileWirelessCharger() {
    }

    @Override
    public void update() {
        if (!hasRedstone && !world.isRemote && storage.getEnergyStored() >= 5000) {
            if (Config.isPerformance) {
                if (counter >= 20) {
                    getItems();
                    counter = 0;
                }
                counter++;
            } else {
                getItems();
            }
        }
    }

    private void getItems() {
        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos().add(-Config.wirelessRange, -Config.wirelessRange, -Config.wirelessRange), getPos().add(Config.wirelessRange, Config.wirelessRange, Config.wirelessRange)));

        for (EntityPlayer player : list) {
            chargeItems(Arrays.asList(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory), player);
        }

    }


    private void chargeItems(List<NonNullList<ItemStack>> list, EntityPlayer player) {
        for (NonNullList<ItemStack> inventoryList : list) {
            for (ItemStack stack : inventoryList) {

                if (isItemValid(stack)) {

                    int maxOut = Config.maxOutput;
                    int stored = storage.getEnergyStored();

                    if (Config.wirelessRangeEnergy) {
                        maxOut = (int) Math.floor(outputByRange(player, Config.maxOutput,getPos()));
                        stored = (int) Math.floor(outputByRange(player, stored, getPos()));
                    }

                    int energy = Math.min(EnergyHelper.chargeItem(stack, (Config.isPerformance ? maxOut * 20 : maxOut), true, 4, Config.isPerformance), stored);
                    int cost = energy;

                    if (energy > 0) {

                        if (Config.wirelessRangeEnergy) {
                            cost = (int) costByRange(player, energy, getPos());
                        }

                        if (cost <= storage.getEnergyStored()) {
                            EnergyHelper.chargeItem(stack, energy, false, 4, Config.isPerformance);
                            storage.extractEnergyFast(cost, false, Config.isPerformance);
                        }
                    }
                }
            }
        }
    }

    private boolean isItemValid(ItemStack stack) {
        return stack != ItemStack.EMPTY && (!Config.ignoreBlocks || !(stack.getItem() instanceof ItemBlock) && EnergyHelper.isItemValid(stack));
    }

    private double outputByRange(EntityPlayer player, double energy, BlockPos pos) {

        BlockPos playerPos = player.getPosition();
        double distance = Math.floor(pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ()));

        return (energy / (1D + (Config.wirelessRangeEnergyRate / 1000D) * distance));
    }

    private double costByRange(EntityPlayer player, double cost, BlockPos pos) {

        BlockPos playerPos = player.getPosition();
        double distance = Math.floor(pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ()));

        return (cost * (1D + (Config.wirelessRangeEnergyRate / 1000D) * distance));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }


    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) storage;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public void setRedstone(boolean redstone){
        hasRedstone = redstone;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        storage.writeToNBT(compound);
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }



}
