package com.bencw12.sourdough.tileentity.render;

import com.bencw12.sourdough.blocks.JarBlock;
import com.bencw12.sourdough.tileentity.TileEntityJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class JarTESR extends TileEntitySpecialRenderer<TileEntityJar> {

    @Override
    public void render(TileEntityJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        //Translate to the location of tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderItem(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }


    private void renderItem(TileEntityJar te){

        int stateVal = this.getWorld().getBlockState(te.getPos()).getValue(JarBlock.FERMENTING_STATE);

        if(!te.getJarItemStacks().get(1).isEmpty()) {


            ItemStack fermentAid = te.getJarItemStacks().get(1);

            if (stateVal > 0 && stateVal < 6) {

                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableLighting();
                GlStateManager.pushMatrix();

                if (stateVal == 1) {
                    GlStateManager.translate(0.4, 0.15, 0.4);
                } else if (stateVal == 2) {
                    GlStateManager.translate(0.4, 0.22, 0.4);
                } else if (stateVal == 3) {
                    GlStateManager.translate(0.4, 0.29, 0.4);
                } else if (stateVal == 4) {
                    GlStateManager.translate(0.4, 0.36, 0.4);
                } else if (stateVal == 5) {
                    GlStateManager.translate(0.4, 0.42, 0.4);
                }

                GlStateManager.scale(0.3f, 0.3f, 0.3f);
                GlStateManager.rotate(90f, 1, 0, 0);

                Minecraft.getMinecraft().getRenderItem().renderItem(fermentAid, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.translate(0f, 0.7f, 0f);
                GlStateManager.rotate(35f, 1f, 1f, 1f);
                Minecraft.getMinecraft().getRenderItem().renderItem(fermentAid, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.rotate(325f, 1f, 1f, 1f);
                GlStateManager.translate(0.6, -0.25, -0.1f);
                GlStateManager.rotate(-35f, 0f, 1f, 1f);
                Minecraft.getMinecraft().getRenderItem().renderItem(fermentAid, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.rotate(35f, 0f, 1f, 1f);
                GlStateManager.translate(-0.2f, 0.3f, 0.2f);
                Minecraft.getMinecraft().getRenderItem().renderItem(fermentAid, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.popMatrix();
            }

        }

    }
}
