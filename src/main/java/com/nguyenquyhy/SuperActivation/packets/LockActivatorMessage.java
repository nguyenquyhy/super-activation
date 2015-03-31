package com.nguyenquyhy.SuperActivation.packets;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LockActivatorMessage implements IMessage {
	private String itemDelegateName;
	private int x;
	private int y;
	private int z;

	public LockActivatorMessage(BlockPos blockPos, String itemDelegateName) {
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
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

	public static class Handler implements IMessageHandler<LockActivatorMessage, IMessage> {
		@Override
		public IMessage onMessage(LockActivatorMessage message, MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			BlockPos blockPos = new BlockPos(message.x, message.y, message.z);
			LockedActivatorTileEntity tileEntity = (LockedActivatorTileEntity)world.getTileEntity(blockPos);
			if (tileEntity != null && (tileEntity.itemDelegateName == null || tileEntity.itemDelegateName.isEmpty())) {
				tileEntity.itemDelegateName = message.itemDelegateName;
				world.markBlockForUpdate(blockPos);
			}
			return null;
		}

	}
}
