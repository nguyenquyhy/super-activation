package com.nguyenquyhy.SuperActivation.blocks;

import java.util.List;
import java.util.Random;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import com.nguyenquyhy.SuperActivation.gui.GUIs;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LockedPressurePlate extends BlockPressurePlate implements ITileEntityProvider {
    private final String name;
    private final boolean invisible;

    public LockedPressurePlate(String name, Material material, BlockPressurePlate.Sensitivity sensitivity, boolean invisible) {
        super(material, sensitivity);
        this.name = name;
        this.invisible = invisible;
        setHardness(0.5F);
        if (material == Material.ROCK) {
            if (invisible)
                setUnlocalizedName("invisibleLockedPressurePlateStone");
            else
                setUnlocalizedName("lockedPressurePlateStone");
        } else {
            if (invisible)
                setUnlocalizedName("invisibleLockedPressurePlateWood");
            else
                setUnlocalizedName("lockedPressurePlateWood");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(SuperActivationMod.instance, GUIs.LockedPressurePlate.ordinal(),
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public int computeRedstoneStrength(World world, BlockPos blockPos) {
        List list = world.getEntitiesWithinAABB(EntityPlayer.class, PRESSURE_AABB.offset(blockPos));

        if (!list.isEmpty()) {

            for (Object entity : list) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (!player.doesEntityNotTriggerPressurePlate()) {
                        ItemStack currentStack = player.inventory.getCurrentItem();
                        if (currentStack != null) {
                            String delegateName = currentStack.getItem().delegate.name().toString();
                            LockedActivatorTileEntity tileEntity = (LockedActivatorTileEntity) world.getTileEntity(blockPos);

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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        if (this.invisible) return EnumBlockRenderType.INVISIBLE;
        else return super.getRenderType(state);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
