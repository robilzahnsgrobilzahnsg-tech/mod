package de.leavemod;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class LeaveMod implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("leavemod", "main"));

    private static KeyMapping leaveKey;

    @Override
    public void onInitializeClient() {
        // Die ß-Taste der deutschen Tastatur liefert in GLFW den Code GLFW_KEY_MINUS
        // (Tokens gehen vom US-Layout aus). Änderbar unter Steuerung -> Tastenbelegung.
        leaveKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.leavemod.leave", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (leaveKey.consumeClick()) {
                leave(mc);
            }
        });
    }

    private static void leave(Minecraft mc) {
        ClientPacketListener connection = mc.getConnection();
        if (connection == null) return; // nicht in einer Welt / auf einem Server
        connection.getConnection().disconnect(Component.literal("Server verlassen (ß)"));
    }
}
