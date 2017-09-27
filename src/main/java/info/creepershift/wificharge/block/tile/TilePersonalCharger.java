package info.creepershift.wificharge.block.tile;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import info.creepershift.wificharge.Main;
import info.creepershift.wificharge.config.Config;
import info.creepershift.wificharge.util.EnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
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
    private GameProfile playerProfile;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;
    public static final int SIZE = 3;

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
                            stored = (int) (stored * 0.1f);
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
                            cost = (int) (cost / 0.1f);
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
//        playerProfile = updateGameprofile(player.getGameProfile());
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

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(getUpdatePacket().getNbtCompound());
    }

    public GameProfile getProfile() {
        return this.playerProfile;
    }

    public static void setProfileCache(PlayerProfileCache profileCacheIn)
    {
        profileCache = profileCacheIn;
        Main.logger.info("PROFILE CACHE FOUND");
    }

    public static void setSessionService(MinecraftSessionService sessionServiceIn)
    {
        sessionService = sessionServiceIn;
        Main.logger.info("SESSION SERVER CONTACTED");
    }

    public GameProfile updateGameprofile(GameProfile input)
    {
        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
        {
            if (input.isComplete() && input.getProperties().containsKey("textures"))
            {
                return input;
            }
            else if (profileCache != null && sessionService != null)
            {
                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());

                if (gameprofile == null)
                {
                    return input;
                }
                else
                {
                    Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);

                    if (property == null)
                    {
                        gameprofile = sessionService.fillProfileProperties(gameprofile, true);
                    }

                    return gameprofile;
                }
            }
            else
            {
                return input;
            }
        }
        else
        {
            return input;
        }
    }


}
