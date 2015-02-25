package com.nguyenquyhy.SuperActivation.blocks;

import java.util.Iterator;
import java.util.List;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import com.nguyenquyhy.SuperActivation.gui.GUIs;
import com.nguyenquyhy.SuperActivation.gui.LockedPressurePlateGuiScreen;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class LockedPressurePlate extends BlockPressurePlate {
	public LockedPressurePlate(String icon, Material material,
			BlockPressurePlate.Sensitivity sensitivity) {
		super(icon, material, sensitivity);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			mc.thePlayer.openGui(SuperActivationMod.instance,
					GUIs.LockedPressurePlate.ordinal(), world, x, y, z);
			return true;
		} else {
			return false;
		}
	};

	@Override
	public int func_150065_e(World world, int x, int y, int z) {
		List list = world.getEntitiesWithinAABB(EntityPlayer.class,
				this.func_150061_a(x, y, z));

		if (list != null && !list.isEmpty()) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				Object entity = iterator.next();

				if (entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					if (!player.doesEntityNotTriggerPressurePlate()) {
						ItemStack currentStack = player.inventory
								.getCurrentItem();
						if (currentStack != null) {
							Item item = currentStack.getItem();
							if (item != null) {
								String name = item.getUnlocalizedName();
								if (name.equals("tile.grass")
										|| name.equals("tile.dirt"))
									return 15;
							}
						}

						// player.addChatMessage(new
						// ChatComponentText("You have to carry a " + blockName
						// + " to activate this!"));
					}
				}
			}
		}

		return 0;
	}

	public static Minecraft mc = Minecraft.getMinecraft();
}
