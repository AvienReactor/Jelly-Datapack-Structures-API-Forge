package com.jejukon.strucmod.world.structures;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

public class Shiverstone extends StructureFeature<JigsawConfiguration> {

    public Shiverstone(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            //JigsawConfiguration catacombsConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Undergarden.MODID, "catacombs/catacombs_entrance")), 50);
            JigsawConfiguration shiverstoneConfig = new JigsawConfiguration(context.config().startPool(), 50);
            PieceGeneratorSupplier.Context<JigsawConfiguration> shiverstoneContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), shiverstoneConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());
            BlockPos shiverstonePos = new BlockPos(context.chunkPos().getMinBlockX(), 50, context.chunkPos().getMinBlockZ());
            return JigsawPlacement.addPieces(shiverstoneContext, PoolElementStructurePiece::new, shiverstonePos, false, false);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}