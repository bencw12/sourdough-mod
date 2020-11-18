package com.bencw12.sourdough.tileentity;

import com.bencw12.sourdough.blocks.BreadOvenBlock;
import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityBreadOven extends TileEntity implements ITickable, IInventory {

    public int cookTime;
    private int logs;
    private int facing;
    private int totalCookTime;
    private boolean isCooking;
    private String customName;
    //Right click on the oven to light it with a flint and steel
    private boolean isLit;
    private int dough;

    private NonNullList<ItemStack> ovenItemStacks;


    public TileEntityBreadOven(){
        //Takes 3 logs and 1 charcoal as fuel
        //First slot (0) is bread, (1) is logs, (2) is charcoal
        this.ovenItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        this.isCooking = false;
        this.facing = 0;
        ovenItemStacks.set(3, new ItemStack(Item.getItemFromBlock(Blocks.LOG)));
        this.isLit = false;
    }

    public void setSlot(int idx, ItemStack stack){
        this.ovenItemStacks.set(idx, stack);
        this.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public boolean isEmpty() {

        return this.ovenItemStacks.isEmpty();

    }

    public boolean isLit(){
        return this.isLit;
    }

    public void light(){
        this.isLit = true;
    }


    @Override
    public ItemStack getStackInSlot(int i) {
        return this.ovenItemStacks.get(i);
    }

    public NonNullList<ItemStack> getOvenStacks(){
        return this.ovenItemStacks;
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }



    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.ovenItemStacks);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("TotalCookTime", (short)this.totalCookTime);
        compound.setBoolean("isCooking", this.isCooking);
        compound.setInteger("facing", this.facing);
        compound.setInteger("logs", this.logs);
        compound.setBoolean("lit", this.isLit);
        compound.setInteger("dough", this.dough);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ovenItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.ovenItemStacks);
        this.logs = compound.getInteger("logs");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("TotalCookTime");
        this.isCooking = compound.getBoolean("isCooking");
        this.facing = compound.getInteger("facing");
        this.isLit = compound.getBoolean("lit");
        this.dough = compound.getInteger("dough");

    }

    public boolean isCooking() {
        return this.isCooking;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCooking(IInventory i) {
        return i.getField(0) > 0;
    }

    public boolean canCook(){

        if(this.ovenItemStacks.isEmpty())
            return false;
        else if(!this.isLit)
            return false;
        else if(this.ovenItemStacks.get(3).isEmpty())
            return false;
        else{
            if(!this.ovenItemStacks.get(2).isEmpty())
                return false;
            else if(this.ovenItemStacks.get(0).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public int getField(int p_getField_1_) {
        switch(p_getField_1_) {
            case 0:
                return this.cookTime;
            case 1:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }



    @Override
    public void setField(int p_setField_1_, int p_setField_2_) {
        switch(p_setField_1_) {
            case 0:
                this.cookTime = p_setField_2_;
                break;
            case 1:
                this.totalCookTime = p_setField_2_;
        }

    }

    public void setDough(int dough){
        this.dough = dough;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void update() {

        this.facing = world.getBlockState(pos).getValue(BreadOvenBlock.FACING).getHorizontalIndex();
        this.logs = this.ovenItemStacks.get(3).getCount() - 1;


        if((world.getBlockState(pos).getValue(BreadOvenBlock.DOUGH) != this.dough || world.getBlockState(pos).getValue(BreadOvenBlock.LOGS) != this.logs) && this.world.isRemote)
            world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(BreadOvenBlock.DOUGH, this.dough).withProperty(BreadOvenBlock.LOGS, this.logs).withProperty(BreadOvenBlock.FACING, EnumFacing.getHorizontal(this.facing)));



        boolean flag = this.isCooking();

        if(!this.world.isRemote){

            if(this.canCook()){

                isCooking = true;
                cookTime++;
                totalCookTime = 200;

                if(cookTime == totalCookTime){
                    cookItem();
                    this.ovenItemStacks.set(0, ItemStack.EMPTY);
                    cookTime = 0;
                    this.isLit = false;
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    this.removeStackFromSlot(0);
                    this.markDirty();
                    this.dough = 2;

                    world.setBlockState(pos, ModBlocks.BREAD_OVEN_BLOCK.getDefaultState().withProperty(BreadOvenBlock.DOUGH, 2).withProperty(BreadOvenBlock.FACING, EnumFacing.getHorizontal(this.facing)).withProperty(BreadOvenBlock.LOGS, logs - 1));

                    if(ovenItemStacks.get(3).getCount()  == 1)
                        this.logs = 0;


                }
            } else {
                isCooking = false;
            }
        }
        if(flag != this.isCooking()){
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            this.markDirty();
        }

    }


    public void cookItem() {
        if (this.ovenItemStacks.get(0).hasTagCompound()) {


            NBTTagCompound nbt = this.ovenItemStacks.get(0).getTagCompound();

            ItemStack result;

            if (nbt.getString("Tier").equals("tier1")) {
                result = new ItemStack(ModItems.SOURDOUGH_COOKED_T1);
            } else if (nbt.getString("Tier").equals("tier2"))
                result = new ItemStack(ModItems.SOURDOUGH_COOKED_T2);
            else {
                result = new ItemStack(ModItems.SOURDOUGH_COOKED_T3);
            }


            result.setTagCompound(nbt);

            this.ovenItemStacks.set(2, result);
        }

        this.ovenItemStacks.set(0, ItemStack.EMPTY);
        this.ovenItemStacks.set(1, ItemStack.EMPTY);

        ItemStack log = this.ovenItemStacks.get(3);

        if (this.logs == 1){
            log.shrink(1);
            this.ovenItemStacks.set(3, log);
        }
        else{
            log.shrink(1);
            this.ovenItemStacks.set(3, log);
        }



        this.markDirty();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState old, IBlockState news) {
        return (old.getBlock() != news.getBlock());
    }

}
