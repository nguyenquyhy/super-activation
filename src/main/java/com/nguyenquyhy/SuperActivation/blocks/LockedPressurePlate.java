package com.nguyenquyhy.SuperActivation.blocks;

import java.util.Iterator;
import java.util.List;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import com.nguyenquyhy.SuperActivation.gui.GUIs;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LockedPressurePlate extends BlockPressurePlate implements
        ITileEntityProvider {
    public LockedPressurePlate(String icon, Material material,
                               BlockPressurePlate.Sensitivity sensitivity) {
        super(icon, material, sensitivity);
        setHardness(0.5F);
        if (material == Material.rock)
            setStepSound(Block.soundTypePiston);
        else
            setStepSound(Block.soundTypeWood);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("lockedPressurePlate");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(SuperActivationMod.instance,
                    GUIs.LockedPressurePlate.ordinal(), world, x, y, z);
        }
        return true;
    }

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
                            String delegateName = currentStack.getItem().delegate
                                    .name();
                            LockedActivatorTileEntity tileEntity = (LockedActivatorTileEntity) world
                                    .getTileEntity(x, y, z);

                            if (tileEntity != null
                                    && delegateName
                                    .equals(tileEntity.itemDelegateName))
                                return 15;
                        }
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new LockedActivatorTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }
}
