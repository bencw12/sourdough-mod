package com.bencw12.sourdough.init;

import com.bencw12.sourdough.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> ITEMS =  new ArrayList<Item>();

    public static final Item SOURDOUGH_STARTER = new ItemSourdoughStarter();
    public static final Item SOURDOUGH_COOKED_T1 = new ItemCookedSourdoughT1();
    public static final Item SOURDOUGH_COOKED_T2 = new ItemCookedSourdoughT2();
    public static final Item SOURDOUGH_COOKED_T3 = new ItemCookedSourdoughT3();
    public static final Item SOURDOUGH = new ItemSourdough();

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event){
        event.getRegistry().register(SOURDOUGH_STARTER);
        event.getRegistry().register(SOURDOUGH);

    }



}
