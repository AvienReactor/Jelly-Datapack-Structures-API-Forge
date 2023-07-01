package com.jejukon.jellystructures.world.structures;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jejukon.jellystructures.JellyStructures;
import com.jejukon.jellystructures.ModHandlers;
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
import java.util.Optional;

public class Shiverstone extends StructureFeature<JigsawConfiguration> {

    public Shiverstone(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
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
            JellyStructures.LOGGER.log(Level.WARN, "Fixed_strucname: {}", kubejs_file_name);

            File dec = ModHandlers.getBoy(("/kubejs/data/" + JellyStructures.MOD_ID + "/additions/" + foldername), kubejs_file_name);

            if(dec != null) {
                try (FileReader fileReader = new FileReader(dec)) {
                    JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                    //Get the info
                    min_y = jsonObject.get("min_y").getAsInt();
                    max_y = jsonObject.get("max_y").getAsInt();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                JellyStructures.LOGGER.log(Level.ERROR, "File found 2: {}", kubejs_file_name);
            }
            //endregion

            int random_y = (int)Math.floor(Math.random()*(max_y - min_y + 1) + min_y);
            BlockPos shiverstonePos = new BlockPos(context.chunkPos().getMiddleBlockX(), random_y, context.chunkPos().getMiddleBlockZ());

            //region Air check
            BlockState currentBS = shiverstoneContext.chunkGenerator().getBaseColumn(shiverstonePos.getX(),shiverstonePos.getZ(),LevelHeightAccessor.create(min_y,max_y)).getBlock(random_y);
            shiverstonePos = ModHandlers.reeeblock(currentBS, shiverstonePos, shiverstoneContext, structure_name, random_y,max_y,min_y);
            //endregion

            JellyStructures.LOGGER.log(Level.WARN, "{} final placement at {} /// FINAL", structure_name, shiverstonePos);

            if (shiverstonePos.equals(new BlockPos(shiverstonePos.getX(), 130, shiverstonePos.getZ()))) {
                return Optional.empty();
            }
            return JigsawPlacement.addPieces(shiverstoneContext, PoolElementStructurePiece::new, shiverstonePos, false, false);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}