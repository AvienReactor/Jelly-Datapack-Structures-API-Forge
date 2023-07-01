package com.jejukon.jellystructures;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ModHandlers {
    public static String getStructorName(String new_string){
        String out_string = new_string.substring(12);
        out_string = ("ad_" + out_string);
        return out_string;
    }

    public static File getFile(String path, String file_name){
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

    public static String[] getIntData(String additions_foldername, String kubejs_file_name){
        int elements = 2;
        String[] temp = new String[elements];
        File selected = null;

        selected = getFile(("/kubejs/data/" + JellyStructures.MOD_ID + "/additions/" + additions_foldername), kubejs_file_name);

        if(selected != null) {
            try (FileReader fileReader = new FileReader(selected)) {
                JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                //Get the info
                temp[0] = jsonObject.get("min_y").getAsString();
                temp[1] = jsonObject.get("max_y").getAsString();
                //temp[2] = jsonObject.get("spawn_in_air").getAsString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JellyStructures.LOGGER.log(Level.ERROR, "File found 2: {}", kubejs_file_name);
        }
        
        return temp;
    }

    public static BlockPos getBlockPos(BlockState currentBS, BlockPos blockPos, PieceGeneratorSupplier.Context<JigsawConfiguration> context, String structure_name, int random_y, int max_y, int min_y){
        boolean air = currentBS.isAir();
        boolean cleared = false;
        BlockPos tmpPos = null;
        int fail_y = 130;

        if(air){ //Pos is an air block :1
            tmpPos = blockPos;
            JellyStructures.LOGGER.log(Level.WARN, "{} AIR at {} /// START", structure_name, blockPos);
            int groundCounter = 0; //Used to count amount of solid blocks
            for (int i = random_y; i < max_y; i++){ //Drill up till solid block found :2
                currentBS = context.chunkGenerator().getBaseColumn(blockPos.getX(),blockPos.getZ(), LevelHeightAccessor.create(min_y,max_y)).getBlock(i);
                if (groundCounter >= 3 && currentBS.isAir()) { //Drill suitable spot found
                    JellyStructures.LOGGER.log(Level.WARN, "{} suitable spot found /// BUILD", structure_name);
                    blockPos = new BlockPos(context.chunkPos().getMiddleBlockX(), i - 1, context.chunkPos().getMiddleBlockZ());
                    break;
                }
                if (!currentBS.isAir()){ //Solid block found
                    groundCounter = groundCounter + 1;
                } else { //Air block found
                    groundCounter = 0;
                }
                JellyStructures.LOGGER.log(Level.WARN, "{} new spot check at Y:{} groundCounter: {} /// SEARCHING", structure_name, i, groundCounter);
            }
        }
        else {
            JellyStructures.LOGGER.log(Level.WARN, "Failed ground fouind first: {} /// FAILED", structure_name);
        }

        if (tmpPos == blockPos){ //Failed
            blockPos = new BlockPos(blockPos.getX(), fail_y, blockPos.getZ());
        }
        return blockPos;
    }
}
