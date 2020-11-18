package com.bencw12.sourdough.blocks;

import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.items.ItemSourdoughStarter;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
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

import java.util.Random;

public class JarBlock extends BlockBase implements ITileEntityProvider {

    public static final AxisAlignedBB JAR_AABB = new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 0.625D, 0.875D);

    public static final PropertyInteger FERMENTING_STATE = PropertyInteger.create("fermenting", 0, 6);


    private boolean isSaturated;
    private static boolean keepInventory;

    public JarBlock(String name) {

        super(name, Material.GLASS);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        setSoundType(SoundType.GLASS);
        setHardness(50.0F);
        setResistance(1.5F);
        setHarvestLevel("pickaxe", 0);
        setLightOpacity(1);
        setTickRandomly(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FERMENTING_STATE, 0));
        useNeighborBrightness = false;
        this.isSaturated = false;
        keepInventory = false;

    }
    //Visual stuff
    public AxisAlignedBB getBoundingBox(IBlockState p_getBoundingBox_1_, IBlockAccess p_getBoundingBox_2_, BlockPos p_getBoundingBox_3_) {
        return JAR_AABB;
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Items.AIR;
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){

        ItemStack handItem = player.getHeldItem(hand);
        TileEntityJar jar = this.getTileEntity(world, pos);

        NonNullList<ItemStack> slots = jar.getJarItemStacks();

        ItemStack dough = slots.get(0);
        ItemStack fermentAid = slots.get(1);
        ItemStack output = slots.get(2);

        if(!world.isRemote) {
            if (jar.isFermenting()) {
                return true;
            } else if (player.isSneaking()) {
                if (!output.isEmpty()) {
                    ItemStack copy = new ItemStack(ModItems.SOURDOUGH_STARTER);

                    NBTTagCompound nbt = output.getTagCompound();

                    copy.setTagCompound(nbt);

                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, copy);
                    } else if (!player.addItemStackToInventory(copy)) {
                        player.dropItem(copy, false);
                    }
                    world.setBlockToAir(pos);

                } else if (output.isEmpty() && fermentAid.isEmpty() && !dough.isEmpty() && dough.getItem() != Items.WHEAT) {

                    ItemStack copy = new ItemStack(ModItems.SOURDOUGH_STARTER);

                    NBTTagCompound nbt = dough.getTagCompound();

                    copy.setTagCompound(nbt);
                    copy.setItemDamage(dough.getItemDamage());

                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, copy);
                    } else if (!player.addItemStackToInventory(copy)) {
                        player.dropItem(copy, false);
                    }
                    world.setBlockToAir(pos);

                } else if (output.isEmpty() && dough.isEmpty() && fermentAid.isEmpty() && !jar.isSaturated()) {

                    ItemStack stack = new ItemStack(Item.getItemFromBlock(ModBlocks.JAR_BLOCK));

                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, stack);
                    } else if (!player.addItemStackToInventory(stack)) {
                        player.dropItem(stack, false);
                    }

                    world.setBlockToAir(pos);
                }
            } else if (output.isEmpty() && dough.isEmpty() && fermentAid.isEmpty() && !jar.isSaturated() && canBeFermented(handItem)) {

                jar.setSlot(0, new ItemStack(Items.WHEAT));
                handItem.shrink(1);
            } else if (output.isEmpty() && !dough.isEmpty() && fermentAid.isEmpty() && !jar.isSaturated() && isFermentAid(handItem)) {
                jar.setSlot(1, new ItemStack(handItem.getItem()));
                handItem.shrink(1);
            } else if (output.isEmpty() && !dough.isEmpty() && !fermentAid.isEmpty() && !jar.isSaturated() && canSaturate(handItem)) {
                if (handItem.getItem() instanceof ItemPotion) {

                    NBTTagCompound nbt = handItem.getTagCompound();

                    String type = nbt.getString("Potion");

                    if (type.equals("minecraft:empty") || type.equals("minecraft:water") || type.equals("minecraft:mundane") || type.equals("minecraft:thick") || type.equals("minecraft:awkward")) {
                        type = "minecraft:empty";
                    }

                    jar.setPotionType(type);
                    jar.saturate();
                    player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));


                }
            } else if (output.isEmpty() && !fermentAid.isEmpty() && !dough.isEmpty() && !jar.isSaturated()) {
                ItemStack stack = new ItemStack(fermentAid.getItem());

                jar.setSlot(1, ItemStack.EMPTY);

                if (handItem.isEmpty()) {
                    player.setHeldItem(hand, stack);
                } else if (!player.addItemStackToInventory(stack)) {
                    player.dropItem(stack, false);
                }
            } else if (output.isEmpty() && fermentAid.isEmpty() && !dough.isEmpty() && !jar.isSaturated()) {

                if (dough.getItem() == Items.WHEAT) {
                    ItemStack stack = new ItemStack(Items.WHEAT);

                    jar.setSlot(0, ItemStack.EMPTY);
                    if (handItem.isEmpty()) {
                        player.setHeldItem(hand, stack);
                    } else if (!player.addItemStackToInventory(stack)) {
                        player.dropItem(stack, false);
                    }
                }

            }
        }

        jar.markDirty();
        world.notifyBlockUpdate(pos, state, state, 3);

        return true;


    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileEntityJar();
    }



    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[]{FERMENTING_STATE});
    }

    public boolean canBeFermented(ItemStack stack){

        Item item = stack.getItem();

        if(item instanceof ItemSourdoughStarter || item == Items.WHEAT){
            return true;
        }
        else
            return false;
    }

    public boolean isFermentAid(ItemStack stack){
        Item item = stack.getItem();

        if(item != Items.NETHER_WART && item != Items.CHORUS_FRUIT && item != Items.SUGAR)
            return false;
        else
            return true;
    }

    public boolean canSaturate(ItemStack stack){

        Item item = stack.getItem();

        if(item != Items.POTIONITEM)
            return false;
        else
            return true;

    }


    public static void setState(boolean isFermenting, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);

        if(!world.isRemote) {
            if (te != null) {
                te.validate();
                world.setTileEntity(pos, te);
            }
        }

    }



    public IBlockState getStateFromMeta(int meta){
            return this.getDefaultState().withProperty(FERMENTING_STATE, meta);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FERMENTING_STATE);
    }


    public void breakBlock(World p_breakBlock_1_, BlockPos p_breakBlock_2_, IBlockState p_breakBlock_3_) {
        super.breakBlock(p_breakBlock_1_, p_breakBlock_2_, p_breakBlock_3_);
    }

    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }


    private TileEntityJar getTileEntity(World world, BlockPos pos){

        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityJar ? (TileEntityJar)te : null;

    }

}
