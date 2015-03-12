package com.nguyenquyhy.SuperActivation;

import com.nguyenquyhy.SuperActivation.blocks.ItemStackHelper;
import com.nguyenquyhy.SuperActivation.blocks.LockedPressurePlate;
import com.nguyenquyhy.SuperActivation.gui.GuiHandler;
import com.nguyenquyhy.SuperActivation.gui.LockedPressurePlateGuiScreen;
import com.nguyenquyhy.SuperActivation.packets.LockActivatorMessage;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = SuperActivationMod.MODID, name = SuperActivationMod.MODNAME, version = SuperActivationMod.VERSION)
public class SuperActivationMod {
	public static final String MODNAME = "Super Activation";
	public static final String MODID = "superactivation";
	public static final String VERSION = "1.0";

	@Mod.Instance
	public static SuperActivationMod instance;

	public static SimpleNetworkWrapper channel;

	public static final LockedPressurePlate lockedPressurePlate = new LockedPressurePlate(
			"stone", Material.rock, BlockPressurePlate.Sensitivity.mobs);

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		channel.registerMessage(LockActivatorMessage.Handler.class,
				LockActivatorMessage.class, 0, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerBlock(lockedPressurePlate, "lockedPressurePlate");

		Block stonePlate = BlockPressurePlate
				.getBlockFromName("stone_pressure_plate");
		GameRegistry.addShapelessRecipe(new ItemStack(lockedPressurePlate),
				new Object[] { stonePlate, Items.redstone });

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		GameRegistry.registerTileEntity(LockedActivatorTileEntity.class,
				LockedActivatorTileEntity.publicName);
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
	}
}
