package com.nguyenquyhy.SuperActivation;

import com.nguyenquyhy.SuperActivation.blocks.LockedPressurePlate;
import com.nguyenquyhy.SuperActivation.gui.GuiHandler;
import com.nguyenquyhy.SuperActivation.gui.LockedPressurePlateGuiScreen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = SuperActivationMod.MODID, name = SuperActivationMod.MODNAME, version = SuperActivationMod.VERSION)
public class SuperActivationMod {
	public static final String MODNAME = "Super Activation";
	public static final String MODID = "superactivation";
	public static final String VERSION = "1.0";

	@Mod.Instance
	public static SuperActivationMod instance;
	
	public static final Block lockedPressurePlate = new LockedPressurePlate(
			"stone", Material.rock, BlockPressurePlate.Sensitivity.mobs)
			.setHardness(0.5F).setStepSound(Block.soundTypePiston)
			.setCreativeTab(CreativeTabs.tabRedstone)
			.setBlockName("lockedPressurePlate");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// new BlockPressurePlate("stone", Material.rock,
		// BlockPressurePlate.Sensitivity.mobs)).setHardness(0.5F).setStepSound(soundTypePiston).setBlockName("pressurePlate"));
		// new BlockPressurePlate("planks_oak", Material.wood,
		// BlockPressurePlate.Sensitivity.everything)).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("pressurePlate"));
		GameRegistry
				.registerBlock(lockedPressurePlate, "lockedPressurePlate");

		Block stonePlate = BlockPressurePlate
				.getBlockFromName("stone_pressure_plate");
		GameRegistry.addShapelessRecipe(new ItemStack(lockedPressurePlate),
				new Object[] { stonePlate, Blocks.redstone_wire });
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
}
