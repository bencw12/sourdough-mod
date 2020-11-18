package com.bencw12.sourdough;

import com.bencw12.sourdough.proxy.CommonProxy;
import com.bencw12.sourdough.tileentity.TileEntityBreadOven;
import com.bencw12.sourdough.tileentity.TileEntityCuttingBoard;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import com.bencw12.sourdough.tileentity.render.JarTESR;
import com.bencw12.sourdough.util.Reference;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.bencw12.sourdough.util.Reference.MOD_ID;

@Mod(modid = MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Main {

    @Mod.Instance(MOD_ID)
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void PreInit(FMLPreInitializationEvent event){

        proxy.registerRenders();
        GameRegistry.registerTileEntity(TileEntityJar.class, MOD_ID + "TileEntityJar");
        GameRegistry.registerTileEntity(TileEntityCuttingBoard.class, MOD_ID + "TileEntityCuttingBoard");
        GameRegistry.registerTileEntity(TileEntityBreadOven.class, MOD_ID + "TileEntityBreadOven");

    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){




    }

    @Mod.EventHandler
    public static void PostInit(FMLPostInitializationEvent event){


    }


}
