package com.bencw12.sourdough.items;

import com.bencw12.sourdough.Main;
import com.bencw12.sourdough.init.ModItems;
import com.bencw12.sourdough.util.IHasModel;
import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCookedSourdough extends ItemFood implements IHasModel {


    public ItemCookedSourdough(String name, int healAmount, float saturation){
        super(healAmount, saturation, false);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.FOOD);
        setHasSubtypes(true);
        setAlwaysEdible();

        ModItems.ITEMS.add(this);
    }





    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if(stack.hasTagCompound()) {

            NBTTagCompound nbt = stack.getTagCompound();

            String type = nbt.getString("Effect");

            //ADD MORE EFFECTS LATER
            if (type.equals("minecraft:leaping")) {
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 3600, 0, false, false));
            } else if (type.equals("minecraft:long_leaping"))
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 9600, 0, false, false));
            else if(type.equals("minecraft:strong_leaping"))
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1800, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.NIGHT.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_NIGHT.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.INVIS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.INVIS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.FIRE.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_FIRE.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.SWIFTNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_SWIFTNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1800, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_SWIFTNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.SLOWNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_SLOWNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 3, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_SLOWNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 4800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.WATER_BREATHING.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_WATER_BREATHING.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.HEALTH.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_HEALTH.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.HARMING.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_HARMING.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.POISON.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.POISON, 900, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_POISON.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.POISON, 420, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_POISON.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.POISON, 1800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.REGEN.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_REGEN.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 440, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_REGEN.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRENGTH.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 3600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.STRONG_STRENGTH.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1800, 1, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_STRENGTH.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 9600, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.WEAKNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.LONG_WEAKNESS.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 4800, 0, false, false));
            else if(type.equals(SourdoughUtil.Effect.LUCK.getName()))
                player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 9600, 0, false, false));


        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_addInformation_1_, @Nullable World p_addInformation_2_, List<String> p_addInformation_3_, ITooltipFlag p_addInformation_4_) {

        //ADD MORE EFFECTS LATER
        if(p_addInformation_1_.hasTagCompound()){
            NBTTagCompound nbt = p_addInformation_1_.getTagCompound();
            if(!nbt.getString("Effect").equals(SourdoughUtil.Effect.NONE.getName())){
                if(nbt.getString("Effect").equals(SourdoughUtil.Effect.JUMP.getName())){
                    p_addInformation_3_.add(TextFormatting.GREEN + "Jump Boost");
                }
                else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_JUMP.getName())){
                    p_addInformation_3_.add(TextFormatting.GREEN + "Jump Boost (8:00)");
                }
                else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_JUMP.getName())){
                    p_addInformation_3_.add(TextFormatting.GREEN + "Jump Boost II");
                }
                else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.NIGHT.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_BLUE + "Night Vision");
                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_NIGHT.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_BLUE + "Night Vision (8:00)");
                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.INVIS.getName())){
                    p_addInformation_3_.add(TextFormatting.GRAY + "Invisibility");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_INVIS.getName())){
                    p_addInformation_3_.add(TextFormatting.GRAY + "Invisibility (8:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.FIRE.getName())){
                    p_addInformation_3_.add(TextFormatting.GOLD + "Fire Resistance");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_FIRE.getName())){
                    p_addInformation_3_.add(TextFormatting.GOLD + "Fire Resistance (8:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.SWIFTNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.AQUA + "Swiftness");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_SWIFTNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.AQUA + "Swiftness II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_SWIFTNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.AQUA + "Swiftness (8:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.SLOWNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GRAY + "Slowness");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_SLOWNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GRAY + "Slowness IV");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_SLOWNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GRAY + "Slowness (4:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.WATER_BREATHING.getName())){
                    p_addInformation_3_.add(TextFormatting.BLUE + "Water Breathing");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_WATER_BREATHING.getName())){
                    p_addInformation_3_.add(TextFormatting.BLUE + "Water Breathing (8:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.HEALTH.getName())){
                    p_addInformation_3_.add(TextFormatting.LIGHT_PURPLE + "Instant Health");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.HARMING.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_RED + "Instant Damage");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_HARMING.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_RED + "Instant Damage II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.POISON.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Poison");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_POISON.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Poison II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_POISON.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Poison (1:30)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.REGEN.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_PURPLE + "Regeneration");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_REGEN.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_PURPLE + "Regeneration II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_REGEN.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_PURPLE + "Regeneration (1:30)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.WEAKNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GRAY + "Weakness");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_WEAKNESS.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GRAY + "Weakness (4:00)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LUCK.getName())){
                    p_addInformation_3_.add(TextFormatting.GREEN + "Luck");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.TURTLE.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Turtle Master");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_TURTLE.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Turtle Master II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_TURTLE_MASTER.getName())){
                    p_addInformation_3_.add(TextFormatting.DARK_GREEN + "Turtle Master (0:40)");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRENGTH.getName())){
                    p_addInformation_3_.add(TextFormatting.RED + "Strength");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.STRONG_STRENGTH.getName())){
                    p_addInformation_3_.add(TextFormatting.RED + "Strength II");

                } else if(nbt.getString("Effect").equals(SourdoughUtil.Effect.LONG_STRENGTH.getName())){
                    p_addInformation_3_.add(TextFormatting.RED + "Strength (8:00)");

                }
            }
        }
    }


    @Override
    public boolean hasEffect(ItemStack stack) {
        if(stack.hasTagCompound()){
            NBTTagCompound nbt = stack.getTagCompound();
            if(nbt.hasKey("Effect")){
                if(!nbt.getString("Effect").equals("minecraft:empty"))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + SourdoughUtil.getRegistryNameFromNBT(stack);
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
