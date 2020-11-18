package com.bencw12.sourdough.items;

import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemCookedSourdoughT3 extends ItemCookedSourdough{

    public ItemCookedSourdoughT3(){
        super("sourdough_cooked_tier3", 20, 0.5F);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
        if(this.isInCreativeTab(tab)){
            for(SourdoughUtil.Effect effect : SourdoughUtil.Effect.values()) {
                items.add(SourdoughUtil.createTier(new ItemStack(this), SourdoughUtil.Tier.T3, effect));
            }
        }
    }
}
