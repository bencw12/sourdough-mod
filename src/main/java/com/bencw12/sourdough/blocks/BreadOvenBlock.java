package com.bencw12.sourdough.blocks;


import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.items.ItemSourdough;
import com.bencw12.sourdough.tileentity.TileEntityBreadOven;
import com.bencw12.sourdough.tileentity.TileEntityCuttingBoard;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;


public class BreadOvenBlock extends BlockBase {
    boolean keepInventory;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger LOGS = PropertyInteger.create("logs", 0, 4);
    public static final PropertyInteger DOUGH = PropertyInteger.create("dough", 0, 2);


    public BreadOvenBlock(String name) {
        super(name, Material.ROCK);
        keepInventory = false;
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOGS, 0).withProperty(DOUGH, 0));
        setHardness(5.0F);
    }

    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }

    public boolean isFullCube(IBlockState p_isFullCube_1_) {
        return false;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, LOGS, DOUGH});
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing lvt_2_1_ = EnumFacing.getFront(meta);
        if (lvt_2_1_.getAxis() == EnumFacing.Axis.Y) {
            lvt_2_1_ = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, lvt_2_1_);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateForPlacement(World p_getStateForPlacement_1_, BlockPos p_getStateForPlacement_2_, EnumFacing p_getStateForPlacement_3_, float p_getStateForPlacement_4_, float p_getStateForPlacement_5_, float p_getStateForPlacement_6_, int p_getStateForPlacement_7_, EntityLivingBase p_getStateForPlacement_8_) {
        return this.getDefaultState().withProperty(FACING, p_getStateForPlacement_8_.getHorizontalFacing().getOpposite());
    }

    public IBlockState withRotation(IBlockState p_withRotation_1_, Rotation p_withRotation_2_) {
        return p_withRotation_1_.withProperty(FACING, p_withRotation_2_.rotate((EnumFacing)p_withRotation_1_.getValue(FACING)));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World p_createTileEntity_1_, IBlockState p_createTileEntity_2_) {
        return new TileEntityBreadOven();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        if(!keepInventory) {
            TileEntity lvt_4_1_ = world.getTileEntity(pos);
            if (lvt_4_1_ instanceof TileEntityBreadOven) {
                ItemStack copy = ((TileEntityBreadOven) lvt_4_1_).getStackInSlot(3);
                copy.shrink(1);
                ((TileEntityBreadOven) lvt_4_1_).setSlot(3, copy);
                InventoryHelper.dropInventoryItems(world, pos, (TileEntityBreadOven) lvt_4_1_);
                world.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {

        ItemStack handItem = player.getHeldItem(hand);

        TileEntityBreadOven oven = this.getTileEntity(world, pos);
        NonNullList<ItemStack> stacks = oven.getOvenStacks();

        ItemStack dough = stacks.get(0);
        ItemStack result = stacks.get(2);
        ItemStack logs = stacks.get(3);

        if(!world.isRemote) {
            //Cannot interact with oven when it is cooking
            if (oven.isCooking()) {
                return false;
            }
            //Light the oven
            else if (result.isEmpty() && !dough.isEmpty() && !oven.isCooking() && !logs.isEmpty() && handItem.getItem() == Items.FLINT_AND_STEEL && !oven.isLit()) {
                oven.light();
                handItem.damageItem(1, player);
            }
            //Place dough in oven
            else if (dough.isEmpty() && handItem.getItem() instanceof ItemSourdough && result.isEmpty()) {

                ItemStack copy = new ItemStack(ModItems.SOURDOUGH);

                //Copy the item's nbt tag
                if (handItem.hasTagCompound()) {
                    NBTTagCompound nbt = handItem.getTagCompound();
                    copy.setTagCompound(nbt);
                }
                if (!player.capabilities.isCreativeMode) {
                    handItem.shrink(1);
                }

                oven.setDough(1);
                oven.setSlot(0, copy);

                world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(DOUGH, 1).withProperty(LOGS, world.getBlockState(pos).getValue(LOGS)).withProperty(FACING, world.getBlockState(pos).getValue(FACING)));

            }
            //Add log to oven
            else if (handItem.getItem() == Item.getItemFromBlock(Blocks.LOG)) {
                if (logs.getCount() >= 1 && logs.getCount() < 5) {
                    logs.setCount(logs.getCount() + 1);
                    oven.setSlot(3, logs);
                    if (!player.capabilities.isCreativeMode) {
                        handItem.shrink(1);
                    }

                }

                world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(DOUGH, world.getBlockState(pos).getValue(DOUGH)).withProperty(LOGS, logs.getCount() - 1).withProperty(FACING, world.getBlockState(pos).getValue(FACING)));

            }
            //Remove uncooked dough
            else if (!dough.isEmpty() && !oven.isCooking() && result.isEmpty()) {

                if (handItem.isEmpty()) {
                    player.setHeldItem(hand, dough);
                } else if (!player.addItemStackToInventory(dough)) {
                    player.dropItem(dough, false);
                }


                oven.setSlot(0, ItemStack.EMPTY);
                oven.setDough(0);

                world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(DOUGH, 0).withProperty(LOGS, world.getBlockState(pos).getValue(LOGS)).withProperty(FACING, world.getBlockState(pos).getValue(FACING)));


            }
            //Remove cooked dough
            else if (!result.isEmpty() && !oven.isCooking() && dough.isEmpty()) {

                if (handItem.isEmpty()) {
                    player.setHeldItem(hand, result);
                } else if (!player.addItemStackToInventory(result)) {
                    player.dropItem(result, false);
                }

                oven.setSlot(2, ItemStack.EMPTY);
                oven.setDough(0);
                world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(DOUGH, 0).withProperty(LOGS, world.getBlockState(pos).getValue(LOGS)).withProperty(FACING, world.getBlockState(pos).getValue(FACING)));

            }
        }

        //Remove a log
        /*
        else if(result.isEmpty() && dough.isEmpty() && !oven.isCooking() && oven.getLogs() > 0){
            ItemStack log = new ItemStack(logs.getItem());
            logs.setCount(logs.getCount() - 1);
            oven.setSlot(3, logs);
            player.addItemStackToInventory(log);
        }
        */

        world.notifyBlockUpdate(pos, state, state, 3);
        oven.markDirty();

        return true;

    }

    @Override
    public boolean hasTileEntity(IBlockState p_hasTileEntity_1_) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

        TileEntityBreadOven te = this.getTileEntity(world, pos);



        if (te.isCooking()) {
            EnumFacing facing = (EnumFacing)state.getValue(FACING);
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double smokeY = (double)pos.getY() + rand.nextDouble() * 2.0D / 16.0D;
            double z = (double)pos.getZ() + 0.5D;
            double lvt_12_1_ = 0.52D;
            double lvt_14_1_ = rand.nextDouble() * 0.6D - 0.3D;
            if (rand.nextDouble() < 0.1D) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch(facing) {
                case WEST:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, x - 0.32D, y + 0.1D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    world.spawnParticle(EnumParticleTypes.FLAME, x + 0.32D, y+0.1D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.2, smokeY + 1.0D, z + lvt_14_1_/6.0D, 0.0D, 0.0D, 0.0D, new int[0]);

                    break;
                case NORTH:
                    world.spawnParticle(EnumParticleTypes.FLAME, x + lvt_14_1_/6.0D, y, z - 0.32D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z - 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    break;
                case SOUTH:
                    world.spawnParticle(EnumParticleTypes.FLAME, x + lvt_14_1_/6.0D, y, z + 0.32D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, smokeY + 1.0D, z + 0.2, 0.0D, 0.0D, 0.0D, new int[15]);
            }

        }
    }

    private TileEntityBreadOven getTileEntity(World world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        return te instanceof  TileEntityBreadOven ? (TileEntityBreadOven)te : null;
    }


}
