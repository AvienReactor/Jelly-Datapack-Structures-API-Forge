package com.jejukon.jellystructures.world.structures;

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

import java.util.Optional;

public class UniversalGen extends StructureFeature<JigsawConfiguration> {

    public UniversalGen(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            int min_y = 30;
            int max_y = 100;
            int exact_y = 50;
            int offset = 1;
            boolean debug_mode = false;
            String structure_name = JellyStructures.MOD_ID;
            String gen_mode = "cave_gen";

            JigsawConfiguration jellyConfig = new JigsawConfiguration(context.config().startPool(), 50);
            PieceGeneratorSupplier.Context<JigsawConfiguration> jellyContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), jellyConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());

            ResourceLocation structure_location = jellyConfig.startPool().value().getName();
            structure_name = structure_name + ":" + structure_location.toString();

            //region Files
            String kubejs_file_name = ModHandlers.getStructorName(structure_location.toString());
            //JellyStructures.LOGGER.log(Level.WARN, "Fixed name: {}", kubejs_file_name);

            String[] data = ModHandlers.getIntData(kubejs_file_name);

            //region Data Assigning
            if (data[0] != null) {min_y = Integer.parseInt(data[0]);}
            if (data[1] != null){max_y = Integer.parseInt(data[1]);}
            if (data[2] != null){debug_mode = Boolean.parseBoolean(data[2]);}
            if (data[3] !=null){gen_mode = data[3];}
            if (data[4] != null){exact_y = Integer.parseInt(data[4]);}
            if (data[5] != null){offset = Integer.parseInt(data[5]);}
            //endregion
            //endregion


            //region Debug additions file info
            if (debug_mode){
                JellyStructures.LOGGER.log(Level.ERROR, "Additions file found");
                JellyStructures.LOGGER.log(Level.ERROR, "Min_y = {}", min_y);
                JellyStructures.LOGGER.log(Level.ERROR, "Max_y = {}", max_y);
                JellyStructures.LOGGER.log(Level.ERROR, "Gen_mode = {}", gen_mode);
                JellyStructures.LOGGER.log(Level.ERROR, "Exact_y = {}", exact_y);
                JellyStructures.LOGGER.log(Level.ERROR, "Offset = {}", offset);
            }
            //endregion

            int fatal_y = (max_y + 20);
            int random_y = (int)Math.floor(Math.random()*(max_y - min_y + 1) + min_y); //Y value
            BlockPos jellyPos = new BlockPos(context.chunkPos().getMiddleBlockX(), random_y, context.chunkPos().getMiddleBlockZ());

            //region Air check
            BlockState currentBS = jellyContext.chunkGenerator().getBaseColumn(jellyPos.getX(),jellyPos.getZ(),LevelHeightAccessor.create(min_y,max_y)).getBlock(random_y);
            jellyPos = ModHandlers.genModeHandler(gen_mode, currentBS, jellyPos, jellyContext, structure_name, random_y,max_y,min_y, fatal_y, exact_y, offset);
            //endregion

            if (debug_mode){JellyStructures.LOGGER.log(Level.WARN, "{} final placement at {} /// FINAL SPOT", structure_name, jellyPos);}

            if (jellyPos.equals(new BlockPos(jellyPos.getX(), fatal_y, jellyPos.getZ()))) {
                if (debug_mode){JellyStructures.LOGGER.log(Level.WARN, "Final spot  is not suitable /// SKIP");}
                return Optional.empty();
            }
            if (debug_mode){JellyStructures.LOGGER.log(Level.WARN, "Structure spawned /// Success");}
            return JigsawPlacement.addPieces(jellyContext, PoolElementStructurePiece::new, jellyPos, false, false);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}