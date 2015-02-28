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
	private String itemDelegateName;
	private int x;
	private int y;
	private int z;

	public LockActivatorMessage() {

	}

	public LockActivatorMessage(int x, int y, int z, String itemDelegateName) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.itemDelegateName = itemDelegateName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, 5);
		y = ByteBufUtils.readVarInt(buf, 5);
		z = ByteBufUtils.readVarInt(buf, 5);
		itemDelegateName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
		ByteBufUtils.writeUTF8String(buf, itemDelegateName);
	}

	public static class Handler implements
			IMessageHandler<LockActivatorMessage, IMessage> {
		@Override
		public IMessage onMessage(LockActivatorMessage message,
				MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			LockedActivatorTileEntity tileEntity = (LockedActivatorTileEntity)world.getTileEntity(message.x, message.y, message.z);
			if (tileEntity != null && (tileEntity.itemDelegateName == null || tileEntity.itemDelegateName.isEmpty())) {
				tileEntity.itemDelegateName = message.itemDelegateName;
				world.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}

	}
}
