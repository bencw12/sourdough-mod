package com.bencw12.sourdough.tileentity;

import com.bencw12.sourdough.blocks.CuttingBoardBlock;
import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.items.ItemSourdoughStarter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileEntityCuttingBoard extends TileEntity implements IInventory, ITickable {
    private NonNullList<ItemStack> dough;
    public TileEntityCuttingBoard(){
        this.dough = NonNullList.withSize(1 ,ItemStack.EMPTY);
    }
    public NonNullList<ItemStack> getDough(){
        return this.dough;
    }

    public void setDough(ItemStack dough){
        this.dough.set(0, dough);
        this.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        if(this.dough.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return (ItemStack)this.dough.get(0);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        return ItemStackHelper.getAndSplit(this.dough, i, i1);
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.dough, i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if(!this.world.isRemote) {
            ItemStack stack = (ItemStack) this.dough.get(i);
            boolean flag = !stack.isEmpty() && stack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, itemStack);
            this.dough.set(i, itemStack);
            if (itemStack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }

            if (i == 0 && !flag)
                this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) { }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) { }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return i == 0 && itemStack.getItem() instanceof ItemSourdoughStarter;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) { }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.dough);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        dough = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.dough);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState old, IBlockState news) {
        return (old.getBlock() != news.getBlock());
    }

    @Override
    public void update() {
        if(!this.world.isRemote) {
            if (world.getBlockState(pos).getValue(CuttingBoardBlock.DOUGH) != 4 - this.dough.get(0).getItemDamage() && !this.dough.get(0).isEmpty())
                world.setBlockState(pos, ModBlocks.CUTTING_BOARD_BLOCK.getDefaultState().withProperty(CuttingBoardBlock.FACING, world.getBlockState(pos).getValue(CuttingBoardBlock.FACING)).withProperty(CuttingBoardBlock.DOUGH, 4 - this.dough.get(0).getItemDamage()));
            if (this.dough.get(0).isEmpty())
                world.setBlockState(pos, ModBlocks.CUTTING_BOARD_BLOCK.getDefaultState().withProperty(CuttingBoardBlock.FACING, world.getBlockState(pos).getValue(CuttingBoardBlock.FACING)).withProperty(CuttingBoardBlock.DOUGH, 0));
        }
        this.markDirty();
    }
}
