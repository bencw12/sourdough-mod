package com.bencw12.sourdough.proxy;

import com.bencw12.sourdough.Main;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import com.bencw12.sourdough.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id){ }


    public void registerTileEntities(){

    }

    public void registerRenders(){



    }

    public <T extends TileEntityJar> void registerTileEntitySpecialRenderer(Class<T> type){}


    public void preInit(FMLInitializationEvent e){

    }


    public void init(FMLInitializationEvent e){



    }
}
