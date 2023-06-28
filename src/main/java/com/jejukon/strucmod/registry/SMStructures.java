package com.jejukon.strucmod.registry;

import com.jejukon.strucmod.StrucMod;
import com.jejukon.strucmod.world.structures.Shiverstone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMStructures {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, StrucMod.MOD_ID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> SHIVERSTONES = STRUCTURES.register("shiverstone", () -> new Shiverstone(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> SHIVERSTONE_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(StrucMod.MOD_ID, "shiverstone"));
}