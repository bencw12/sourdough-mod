package com.bencw12.sourdough.items;

import com.bencw12.sourdough.Main;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.util.IHasModel;
import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name){
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.FOOD);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {

        Main.proxy.registerItemRenderer(this, 0, "inventory");



    }
}
