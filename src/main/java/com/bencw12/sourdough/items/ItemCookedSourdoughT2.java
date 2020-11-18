package com.bencw12.sourdough.items;

import com.bencw12.sourdough.util.SourdoughUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemCookedSourdoughT2 extends ItemCookedSourdough{
    public ItemCookedSourdoughT2(){
        super("sourdough_cooked_tier2", 16, 0.5F);

    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
        if(this.isInCreativeTab(tab)){
            for(SourdoughUtil.Effect effect : SourdoughUtil.Effect.values()) {
                items.add(SourdoughUtil.createTier(new ItemStack(this), SourdoughUtil.Tier.T2, effect));
            }
        }
    }
}
