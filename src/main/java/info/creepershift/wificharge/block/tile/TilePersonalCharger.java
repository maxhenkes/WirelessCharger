package info.creepershift.wificharge.block.tile;

import info.creepershift.wificharge.config.Config;
import info.creepershift.wificharge.util.EnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TilePersonalCharger extends TileEntityBase implements ITickable {

    private final ForgeEnergyImpl storage = new ForgeEnergyImpl(Config.personalCapacity, Config.personalMaxInput, Config.personalMaxOutput);
    private boolean hasRedstone = false;
    private UUID playerUUID;
    private int hardLimit;

    public TilePersonalCharger() {
        hardLimit = (int) (Config.personalMaxOutput * 0.1f);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            EntityPlayerMP p = getPlayer();
            if (!hasRedstone && p != null && storage.getEnergyStored() >= 5000) {
                if (Config.transferDimension || p.getEntityWorld() == world) {
                    chargeItems(p);
                }
            }
        }
    }

    private void chargeItems(EntityPlayerMP player) {
        List<NonNullList<ItemStack>> list = Arrays.asList(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory);

        for (NonNullList<ItemStack> inventoryList : list) {
            for (ItemStack stack : inventoryList) {
                if (isItemValid(stack)) {

                    int maxOut = Config.personalMaxOutput;
                    int stored = storage.getEnergyStored();
                    boolean reachedLimit = false;
                    boolean otherWorldCost = Config.dimensionCost && player.getEntityWorld() != world;

                    if (Config.personalRangeCost && !otherWorldCost) {
                        maxOut = (int) Math.floor(outputByRange(player, Config.personalMaxOutput, getPos()));
                        stored = (int) Math.floor(outputByRange(player, stored, getPos()));

                        if (Config.rangeHardLimit && maxOut < hardLimit) {
                            maxOut = hardLimit;
                            stored = (int)(stored * 0.1f);
                            reachedLimit = true;
                        }

                    } else if (otherWorldCost) {
                        maxOut = (int) Math.floor(outputByDimension(Config.personalMaxOutput));
                        stored = (int) Math.floor(outputByDimension(stored));
                    }

                    int energy = Math.min(EnergyHelper.chargeItem(stack, maxOut, true), stored);
                    int cost = energy;

                    if (energy > 0) {

                        if (reachedLimit) {
                            cost = (int)(cost/0.1f);
                        } else if (Config.personalRangeCost && !otherWorldCost) {
                            cost = (int) costByRange(player, energy, getPos());
                        } else if (otherWorldCost) {
                            cost = (int) Math.floor(costByDimension(energy));
                        }

                        if (cost <= storage.getEnergyStored()) {
                            EnergyHelper.chargeItem(stack, energy, false);
                            storage.extractEnergy(cost, false);
                        }
                    }

                }
            }
        }
    }

    private double outputByRange(EntityPlayer player, double energy, BlockPos pos) {

        BlockPos playerPos = player.getPosition();
        double distance = Math.floor(pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ()));

        return (energy / (1D + (Config.rangeRate / 1000D) * Math.max(1, distance / 100D)));
    }

    private double costByRange(EntityPlayer player, double cost, BlockPos pos) {

        BlockPos playerPos = player.getPosition();
        double distance = Math.floor(pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ()));

        return (cost * (1D + (Config.rangeRate / 1000D) * Math.max(1, distance / 100D)));
    }

    private double costByDimension(double cost) {

        return cost * (1D + (Config.dimensionRate / 100));
    }

    private double outputByDimension(double energy) {

        return energy / (1D + (Config.dimensionRate / 100));
    }

    private boolean isItemValid(ItemStack stack) {
        return stack != ItemStack.EMPTY && (!Config.ignoreBlocks || !(stack.getItem() instanceof ItemBlock) && EnergyHelper.isItemValid(stack));
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

    public void setRedstone(boolean redstone) {
        hasRedstone = redstone;
    }

    public void setPlayer(EntityPlayerMP player) {
        playerUUID = player.getUniqueID();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.readFromNBT(compound);
        playerUUID = compound.getUniqueId("Player");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        storage.writeToNBT(compound);
        compound.setUniqueId("Player", playerUUID);
        return compound;
    }

    private EntityPlayerMP getPlayer() {
        PlayerList pl = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        if (playerUUID != null) {
            return pl.getPlayerByUUID(playerUUID);
        }
        return null;
    }

}
