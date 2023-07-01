package com.jejukon.jellystructures.registry;

import com.jejukon.jellystructures.JellyStructures;
import com.jejukon.jellystructures.world.structures.Shiverstone;
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
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, JellyStructures.MOD_ID);

    //region OverWorld
    static String overworldName = "undergarden";
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> OVERWORLDSTRUCTS = STRUCTURES.register(overworldName, () -> new Shiverstone(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> OVERWORLDSTRUCTS_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(JellyStructures.MOD_ID, overworldName));
    //endregion

    //region Nether
    static String netherName = "shiverstone";
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> NETHERSTRUCTS = STRUCTURES.register(netherName, () -> new Shiverstone(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> NETHERSTRUCTS_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(JellyStructures.MOD_ID, netherName));
    //endregion

    //region Undergarden
    static String undergardenName = "undergarden";
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> UNDERGARDENSTRUCTS = STRUCTURES.register(undergardenName, () -> new Shiverstone(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> UNDERGARDENSTRUCTS_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(JellyStructures.MOD_ID, undergardenName));
    //endregion

}