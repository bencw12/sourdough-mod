package com.bencw12.sourdough.init;

import com.bencw12.sourdough.blocks.BreadOvenBlock;
import com.bencw12.sourdough.blocks.CuttingBoardBlock;
import com.bencw12.sourdough.blocks.JarBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {



    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block JAR_BLOCK = new JarBlock("jar_block");
    public static final Block CUTTING_BOARD_BLOCK = new CuttingBoardBlock("cutting_board_block");
    public static final Block BREAD_OVEN_BLOCK = new BreadOvenBlock("bread_oven_block");

}
