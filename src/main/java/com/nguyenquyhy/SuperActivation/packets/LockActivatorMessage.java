package com.nguyenquyhy.SuperActivation.packets;

import net.minecraft.world.World;

import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class LockActivatorMessage implements IMessage {
	private String itemUnlocalizedName;
	private int x;
	private int y;
	private int z;

	public LockActivatorMessage() {

	}

	public LockActivatorMessage(int x, int y, int z, String itemUnlocalizedName) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.itemUnlocalizedName = itemUnlocalizedName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, 5);
		y = ByteBufUtils.readVarInt(buf, 5);
		z = ByteBufUtils.readVarInt(buf, 5);
		itemUnlocalizedName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
		ByteBufUtils.writeUTF8String(buf, itemUnlocalizedName);
	}

	public static class Handler implements
			IMessageHandler<LockActivatorMessage, IMessage> {
		@Override
		public IMessage onMessage(LockActivatorMessage message,
				MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			LockedActivatorTileEntity tileEntity = (LockedActivatorTileEntity)world.getTileEntity(message.x, message.y, message.z);
			if (tileEntity != null && (tileEntity.itemUnlocalizedName == null || tileEntity.itemUnlocalizedName.isEmpty())) {
				tileEntity.itemUnlocalizedName = message.itemUnlocalizedName;
				world.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}

	}
}
