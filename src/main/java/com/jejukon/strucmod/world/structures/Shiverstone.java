package com.jejukon.strucmod.world.structures;

import com.jejukon.strucmod.StrucMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
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

public class Shiverstone extends StructureFeature<JigsawConfiguration> {

    public Shiverstone(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            int min_y = 30;
            int max_y = 100;
            int random_y = (int)Math.floor(Math.random()*(max_y - min_y + 1) + min_y);
            //JigsawConfiguration catacombsConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Undergarden.MODID, "catacombs/catacombs_entrance")), 50);
            JigsawConfiguration shiverstoneConfig = new JigsawConfiguration(context.config().startPool(), 50);
            PieceGeneratorSupplier.Context<JigsawConfiguration> shiverstoneContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), shiverstoneConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());

            BlockPos shiverstonePos = new BlockPos(context.chunkPos().getMiddleBlockX(), random_y, context.chunkPos().getMiddleBlockZ());

            BlockState blockes = shiverstoneContext.chunkGenerator().getBaseColumn(shiverstonePos.getX(),shiverstonePos.getZ(),LevelHeightAccessor.create(min_y,max_y)).getBlock(random_y);
            boolean air = blockes.isAir();

            if(air){
                StrucMod.LOGGER.log(Level.WARN, "Shiverstone AIR at {}", shiverstonePos);
            }

            StrucMod.LOGGER.log(Level.WARN, "Shiverstone at {} /// CALLED", shiverstonePos);
            return JigsawPlacement.addPieces(shiverstoneContext, PoolElementStructurePiece::new, shiverstonePos, false, false);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}