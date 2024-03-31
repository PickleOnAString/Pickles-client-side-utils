package net.picklestring;

import com.google.gson.Gson;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;

public class PicklesClientSideUtilsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("picklesclientsideutils");
	public static KeyBinding keyBinding;
	public static String MOD_ID = "picklesclientsideutils";
	public static Gson gson;
	public static boolean GlyphCopyIsEnabled;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		HttpServer.url = "http://"+config.syncingServerAddress+(config.syncingServerPort.isEmpty() ? "" : ":")+config.syncingServerPort+"/";
        try {
			if (HttpServer.Ping()) {
				if (config.syncResourcePackFromServer) {
					LOGGER.info("Downloading resource pack");
					HttpServer.downloadResourcePack();
				}
			}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gson = new Gson();

		GlyphCopyIsEnabled = config.glyphCopy;
		if (config.glyphCopy) {
			keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
					"key.picklesclientsideutils.copy_glyph", // The translation key of the keybinding's name
					InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
					GLFW.GLFW_KEY_C, // The keycode of the key
					"category.picklesclientsideutils.keybindings" // The translation key of the keybinding's category.
			));
		}

		ClientTickEvents.END_CLIENT_TICK.register(GlyphCopy::GlyphCopyCheck);
		ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedInject());

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("upload_texture")
						.then(argument("url", StringArgumentType.string())
							.then(argument("fileName", StringArgumentType.string())
								.executes(Commands::createTexture)))));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("upload_item_override")
						.then(argument("item", StringArgumentType.string())
								.then(argument("displayName", StringArgumentType.string())
										.then(argument("texture", StringArgumentType.string())
												.then(argument("fileName", StringArgumentType.string())
													.executes(Commands::createProp)))))));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("upload_elytra_override")
						.then(argument("displayName", StringArgumentType.string())
								.then(argument("texture", StringArgumentType.string())
										.then(argument("fileName", StringArgumentType.string())
												.executes(Commands::createElytraProp))))));
	}
}