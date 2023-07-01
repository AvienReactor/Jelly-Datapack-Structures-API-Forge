package com.jejukon.jellystructures;

import com.jejukon.jellystructures.registry.SMTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(JellyStructures.MOD_ID)
public class JellyStructures {
    public static final String MOD_ID = "jellystructures";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogManager.getLogger();

    public JellyStructures() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);

        DeferredRegister<?>[] registers = {
                SMTypes.STRUCTURES
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