package com.bencw12.sourdough.proxy;

import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import com.bencw12.sourdough.tileentity.render.JarTESR;
import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.bencw12.sourdough.util.Reference.MOD_ID;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{

    public void registerItemRenderer(Item item, int meta, String id){

        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));

    }


    @Override
    public void init(FMLInitializationEvent e){


    }

    @Override
    public void registerRenders(){

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJar.class, new JarTESR());


    }


    @Override
    public void preInit(FMLInitializationEvent e) {
        super.preInit(e);

        OBJLoader.INSTANCE.addDomain(MOD_ID);

    }
}
