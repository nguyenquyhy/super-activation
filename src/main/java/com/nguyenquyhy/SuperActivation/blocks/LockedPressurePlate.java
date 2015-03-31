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
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class LockedPressurePlate extends BlockPressurePlate implements ITileEntityProvider {
    public LockedPressurePlate(Material material, BlockPressurePlate.Sensitivity sensitivity) {
        super(material, sensitivity);
        setHardness(0.5F);
        if (material == Material.rock) {
            setStepSound(Block.soundTypePiston);
            setUnlocalizedName("lockedPressurePlateStone");
        }
        else {
            setStepSound(Block.soundTypeWood);
            setUnlocalizedName("lockedPressurePlateWood");
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(SuperActivationMod.instance, GUIs.LockedPressurePlate.ordinal(),
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public int computeRedstoneStrength(World world, BlockPos blockPos) {
        List list = world.getEntitiesWithinAABB(EntityPlayer.class, this.getSensitiveAABB(blockPos));

        if (list != null && !list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object entity = iterator.next();

                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (!player.doesEntityNotTriggerPressurePlate()) {
                        ItemStack currentStack = player.inventory.getCurrentItem();
                        if (currentStack != null) {
                            String delegateName = currentStack.getItem().delegate.name();
                            LockedActivatorTileEntity tileEntity =
                                    (LockedActivatorTileEntity) world.getTileEntity(blockPos);

                            if (tileEntity != null && delegateName.equals(tileEntity.itemDelegateName))
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
