package com.bencw12.sourdough.tileentity;

import com.bencw12.sourdough.blocks.JarBlock;
import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.items.ItemSourdoughStarter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.datafix.fixes.PotionItems;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;

public class TileEntityJar extends TileEntity implements ITickable, IInventory {


    private RenderEntityItem itemRenderer;

    private int fermentTime;
    private int totalFermentTime;
    private String jarCustomName;
    private boolean isSaturated;
    private String potionType;
    private int tier;


    private NonNullList<ItemStack> jarItemStacks;

    public TileEntityJar(){

        this.jarItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
        this.potionType = "";
        
    }

    public String getPotionType(){
        return this.potionType;
    }

    public void setPotionType(String potionType){
        this.potionType = potionType;
    }



    @Override
    public int getSizeInventory() {
        return this.jarItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator var1 = this.jarItemStacks.iterator();

        ItemStack itemstack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemstack = (ItemStack)var1.next();
        } while(itemstack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return (ItemStack)this.jarItemStacks.get(index);
    }


    @Override
    public ItemStack decrStackSize(int p_decrStackSize_1_, int p_decrStackSize_2_) {
        return ItemStackHelper.getAndSplit(this.jarItemStacks, p_decrStackSize_1_, p_decrStackSize_2_);
    }

    @Override
    public ItemStack removeStackFromSlot(int p_removeStackFromSlot_1_) {
        return ItemStackHelper.getAndRemove(this.jarItemStacks, p_removeStackFromSlot_1_);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = (ItemStack)this.jarItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.jarItemStacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalFermentTime = this.getFermentTime();
            this.fermentTime = 0;
            this.markDirty();
        }

    }

    //Time in number of ticks
    public int getFermentTime(){
        ItemStack dough = this.jarItemStacks.get(0);


        if(dough.hasTagCompound()){

            NBTTagCompound nbt = dough.getTagCompound();

            String tier = nbt.getString("Tier");

            if(tier.equals("tier1")){

                if(dough.isItemDamaged()){
                    int damage = dough.getItemDamage();
                    switch(damage){
                        case 1:
                            return 120;
                        case 2:
                            return 240;
                        case 3:
                            return 360;
                    }
                }
                else if(getFermentedResult() == 2)
                    return 480;

            } else if(tier.equals("tier2")){
                if(dough.isItemDamaged()){
                    int damage = dough.getItemDamage();
                    switch(damage){
                        case 1:
                            return 150;
                        case 2:
                            return 300;
                        case 3:
                            return 450;
                    }
                }
                else if(getFermentedResult() == 3)
                    return 720;

            } else if(tier.equals("tier3")){
                if(dough.isItemDamaged()){
                    int damage = dough.getItemDamage();
                    switch(damage){
                        case 1:
                            return 180;
                        case 2:
                            return 320;
                        case 3:
                            return 500;
                    }
                }
            }


        } else {
            return 48000;
        }
        return 48000;

    }

    public int getCurrentFermentTime(){
        return fermentTime;
    }

    public void saturate(){
        isSaturated = true;
        this.markDirty();
    }

    public boolean isSaturated(){
        return isSaturated;
    }

    @Override
    //1 dough at a time, please
    public int getInventoryStackLimit(){
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer p_isUsableByPlayer_1_) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return p_isUsableByPlayer_1_.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.jarCustomName : "container.jar";
    }

    @Override
    public boolean hasCustomName() {
        return this.jarCustomName != null && !this.jarCustomName.isEmpty();
    }





    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager p_onDataPacket_1_, SPacketUpdateTileEntity p_onDataPacket_2_) {
        this.readFromNBT(p_onDataPacket_2_.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.fermentTime = compound.getInteger("FermentTime");
        this.totalFermentTime = compound.getInteger("FermentTimeTotal");
        this.isSaturated = compound.getBoolean("isSaturated");
        this.potionType = compound.getString("PotionType");
        this.potionType = compound.getString("PotionType");
        jarItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.jarItemStacks);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("FermentTime", (short)this.fermentTime);
        compound.setInteger("FermentTimeTotal", (short)this.totalFermentTime);
        compound.setBoolean("isSaturated", this.isSaturated);
        compound.setString("PotionType", this.potionType);



        ItemStackHelper.saveAllItems(compound, this.jarItemStacks);

        return compound;
    }

    public boolean isFermenting() {
        return this.fermentTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isFermenting(IInventory i) {
        return i.getField(0) > 0;
    }

    public void update() {

        boolean flag = false;

        if(this.world.isRemote){
            ItemStack dough = this.jarItemStacks.get(0);
            ItemStack output = this.jarItemStacks.get(2);
            if(dough.hasTagCompound()) {
                switch (dough.getTagCompound().getString("Tier")) {
                    case "tier1":
                        this.tier = 1;
                        break;
                    case "tier2":
                        this.tier = 2;
                        break;
                    case "tier3":
                        this.tier = 3;
                        break;
                    default:
                        break;

                }
            }
            if(output.hasTagCompound()){
                switch (output.getTagCompound().getString("Tier")) {
                    case "tier1":
                        this.tier = 1;
                        break;
                    case "tier2":
                        this.tier = 2;
                        break;
                    case "tier3":
                        this.tier = 3;
                        break;
                    default:
                        break;
                }
            }

            if(this.world.getBlockState(pos).getValue(JarBlock.TIER) != this.tier){


                if(tier != 0)
                    this.world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, (world.getBlockState(pos).getValue(JarBlock.FERMENTING_STATE))).withProperty(JarBlock.TIER, tier));

            }
        }


        if (!this.world.isRemote) {

            ItemStack dough = this.jarItemStacks.get(0);

            //If there is dough in the jar but it is not saturated, change the model to the jar with dough but no water
            if(!this.jarItemStacks.get(0).isEmpty()) {
                if(dough.getItem()==ModItems.SOURDOUGH_STARTER){
                    switch(dough.getTagCompound().getString("Tier")){
                        case "tier1":
                            tier = 1;
                            break;
                        case "tier2":
                            tier = 2;
                            break;
                        case "tier3":
                            tier = 3;
                            break;
                        default:
                            break;

                    }

                    int remaining = dough.getItemDamage();
                    if(this.isFermenting()) {
                        int lvls = 6 - remaining;
                        int interval = totalFermentTime / (6 - lvls);

                        if (fermentTime % interval == 0.0 && fermentTime != 0) {
                            world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, (world.getBlockState(pos).getValue(JarBlock.FERMENTING_STATE)) + 1).withProperty(JarBlock.TIER, tier));
                        }
                    } else {
                        world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 6 - remaining).withProperty(JarBlock.TIER, tier));
                    }

                }
                else if (fermentTime <= totalFermentTime * 1 / 5)
                    world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 1));
                else if (fermentTime <= totalFermentTime * 2 / 5)
                    world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 2));
                else if (fermentTime <= totalFermentTime * 3 / 5)
                    world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 3));
                else if (fermentTime <= totalFermentTime * 4 / 5)
                    world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 4));
                else if (fermentTime <= totalFermentTime)
                    world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 5));
            }
            else if(!this.jarItemStacks.get(2).isEmpty()){
                switch(jarItemStacks.get(2).getTagCompound().getString("Tier")){
                    case "tier1":
                        tier = 1;
                        break;
                    case "tier2":
                        tier = 2;
                        break;
                    case "tier3":
                        tier = 3;
                        break;
                    default:
                        break;
                }
                world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 6).withProperty(JarBlock.TIER, tier));
            }
            else
                world.setBlockState(pos, ModBlocks.JAR_BLOCK.getDefaultState().withProperty(JarBlock.FERMENTING_STATE, 0));

            if(this.canFerment()){


                int result = this.getFermentedResult();
                fermentTime++;
                totalFermentTime = this.getFermentTime();



                if(fermentTime == totalFermentTime){

                    NBTTagCompound compound = new NBTTagCompound();


                    if(dough.hasTagCompound()){
                        compound = this.jarItemStacks.get(0).getTagCompound();
                    }

                    fermentItem();

                    //Set the potion type
                    if(result == this.getTier(dough) && !(dough.getItem() == Items.WHEAT)){

                        if( this.potionType.equals("minecraft:empty"))
                            jarItemStacks.get(2).setTagCompound(compound);
                        else{
                            compound.setString("Effect", potionType);
                            jarItemStacks.get(2).setTagCompound(compound);
                        }

                    }
                    else if(!this.potionType.equals("")){

                        NBTTagCompound nbt = jarItemStacks.get(2).getTagCompound();
                        if(!this.potionType.equals("minecraft:empty"))
                            nbt.setString("Effect", potionType);
                        else if(dough.hasTagCompound()){
                            nbt.setString("Effect", dough.getTagCompound().getString("Effect"));
                        }
                        jarItemStacks.get(2).setTagCompound(nbt);

                    }
                    potionType = "";


                    this.removeStackFromSlot(1);
                    fermentTime = 0;



                    flag = true;
                }
            }
        }
        if(flag) {
            this.markDirty();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    public int getTier(ItemStack stack){
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = new NBTTagCompound();

            if (nbt.getString("Tier").equals("tier1")) {
                return 1;
            } else if (nbt.getString("Tier").equals("tier2")) {
                return 2;
            } else if (nbt.getString("Tier").equals("tier3")) {
                return 3;
            } else return 0;
        } else
            return -1;
    }

    private boolean canFerment() {

        int expected = getFermentedResult();
        //if there is no recipe cannot ferment


        if (expected == 0) {
            return false;
        } else {
            //Output
            ItemStack output = this.jarItemStacks.get(2);
            //If output is empty can ferment
            if (output.isEmpty()) {
                if(expected != 0 && isSaturated)
                    return true;
            }
            //If the output slot is not empty cannot ferment
            else if(!output.isEmpty())
                return false;
        }
        return false;
    }

    public void fermentItem() {
        if (this.canFerment()) {

            int result = getFermentedResult();

            ItemStack starter = new ItemStack(ModItems.SOURDOUGH_STARTER);
            NBTTagCompound nbt = new NBTTagCompound();
            if(result == 1){
                nbt.setString("Tier", "tier1");
                starter.setTagCompound(nbt);
            }
            else if(result == 2){
                nbt.setString("Tier", "tier2");
                starter.setTagCompound(nbt);
            } else if (result == 3) {
                nbt.setString("Tier", "tier3");
                starter.setTagCompound(nbt);
            }

            this.jarItemStacks.set(2, starter);
            this.jarItemStacks.set(0, ItemStack.EMPTY);
            this.setSlot(1, ItemStack.EMPTY);

            isSaturated = false;

            this.markDirty();
        }

    }

    public int getFermentedResult(){

        ItemStack dough = this.jarItemStacks.get(0);
        ItemStack fermentAid = this.jarItemStacks.get(1);

        if(dough.isItemDamaged()){
            if(dough.hasTagCompound()) {

                NBTTagCompound nbt = dough.getTagCompound();

                if (nbt.hasKey("Tier")) {
                    if (nbt.getString("Tier").equals("tier1") && fermentAid.getItem() == Items.SUGAR)
                        return 1;
                    else if (nbt.getString("Tier").equals("tier2") && fermentAid.getItem() == Items.NETHER_WART || nbt.getString("Tier").equals("tier1") && fermentAid.getItem() == Items.NETHER_WART && dough.getItemDamage() == 3)
                        return 2;
                    else if(nbt.getString("Tier").equals("tier3") && fermentAid.getItem() == Items.CHORUS_FRUIT || nbt.getString("Tier").equals("tier2") && fermentAid.getItem() == Items.CHORUS_FRUIT && dough.getItemDamage() == 3)
                        return 3;
                    else
                        return 0;
                }
            }
        }
        else if(dough.getItem() == Items.WHEAT && fermentAid.getItem() == Items.SUGAR){
            return 1;
        }

        return 0;

    }


    public boolean isItemValidForSlot(int index, ItemStack stack){

        //output
        if(index == 2){
            return false;
        }
        //input1
        else if(index == 0) {
            if (stack.getItem() instanceof ItemSourdoughStarter)
                return true;

            else
                return false;
        }
        //input2
        else if(index == 1){
            if(stack.getItem() == Items.NETHER_WART ||
                stack.getItem() == Items.CHORUS_FRUIT)
                return true;
            else
                return false;
        }
        else
            return false;

    }

    public int getField(int p_getField_1_) {
        switch(p_getField_1_) {
            case 0:
                return this.fermentTime;
            case 1:
                return this.totalFermentTime;
            default:
                return 0;
        }
    }

    public void setField(int p_setField_1_, int p_setField_2_) {
        switch(p_setField_1_) {
            case 0:
                this.fermentTime = p_setField_2_;
                break;
            case 1:
                this.totalFermentTime = p_setField_2_;
        }

    }

    public int getFieldCount() {
        return 2;
    }

    public void clear() {
        this.jarItemStacks.clear();
    }

    public NonNullList<ItemStack> getJarItemStacks(){
        return jarItemStacks;
    }

    public boolean setSlot(int idx, ItemStack itemstack){

        jarItemStacks.set(idx, itemstack);

        this.markDirty();

        return true;

    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState old, IBlockState news) {
        return (old.getBlock() != news.getBlock());
    }
}
