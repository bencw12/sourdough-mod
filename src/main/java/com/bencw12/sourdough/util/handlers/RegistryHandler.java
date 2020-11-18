package com.bencw12.sourdough.util.handlers;

import com.bencw12.sourdough.init.ModBlocks;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.util.IHasModel;
import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }



    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event){

        for(Item item : ModItems.ITEMS){
            if(item instanceof IHasModel){
                ((IHasModel)item).registerModels();
            }
        }

        for(Block block : ModBlocks.BLOCKS){
            if(block instanceof IHasModel){
                ((IHasModel)block).registerModels();
            }
        }



        ModelBakery.registerItemVariants(ModItems.SOURDOUGH_STARTER, new ResourceLocation(ModItems.SOURDOUGH_STARTER.getRegistryName().toString() + "_" + "tier1"));
        ModelBakery.registerItemVariants(ModItems.SOURDOUGH_STARTER, new ResourceLocation(ModItems.SOURDOUGH_STARTER.getRegistryName().toString() + "_" + "tier2"));
        ModelBakery.registerItemVariants(ModItems.SOURDOUGH_STARTER, new ResourceLocation(ModItems.SOURDOUGH_STARTER.getRegistryName().toString() + "_" + "tier3"));



        ModelLoader.setCustomMeshDefinition(ModItems.SOURDOUGH_STARTER, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack itemStack) {
                return new ModelResourceLocation(itemStack.getItem().getRegistryName() + "_" + SourdoughUtil.getRegistryNameFromNBT(itemStack), "inventory");

            }
        });

        for(SourdoughUtil.Tier tier : SourdoughUtil.Tier.values()){
            ModelBakery.registerItemVariants(ModItems.SOURDOUGH, new ResourceLocation(ModItems.SOURDOUGH.getRegistryName().toString() + "_" + tier.getName()));
        }

        ModelLoader.setCustomMeshDefinition(ModItems.SOURDOUGH, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack itemStack) {
                return new ModelResourceLocation(itemStack.getItem().getRegistryName() + "_" + SourdoughUtil.getRegistryNameFromNBT(itemStack), "inventory");
            }
        });
    }


}
