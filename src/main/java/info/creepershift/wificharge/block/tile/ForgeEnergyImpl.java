package info.creepershift.wificharge.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class ForgeEnergyImpl extends EnergyStorage {


    public ForgeEnergyImpl(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.setEnergyStored(compound.getInteger("Energy"));
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Energy", this.getEnergyStored());
    }


    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public int extractEnergyFast(int maxExtract, boolean simulate, boolean ignoreChargeLimit) {
        if (!canExtract())
            return 0;

        int actualExtract = ignoreChargeLimit ? maxExtract : Math.min(this.maxExtract, maxExtract);

        int energyExtracted = Math.min(energy, actualExtract);

        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

}
