package com.bencw12.sourdough.blocks;

import com.bencw12.sourdough.Main;
import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBase extends Block implements IHasModel {


    public BlockBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.DECORATIONS);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {

        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");

    }

    @Override
    public boolean onBlockActivated(World p_onBlockActivated_1_, BlockPos p_onBlockActivated_2_, IBlockState p_onBlockActivated_3_, EntityPlayer p_onBlockActivated_4_, EnumHand p_onBlockActivated_5_, EnumFacing p_onBlockActivated_6_, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        return super.onBlockActivated(p_onBlockActivated_1_, p_onBlockActivated_2_, p_onBlockActivated_3_, p_onBlockActivated_4_, p_onBlockActivated_5_, p_onBlockActivated_6_, p_onBlockActivated_7_, p_onBlockActivated_8_, p_onBlockActivated_9_);
    }
}
