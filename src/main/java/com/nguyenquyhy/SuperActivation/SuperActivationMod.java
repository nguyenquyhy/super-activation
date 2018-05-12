package com.nguyenquyhy.SuperActivation;

import com.nguyenquyhy.SuperActivation.blocks.LockedPressurePlate;
import com.nguyenquyhy.SuperActivation.gui.GuiHandler;
import com.nguyenquyhy.SuperActivation.packets.LockActivatorMessage;
import com.nguyenquyhy.SuperActivation.proxies.Proxy;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = SuperActivationMod.MODID, name = SuperActivationMod.MODNAME, version = SuperActivationMod.VERSION)
public class SuperActivationMod {
    public static final String MODNAME = "Super Activation";
    public static final String MODID = "superactivation";
    public static final String VERSION = "1.3.0";

    //public static final RegistryNamespacedDefaultedByKey blockRegistry = net.minecraftforge.fml.common.registry.GameData.getBlockRegistry();

    @Mod.Instance
    public static SuperActivationMod instance;

    @SidedProxy(clientSide = "com.nguyenquyhy.SuperActivation.proxies.ClientProxy", serverSide = "com.nguyenquyhy.SuperActivation.proxies.Proxy")
    public static Proxy proxy;

    public static SimpleNetworkWrapper channel;

    public static final LockedPressurePlate lockedStonePressurePlate =
            new LockedPressurePlate("locked_stone_pressure_plate", Material.ROCK, BlockPressurePlate.Sensitivity.MOBS, false);
    public static final LockedPressurePlate lockedWoodenPressurePlate =
            new LockedPressurePlate("locked_wooden_pressure_plate", Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING, false);

    public static final LockedPressurePlate invisibleLockedStonePressurePlate =
            new LockedPressurePlate("invisible_locked_stone_pressure_plate", Material.ROCK, BlockPressurePlate.Sensitivity.MOBS, true);
    public static final LockedPressurePlate invisibleLockedWoodenPressurePlate =
            new LockedPressurePlate("invisible_locked_wooden_pressure_plate", Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING, true);

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        channel.registerMessage(LockActivatorMessage.Handler.class, LockActivatorMessage.class, 0, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        Block stonePlate = BlockPressurePlate.getBlockFromName("stone_pressure_plate");
//        Block woodenPlate = BlockPressurePlate.getBlockFromName("wooden_pressure_plate");
//
//        GameRegistry.addShapelessRecipe(new ItemStack(lockedStonePressurePlate), stonePlate, Items.REDSTONE);
//        GameRegistry.addShapelessRecipe(new ItemStack(lockedWoodenPressurePlate), woodenPlate, Items.REDSTONE);
//
//        GameRegistry.addShapelessRecipe(new ItemStack(invisibleLockedStonePressurePlate), stonePlate, Items.REDSTONE, Blocks.GLASS_PANE);
//        GameRegistry.addShapelessRecipe(new ItemStack(invisibleLockedWoodenPressurePlate), woodenPlate, Items.REDSTONE, Blocks.GLASS_PANE);

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        GameRegistry.registerTileEntity(LockedActivatorTileEntity.class, LockedActivatorTileEntity.publicName);

        proxy.registerItems();
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
    }
}
