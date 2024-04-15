package eu.lilithmonodia.dialogextractor.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinecraftLogTest {
    private MinecraftLog minecraftLog;

    /**
     * Sets up the test environment before each test case is executed.
     */
    @BeforeEach
    void setUp() {
        // In a real test, this should be a piece of a Minecraft log.
        String LOG_SAMPLE = """
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named ModelViewMat in the specified shader program.
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named ProjMat in the specified shader program.
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named ColorModulator in the specified shader program.
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named FogStart in the specified shader program.
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named FogEnd in the specified shader program.
                [15:50:05] [Render thread/WARN]: Shader clouds could not find uniform named FogColor in the specified shader program.
                [15:50:06] [Worker-Main-16/INFO]: [citresewn] Linking baked models to item CITs...
                [15:50:06] [Render thread/WARN]: Missing sound for event: minecraft:item.goat_horn.play
                [15:50:06] [Render thread/WARN]: Missing sound for event: minecraft:entity.goat.screaming.horn_break
                [15:50:06] [Render thread/INFO]: OpenAL initialized on device OpenAL Soft on 5 - MSI G27C4 E2 (2- AMD High Definition Audio Device)
                [15:50:06] [Render thread/INFO]: Initializing Sound Physics
                [15:50:06] [Render thread/INFO]: EFX Extension recognized
                [15:50:06] [Render thread/INFO]: Max auxiliary sends: 4
                [15:50:06] [Render thread/INFO]: Aux slot 1 created
                [15:50:06] [Render thread/INFO]: Aux slot 2 created
                [15:50:06] [Render thread/INFO]: Aux slot 3 created
                [15:50:06] [Render thread/INFO]: Aux slot 4 created
                [15:50:06] [Render thread/INFO]: EFX ready
                [15:50:06] [Render thread/INFO]: Sound engine started
                [15:50:06] [Render thread/INFO]: Created: 4096x4096x0 minecraft:textures/atlas/blocks.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 512x512x4 minecraft:textures/atlas/signs.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 1024x1024x4 minecraft:textures/atlas/banner_patterns.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 1024x1024x4 minecraft:textures/atlas/shield_patterns.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 2048x2048x4 minecraft:textures/atlas/armor_trims.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 256x128x4 minecraft:textures/atlas/decorated_pot.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 512x512x4 minecraft:textures/atlas/chest.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 1024x512x4 minecraft:textures/atlas/shulker_boxes.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 1024x512x4 minecraft:textures/atlas/beds.png-atlas
                [15:50:06] [Render thread/WARN]: Shader rendertype_entity_translucent_emissive could not find sampler named Sampler2 in the specified shader program.
                [15:50:06] [Render thread/INFO]: Created: 1024x512x0 minecraft:textures/atlas/particles.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 512x512x0 minecraft:textures/atlas/paintings.png-atlas
                [15:50:06] [Render thread/INFO]: Created: 256x256x0 minecraft:textures/atlas/mob_effects.png-atlas
                [15:50:06] [Render thread/INFO]: Loaded AmbientEngine 'basic' v2.3.4. 11 dimension(s), 10 features, 11 groups, 35 regions, 62 sounds, 4 solids and 2 biome types
                [15:50:06] [Render thread/INFO]: Loaded 0 built-in, 0 custom block tags
                [15:50:06] [Render thread/INFO]: Loaded 6 built-in, 0 custom item tags
                [15:50:06] [Render thread/INFO]: Loaded 0 built-in, 0 custom entity tags
                [15:50:06] [Render thread/INFO]: Loaded 0 built-in, 0 custom biome tags
                [15:50:06] [Render thread/INFO]: Stopping worker threads
                [15:50:06] [Render thread/INFO]: Started 10 worker threads
                [15:50:12] [Render thread/INFO]: [System] [CHAT] <§fKohaku§f [dit]> h_uh^bthaah?.
                [15:50:16] [Render thread/INFO]: [System] [CHAT] <§fKohaku§f [dit fort]> iorghrhbaoiuhubraiomp.
                [15:50:19] [Render thread/INFO]: [System] [CHAT] <§fKohaku§f [dit]> riuyaypbaeezv.
                [15:50:22] [Render thread/INFO]: [System] [CHAT] * §fKohaku§f riupyagbirmbveyu. *
                """;
        minecraftLog = new MinecraftLog(LOG_SAMPLE);
    }

    @Test
    void testExtractDialogue() {
        MinecraftLog result = minecraftLog.extractDialogue();
        assertNotNull(result, "Extracted MinecraftLog should not be null");
        String CLEANED_LOG_SAMPLE = """
                <Kohaku [dit]> h_uh^bthaah?.
                <Kohaku [dit fort]> iorghrhbaoiuhubraiomp.
                <Kohaku [dit]> riuyaypbaeezv.
                * Kohaku riupyagbirmbveyu. *""";
        assertEquals(CLEANED_LOG_SAMPLE, result.log(), "The Minecraft log was not extracted and cleaned correctly.");
    }

    @Test
    void testExtractDialogueWithEmptyLog() {
        MinecraftLog result = new MinecraftLog("").extractDialogue();
        assertNotNull(result, "Extracted MinecraftLog should not be null");
        assertTrue(result.log().isEmpty(), "The Minecraft log should be empty string.");
    }

    @Test
    void testExtractDialogueWithColorCodes() {
        String logWithColorCodes = "[CHAT] <§fKohaku§f [dit]> hello§eworld.\n" +
                "[CHAT] <§fKohaku§f [dit fort]> goo§dmorning.";
        String expectedCleanedDialog = "<Kohaku [dit]> helloworld.\n" +
                "<Kohaku [dit fort]> goomorning.";
        MinecraftLog result = new MinecraftLog(logWithColorCodes).extractDialogue();
        assertEquals(expectedCleanedDialog, result.log(), "The Minecraft log was not extracted and cleaned correctly.");
    }


    @Test
    void testExtractDialogueWithNoCHATPrefix() {
        String logWithoutCHATPrefix = "<§fKohaku§f [dit]> hello\n" +
                "<§fKohaku§f [dit fort]> good morning.";
        MinecraftLog result = new MinecraftLog(logWithoutCHATPrefix).extractDialogue();
        assertTrue(result.log().isEmpty(), "Output should be empty when there is no [CHAT] prefix.");
    }

    @Test
    void testExtractDialogueWithShadersReloaded() {
        String logWithShadersReloaded = "[CHAT] Shaders Reloaded!\n" +
                "[CHAT] <§fKohaku§f [dit fort]> good morning.";
        String expectedDialog = "<Kohaku [dit fort]> good morning.";
        MinecraftLog result = new MinecraftLog(logWithShadersReloaded).extractDialogue();
        assertEquals(expectedDialog, result.log(), "The line with 'Shaders Reloaded!' should be excluded.");
    }

    /**
     * Tests the {@link MinecraftLog#extractDialogue()} method when the log is null.
     * Expects a NullPointerException to be thrown.
     */
    @Test
    void testExtractDialogueWithNullLog() {
        MinecraftLog minecraftLog = new MinecraftLog(null);
        assertThrows(NullPointerException.class, minecraftLog::extractDialogue);
    }
}
