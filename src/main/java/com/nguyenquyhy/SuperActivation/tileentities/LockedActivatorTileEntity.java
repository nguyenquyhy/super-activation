package com.nguyenquyhy.SuperActivation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LockedActivatorTileEntity extends TileEntity {
	public static final String publicName = "lockedActivatorTileEntity";

	public String itemUnlocalizedName;

	public static String getPublicname() {
		return publicName;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		writeSynableDataToNBT(tag);
		super.writeToNBT(tag);
	}

	private void writeSynableDataToNBT(NBTTagCompound tag) {
		if (itemUnlocalizedName != null)
			tag.setString("itemUnlocalizedName", itemUnlocalizedName);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		readSyncableDataFromNBT(tag);
		super.readFromNBT(tag);
	}

	private void readSyncableDataFromNBT(NBTTagCompound tag) {
		itemUnlocalizedName = tag.getString("itemUnlocalizedName");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSynableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.func_148857_g());
	}
}
