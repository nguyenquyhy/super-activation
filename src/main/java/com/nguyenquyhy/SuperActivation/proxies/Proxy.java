package com.nguyenquyhy.SuperActivation.proxies;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nguyenquyhy.SuperActivation.SuperActivationMod.*;

/**
 * Created by Hy on 3/31/2015.
 */
@Mod.EventBusSubscriber
public class Proxy {
    public void registerItems(){

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(lockedStonePressurePlate.setRegistryName(lockedStonePressurePlate.getName()));
        event.getRegistry().registerAll(lockedWoodenPressurePlate.setRegistryName(lockedWoodenPressurePlate.getName()));
        event.getRegistry().registerAll(invisibleLockedStonePressurePlate.setRegistryName(invisibleLockedStonePressurePlate.getName()));
        event.getRegistry().registerAll(invisibleLockedWoodenPressurePlate.setRegistryName(invisibleLockedWoodenPressurePlate.getName()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(lockedStonePressurePlate).setRegistryName(lockedStonePressurePlate.getName()));
        event.getRegistry().registerAll(new ItemBlock(lockedWoodenPressurePlate).setRegistryName(lockedWoodenPressurePlate.getName()));
        event.getRegistry().registerAll(new ItemBlock(invisibleLockedStonePressurePlate).setRegistryName(invisibleLockedStonePressurePlate.getName()));
        event.getRegistry().registerAll(new ItemBlock(invisibleLockedWoodenPressurePlate).setRegistryName(invisibleLockedWoodenPressurePlate.getName()));
    }
}
