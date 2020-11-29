package com.bencw12.sourdough.util;

import com.bencw12.sourdough.blocks.JarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SourdoughUtil {

    public static ItemStack createTier(ItemStack stack, Tier tier, Effect effect){
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Tier", tier.getName());
        nbt.setString("Effect", effect.getName());
        stack.setTagCompound(nbt);
        
        return stack;
    }
    
    public static String getRegistryNameFromNBT(ItemStack stack){

        String result = "";

        if(stack.hasTagCompound()){
            if(stack.getTagCompound().hasKey("Tier")){
                result += stack.getTagCompound().getString("Tier");
            }
            return result;
        }
        return "tier1";
    }

    public static enum Tier {
        T1("tier1"),
        T2("tier2"),
        T3("tier3");

        private String name;
        private Tier(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
    }

    public static enum Effect {
        NONE("minecraft:empty"),
        JUMP("minecraft:leaping"),
        LONG_JUMP("minecraft:long_leaping"),
        STRONG_JUMP("minecraft:strong_leaping"),
        NIGHT("minecraft:night_vision"),
        LONG_NIGHT("minecraft:long_night_vision"),
        INVIS("minecraft:invisibility"),
        LONG_INVIS("minecraft:long_invisibility"),
        FIRE("minecraft:fire_resistance"),
        LONG_FIRE("minecraft:long_fire_resistance"),
        SWIFTNESS("minecraft:swiftness"),
        STRONG_SWIFTNESS("minecraft:strong_swiftness"),
        LONG_SWIFTNESS("minecraft:long_swiftness"),
        SLOWNESS("minecraft:slowness"),
        STRONG_SLOWNESS("minecraft:strong_slowness"),
        LONG_SLOWNESS("minecraft:long_slowness"),
        WATER_BREATHING("minecraft:water_breathing"),
        LONG_WATER_BREATHING("minecraft:long_water_breathing"),
        HEALTH("minecraft:healing"),
        STRONG_HEALTH("minecraft:strong_healing"),
        HARMING("minecraft:harming"),
        STRONG_HARMING("minecraft:strong_harming"),
        POISON("minecraft:poison"),
        STRONG_POISON("minecraft:strong_poison"),
        LONG_POISON("minecraft:long_poison"),
        REGEN("minecraft:regeneration"),
        STRONG_REGEN("minecraft:strong_regeneration"),
        LONG_REGEN("minecraft:long_regeneration"),
        STRENGTH("minecraft:strength"),
        STRONG_STRENGTH("minecraft:strong_strength"),
        LONG_STRENGTH("minecraft:long_strength"),
        WEAKNESS("minecraft:weakness"),
        LONG_WEAKNESS("minecraft:long_weakness"),
        LUCK("minecraft:luck"),
        TURTLE("minecraft:turtle_master"),
        STRONG_TURTLE("minecraft:strong_turtle_master"),
        LONG_TURTLE_MASTER("minecraft:long_turtle_master");

        private String name;
        private Effect(String name) { this.name = name; }
        public String getName() { return this.name; }
    }
}
