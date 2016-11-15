package com.nguyenquyhy.SuperActivation.proxies;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by Hy on 3/31/2015.
 */
public class ClientProxy extends Proxy {
    @Override
    public void registerItems() {
        super.registerItems();
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        //blocks
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(SuperActivationMod.lockedStonePressurePlate), 0,
                new ModelResourceLocation(SuperActivationMod.MODID + ":" + SuperActivationMod.lockedStonePressurePlate.getName(), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(SuperActivationMod.lockedWoodenPressurePlate), 0,
                new ModelResourceLocation(SuperActivationMod.MODID + ":" + SuperActivationMod.lockedWoodenPressurePlate.getName(), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(SuperActivationMod.invisibleLockedStonePressurePlate), 0,
                new ModelResourceLocation(SuperActivationMod.MODID + ":" + SuperActivationMod.invisibleLockedStonePressurePlate.getName(), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(SuperActivationMod.invisibleLockedWoodenPressurePlate), 0,
                new ModelResourceLocation(SuperActivationMod.MODID + ":" + SuperActivationMod.invisibleLockedWoodenPressurePlate.getName(), "inventory"));
    }
}
