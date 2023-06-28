package com.jejukon.strucmod;

import com.jejukon.strucmod.registry.SMStructures;
import com.jejukon.strucmod.world.structures.ModStructures;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(StrucMod.MOD_ID)
public class StrucMod {
    public static final String MOD_ID = "strucmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogManager.getLogger();

    public StrucMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModStructures.register(eventBus);

        eventBus.addListener(this::setup);

        DeferredRegister<?>[] registers = {
                SMStructures.STRUCTURES
        };

        for (DeferredRegister<?> register : registers){
            register.register(eventBus);
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}