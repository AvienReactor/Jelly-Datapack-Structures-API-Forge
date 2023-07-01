package com.jejukon.jellystructures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ModHandlers {
    public static String getStructorName(String new_string){
        String out_string = new_string.substring(12);
        out_string = ("ad_" + out_string);
        return out_string;
    }

    public static File getBoy(String path, String file_name){
        File directory = new File("");
        directory = new File(directory.getAbsolutePath() + path);

        JellyStructures.LOGGER.log(Level.WARN, "FileName: {}", directory.toString());

        if (!directory.isDirectory()){
            JellyStructures.LOGGER.log(Level.ERROR, "Directory not found: {}", directory.toString());
        }
        else {
            File[] files = directory.listFiles(); //list all content in directory

            for (File file : files) {
                JellyStructures.LOGGER.log(Level.ERROR, "File found 1: {}", file);
                if (file.isFile()) { //Checks if it's a file
                    if (file.getName().equals(file_name + ".json")) { //Check if file name matches
                        return file;
                    }
                }
            }
        }
        return null;
    }

    public static BlockPos reeeblock(BlockState currentBS, BlockPos shiverstonePos, PieceGeneratorSupplier.Context<JigsawConfiguration> shiverstoneContext, String structure_name, int random_y, int max_y, int min_y){
        boolean air = currentBS.isAir();
        boolean cleared = false;
        BlockPos tmpPos = null;

        if(air){ //Pos is an air block :1
            tmpPos = shiverstonePos;
            JellyStructures.LOGGER.log(Level.WARN, "{} AIR at {} /// START", structure_name, shiverstonePos);
            int groundCounter = 0; //Used to count amount of solid blocks
            for (int i = random_y; i < max_y; i++){ //Drill up till solid block found :2
                currentBS = shiverstoneContext.chunkGenerator().getBaseColumn(shiverstonePos.getX(),shiverstonePos.getZ(), LevelHeightAccessor.create(min_y,max_y)).getBlock(i);
                if (groundCounter >= 3 && currentBS.isAir()) { //Drill suitable spot found
                    JellyStructures.LOGGER.log(Level.WARN, "{} suitable spot found /// BUILD", structure_name);
                    shiverstonePos = new BlockPos(shiverstoneContext.chunkPos().getMiddleBlockX(), i - 1, shiverstoneContext.chunkPos().getMiddleBlockZ());
                    break;
                }
                if (!currentBS.isAir()){ //Solid block found
                    groundCounter = groundCounter + 1;
                } else { //Air block found
                    groundCounter = 0;
                }
                //shiverstonePos = new BlockPos(shiverstoneContext.chunkPos().getMiddleBlockX(), -999, shiverstoneContext.chunkPos().getMiddleBlockZ());
                JellyStructures.LOGGER.log(Level.WARN, "{} new spot check at Y:{} groundCounter: {} /// SEARCHING", structure_name, i, groundCounter);
            }
        }
        else {
            JellyStructures.LOGGER.log(Level.WARN, "Failed ground fouind first: {} /// FAILED", structure_name);
        }

        if (tmpPos == shiverstonePos){
            shiverstonePos = new BlockPos(shiverstonePos.getX(), 130, shiverstonePos.getZ());
        }
        return shiverstonePos;
    }
}
