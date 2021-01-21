package com.bencw12.sourdough.blocks;

import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.items.ItemSourdough;
import com.bencw12.sourdough.items.ItemSourdoughStarter;
import com.bencw12.sourdough.tileentity.TileEntityCuttingBoard;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import scala.tools.nsc.io.Jar;

import javax.annotation.Nullable;
import java.util.Random;

public class CuttingBoardBlock extends BlockBase implements ITileEntityProvider {
    private static boolean keepInventory;

    public static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0625D, 0, 0.1875D, 0.9375D, 0.0625D, 0.8125D);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger DOUGH = PropertyInteger.create("dough",0,4);

    public CuttingBoardBlock(String name) {
        super(name, Material.WOOD);
        setSoundType(SoundType.WOOD);
        setHardness(0.5F);
        setResistance(1.5F);
        setHarvestLevel("pickaxe", 1);
        keepInventory = false;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DOUGH, 0));
    }

    public AxisAlignedBB getBoundingBox(IBlockState p_getBoundingBox_1_, IBlockAccess p_getBoundingBox_2_, BlockPos p_getBoundingBox_3_) {
        if(p_getBoundingBox_1_.getValue(FACING) == EnumFacing.NORTH || p_getBoundingBox_1_.getValue(FACING) == EnumFacing.SOUTH){
            return BOUNDS;
        }
        else{
            return new AxisAlignedBB(0.1875D, 0, 0.0625D, 0.8125D, 0.0625D, 0.9375D);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityCuttingBoard();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(ModBlocks.CUTTING_BOARD_BLOCK);
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    public boolean doesSideBlockRendering(IBlockState p_doesSideBlockRendering_1_, IBlockAccess p_doesSideBlockRendering_2_, BlockPos p_doesSideBlockRendering_3_, EnumFacing p_doesSideBlockRendering_4_) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack handItem = player.getHeldItem(hand);
        TileEntityCuttingBoard board = this.getTileEntity(world, pos);
        ItemStack dough = board.getDough().get(0);

        if(!world.isRemote) {
            //Place Sourdough Starter
            if (handItem.getItem() instanceof ItemSourdoughStarter) {

                if (dough.isEmpty()) {
                    board.setDough(handItem.copy());
                    player.setHeldItem(hand, new ItemStack(Item.getItemFromBlock(ModBlocks.JAR_BLOCK)));

                } else {
                    return false;
                }
            }
            //Cut piece of sourdough
            else if (handItem.getItem() instanceof ItemSword) {
                if (dough.isEmpty()) {
                    return false;
                } else if (true) {

                    int damage = dough.getItemDamage();
                    if (damage == 3) {
                        board.setDough(ItemStack.EMPTY);
                        board.removeStackFromSlot(0);
                    } else {
                        damage++;
                        dough.setItemDamage(damage);
                    }
                    NBTTagCompound nbt = dough.getTagCompound();

                    ItemStack result = new ItemStack(ModItems.SOURDOUGH);
                    result.setTagCompound(nbt);

                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, result);
                    } else if (!player.addItemStackToInventory(result)) {
                        player.dropItem(result, false);
                    }
                }
            }
            //Put sourdough back in jar
            else if (Block.getBlockFromItem(handItem.getItem()) == ModBlocks.JAR_BLOCK) {

                if (board.getDough().get(0).getItem() instanceof ItemSourdoughStarter) {
                    handItem.shrink(1);
                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, dough);
                    } else if (!player.addItemStackToInventory(dough)) {
                        player.dropItem(dough, false);
                    }
                    board.setDough(ItemStack.EMPTY);
                    board.removeStackFromSlot(0);
                }
            } else {
                board.setDough(ItemStack.EMPTY);
            }
        }

        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, this, 0, 0);
        board.markDirty();

        return true;
    }

    public static void setState(int type, World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        TileEntity board = world.getTileEntity(pos);

        keepInventory = true;

            world.setBlockState(pos, ModBlocks.CUTTING_BOARD_BLOCK.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(DOUGH, type), 3);
            world.setBlockState(pos, ModBlocks.CUTTING_BOARD_BLOCK.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(DOUGH, type), 3);

        keepInventory = false;

        if(board != null){
            board.validate();
            world.setTileEntity(pos, board);
        }
    }

    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[]{FACING, DOUGH});
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
    }

    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    private TileEntityCuttingBoard getTileEntity(World world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityCuttingBoard ? (TileEntityCuttingBoard)te : null;
    }

    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float p_getStateForPlacement_4_, float p_getStateForPlacement_5_, float p_getStateForPlacement_6_, int p_getStateForPlacement_7_, EntityLivingBase p_getStateForPlacement_8_) {
        return this.getDefaultState().withProperty(FACING, p_getStateForPlacement_8_.getHorizontalFacing().getOpposite());
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
}
