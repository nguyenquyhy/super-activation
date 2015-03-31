package com.nguyenquyhy.SuperActivation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class LockedActivatorTileEntity extends TileEntity {
	public static final String publicName = "lockedActivatorTileEntity";
	public static final String delegateNameKey = "itemDelegateName";
	
	public String itemDelegateName;

	public static String getPublicname() {
		return publicName;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		writeSynableDataToNBT(tag);
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		readSyncableDataFromNBT(tag);
		super.readFromNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSynableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	private void readSyncableDataFromNBT(NBTTagCompound tag) {
		itemDelegateName = tag.getString(delegateNameKey);
	}

	private void writeSynableDataToNBT(NBTTagCompound tag) {
		if (itemDelegateName != null) tag.setString(delegateNameKey, itemDelegateName);
	}
}
