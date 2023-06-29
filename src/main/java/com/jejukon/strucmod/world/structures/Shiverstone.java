package com.jejukon.strucmod.world.structures;

import com.jejukon.strucmod.StrucMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Level;

import javax.json.Json;
import java.io.File;
import java.io.Reader;
import java.nio.file.DirectoryStream;

public class Shiverstone extends StructureFeature<JigsawConfiguration> {

    public Shiverstone(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            int dim_buildlimit = 150;
            int min_y = 30;
            int max_y = 100;
            int random_y = (int)Math.floor(Math.random()*(max_y - min_y + 1) + min_y);
            String structure_name = "Shiverstone";



            JigsawConfiguration shiverstoneConfig = new JigsawConfiguration(context.config().startPool(), 50);
            PieceGeneratorSupplier.Context<JigsawConfiguration> shiverstoneContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), shiverstoneConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());

            ResourceLocation structure_location = shiverstoneConfig.startPool().value().getName();
            structure_name = structure_name + " :*: " + structure_location.toString();

            //region Files
            String file_name = structure_location.toString();
            StrucMod.LOGGER.log(Level.WARN, "FileName: {}", file_name);

            String directoryPath = ;
            //File directory = new File(directoryPath);

            //if (directory.exists()){
            //    directoryPath = "a";
            //}
            //File[] files = directory.listFiles(); //list all content in directory

            //for (File file : files){
            //    if (file.isFile()){ //Checks if it's a file
            //        if (file.getName().equals(structure_location.toString())){ //Check if file name matches
            //            directoryPath = "a";
            //        }
            //    }
            //}
            //endregion

            //ResourceLocation structure_miny = shiverstoneConfig.startPool().value();

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