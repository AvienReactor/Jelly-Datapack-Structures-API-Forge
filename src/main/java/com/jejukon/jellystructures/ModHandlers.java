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
        String out_string = new_string.substring(new_string.indexOf("tp_")+3);
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
            File[] folders = directory.listFiles(); //list all content in directory


            for (File folder : folders) { //Reads each folder in the directory
                if (folder.isDirectory()) {
                    File[] files = folder.listFiles(); //list all content in folder
                    for (File file : files) { //Reads each file in folder
                        if (file.isFile()) { //Checks if it's a file
                            if (file.getName().equals(file_name + ".json")) { //Check if file name matches
                                JellyStructures.LOGGER.log(Level.ERROR, "File found: {}", file);
                                return file;
                            }
                        }
                    }
                } else if (folder.isFile()) { //Checks if it's a file
                    if (folder.getName().equals(file_name + ".json")) { //Check if file name matches
                        JellyStructures.LOGGER.log(Level.ERROR, "File found: {}", folder);
                        return folder;
                    }
                }
            }
        }
        return null;
    }

    public static String[] getIntData(String kubejs_file_name){
        int elements = 6;
        String[] temp = new String[elements];
        File selected = null;

        selected = getFile(("/kubejs/data/" + JellyStructures.MOD_ID + "/additions/"), kubejs_file_name);

        if(selected != null) {
            try (FileReader fileReader = new FileReader(selected)) {
                JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                //Get the info
                temp[0] = jsonObject.get("min_y").getAsString();//Ints
                temp[1] = jsonObject.get("max_y").getAsString();
                temp[2] = jsonObject.get("debug_mode").getAsString();
                //temp[3] = jsonObject.get("out_of_bounds_y").getAsString();
                temp[4] = jsonObject.get("gen_mode").getAsString();
                temp[5] = jsonObject.get("exact_y").getAsString();
                //temp[3] = jsonObject.get("spawn_in_air").getAsString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //JellyStructures.LOGGER.log(Level.ERROR, "File found 2: {}", kubejs_file_name);
        }
        
        return temp;
    }

    public static BlockPos getCaveBlockPos(BlockState currentBS, BlockPos blockPos, PieceGeneratorSupplier.Context<JigsawConfiguration> context, String structure_name, int random_y, int max_y, int min_y, int fail_y){
        boolean air = currentBS.isAir();
        BlockPos tmpPos = null;

        if(air){ //Pos is an air block :1
            tmpPos = blockPos;
            //JellyStructures.LOGGER.log(Level.WARN, "{} AIR at {} /// START", structure_name, blockPos); // Dev only
            int groundCounter = 0; //Used to count amount of solid blocks
            for (int i = random_y; i < max_y; i++){ //Drill up till solid block found :2
                currentBS = context.chunkGenerator().getBaseColumn(blockPos.getX(),blockPos.getZ(), LevelHeightAccessor.create(min_y,max_y)).getBlock(i);
                if (groundCounter >= 3 && currentBS.isAir()) { //Drill suitable spot found
                    //JellyStructures.LOGGER.log(Level.WARN, "{} suitable spot found /// BUILD", structure_name); // Dev only
                    blockPos = new BlockPos(context.chunkPos().getMiddleBlockX(), i - 1, context.chunkPos().getMiddleBlockZ());
                    break;
                }
                if (!currentBS.isAir()){ //Solid block found
                    groundCounter = groundCounter + 1;
                } else { //Air block found
                    groundCounter = 0;
                }
                //JellyStructures.LOGGER.log(Level.WARN, "{} new spot check at Y:{} groundCounter: {} /// SEARCHING", structure_name, i, groundCounter); // Dev only
            }
        }
        else {
            //JellyStructures.LOGGER.log(Level.WARN, "Failed ground found first: {} /// FAILED", structure_name); // Dev only
        }

        if (tmpPos == blockPos){ //Failed
            blockPos = new BlockPos(blockPos.getX(), fail_y, blockPos.getZ());
        }
        return blockPos;
    }

    public static BlockPos getSkyBlockPos(BlockState currentBS, BlockPos blockPos, int random_y, int fail_y){
        boolean air = currentBS.isAir();
        BlockPos tmpPos = null;

        blockPos = new BlockPos(blockPos.getX(), random_y, blockPos.getZ());

        if(!air){ //Is solid block
            blockPos = new BlockPos(blockPos.getX(), fail_y, blockPos.getZ());
        }

        return blockPos;
    }

    public static BlockPos getExactBlockPos(BlockState currentBS, BlockPos blockPos, int exact_y, int fail_y){
        blockPos = new BlockPos(blockPos.getX(), exact_y, blockPos.getZ());

        return blockPos;
    }

    public static BlockPos genModeHandler(String gen_mode, BlockState currentBS, BlockPos blockPos, PieceGeneratorSupplier.Context<JigsawConfiguration> context, String structure_name, int random_y, int max_y, int min_y, int fail_y, int exact_y){
        BlockPos tempPos = null;

        //Gen switch
        switch (gen_mode.toLowerCase()) {
            case "cave_gen" ->
                    tempPos = getCaveBlockPos(currentBS, blockPos, context, structure_name, random_y, max_y, min_y, fail_y);
            case "sky_gen" ->
                    tempPos = getSkyBlockPos(currentBS, blockPos, random_y, fail_y);
            case "exact_gen" -> tempPos = getExactBlockPos(currentBS, blockPos, exact_y, fail_y);
        }

        return tempPos;
    }

}
