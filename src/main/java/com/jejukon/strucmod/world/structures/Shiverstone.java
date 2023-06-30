package com.jejukon.strucmod.world.structures;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jejukon.strucmod.ModHandlers;
import com.jejukon.strucmod.StrucMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.apache.logging.log4j.Level;

import java.io.*;

public class Shiverstone extends StructureFeature<JigsawConfiguration> {

    public Shiverstone(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            int dim_buildlimit = 150;
            int min_y = 30;
            int max_y = 100;
            String structure_name = "Shiverstone";
            String foldername = "nether";



            JigsawConfiguration shiverstoneConfig = new JigsawConfiguration(context.config().startPool(), 50);
            PieceGeneratorSupplier.Context<JigsawConfiguration> shiverstoneContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), shiverstoneConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());

            ResourceLocation structure_location = shiverstoneConfig.startPool().value().getName();
            structure_name = structure_name + " :*: " + structure_location.toString();

            //region Files
            String kubejs_file_name = ModHandlers.getStructorName(structure_location.toString());
            StrucMod.LOGGER.log(Level.WARN, "Fixed_strucname: {}", kubejs_file_name);

            String file_name = structure_location.getPath();


            File directory = new File("");
            directory = new File(directory.getAbsolutePath() + ("/kubejs/data/" + StrucMod.MOD_ID + "/additions/" + foldername));

            StrucMod.LOGGER.log(Level.WARN, "FileName: {}", directory.toString());

            if (!directory.isDirectory()){
                StrucMod.LOGGER.log(Level.ERROR, "Directory not found: {}", directory.toString());
            }
            else {
                File[] files = directory.listFiles(); //list all content in directory

                for (File file : files) {
                    StrucMod.LOGGER.log(Level.ERROR, "File found 1: {}", file);
                    if (file.isFile()) { //Checks if it's a file
                        if (file.getName().equals(kubejs_file_name + ".json")) { //Check if file name matches



                            try (FileReader fileReader = new FileReader(file)){
                                JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                                //Get the info
                                min_y = jsonObject.get("min_y").getAsInt();
                                max_y = jsonObject.get("max_y").getAsInt();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            StrucMod.LOGGER.log(Level.ERROR, "File found 2: {}", kubejs_file_name);
                        }
                    }
                }
            }
            //endregion

            int random_y = (int)Math.floor(Math.random()*(max_y - min_y + 1) + min_y);
            BlockPos shiverstonePos = new BlockPos(context.chunkPos().getMiddleBlockX(), random_y, context.chunkPos().getMiddleBlockZ());

            //region Air check
            BlockState currentBS = shiverstoneContext.chunkGenerator().getBaseColumn(shiverstonePos.getX(),shiverstonePos.getZ(),LevelHeightAccessor.create(min_y,max_y)).getBlock(random_y);
            boolean air = currentBS.isAir();
            boolean cleared = false;

            if(air){ //Pos is an air block :1
                StrucMod.LOGGER.log(Level.WARN, "{} AIR at {} /// START", structure_name, shiverstonePos);
                int groundCounter = 0; //Used to count amount of solid blocks
                for (int i = random_y; i < max_y; i++){ //Drill up till solid block found :2
                    currentBS = shiverstoneContext.chunkGenerator().getBaseColumn(shiverstonePos.getX(),shiverstonePos.getZ(),LevelHeightAccessor.create(min_y,max_y)).getBlock(i);
                    if (groundCounter >= 3 && currentBS.isAir()) { //Drill suitable spot found
                        StrucMod.LOGGER.log(Level.WARN, "{} suitable spot found /// BUILD", structure_name);
                        shiverstonePos = new BlockPos(context.chunkPos().getMiddleBlockX(), i - 1, context.chunkPos().getMiddleBlockZ());
                        break;
                    }
                    if (!currentBS.isAir()){ //Solid block found
                        groundCounter = groundCounter + 1;
                    } else { //Air block found
                        groundCounter = 0;
                    }
                    StrucMod.LOGGER.log(Level.WARN, "{} new spot check at Y:{} groundCounter: {} /// SEARCHING", structure_name, i, groundCounter);
                }
                //Fail area
                //if()
                StrucMod.LOGGER.log(Level.WARN, "{} /// FAILED", structure_name);
                shiverstonePos = new BlockPos(context.chunkPos().getMiddleBlockX(), dim_buildlimit  + 20, context.chunkPos().getMiddleBlockZ());
            }
            //endregion

            StrucMod.LOGGER.log(Level.WARN, "{} final placement at {} /// FINAL", structure_name, shiverstonePos);
            return JigsawPlacement.addPieces(shiverstoneContext, PoolElementStructurePiece::new, shiverstonePos, false, false);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}