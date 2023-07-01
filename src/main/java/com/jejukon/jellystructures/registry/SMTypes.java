package com.jejukon.jellystructures.registry;

import com.jejukon.jellystructures.JellyStructures;
import com.jejukon.jellystructures.world.structures.UniversalGen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMTypes {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, JellyStructures.MOD_ID);

    //region UniversalType
    static String universalName = "universal";
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> OVERWORLDSTRUCTS = STRUCTURES.register(universalName, () -> new UniversalGen(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> OVERWORLDSTRUCTS_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(JellyStructures.MOD_ID, universalName));
    //endregion
}