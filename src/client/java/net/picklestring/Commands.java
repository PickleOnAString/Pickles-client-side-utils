package net.picklestring;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.io.IOException;

public class Commands {
    public static int createTexture(CommandContext<FabricClientCommandSource> context) {
        if (HttpServer.Ping()) {
            String url = StringArgumentType.getString(context, "url");
            String fileName = StringArgumentType.getString(context, "fileName");
            try {
                HttpServer.createTexture(url, fileName);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            context.getSource().sendFeedback(Text.literal("Uploaded!"));
        }
        else {
            context.getSource().sendFeedback(Text.literal("Server offline!"));
        }
        return 1;
    }

    public static int createProp(CommandContext<FabricClientCommandSource> context) {
        if (HttpServer.Ping()) {
            String item = StringArgumentType.getString(context, "item");
            String displayName = StringArgumentType.getString(context, "displayName");
            String texture = StringArgumentType.getString(context, "texture");
            String fileName = StringArgumentType.getString(context, "fileName");

            try {
                HttpServer.createPropFile(item, displayName, texture, fileName);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            context.getSource().sendFeedback(Text.literal("Uploaded!"));
        }
        else {
            context.getSource().sendFeedback(Text.literal("Server offline!"));
        }

        return 1;
    }

    public static int createElytraProp(CommandContext<FabricClientCommandSource> context) {
        if (HttpServer.Ping()) {
            String displayName = StringArgumentType.getString(context, "displayName");
            String texture = StringArgumentType.getString(context, "texture");
            String fileName = StringArgumentType.getString(context, "fileName");

            try {
                HttpServer.createElytraPropFile(displayName, texture, fileName);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            context.getSource().sendFeedback(Text.literal("Uploaded!"));
        }
        else {
            context.getSource().sendFeedback(Text.literal("Server offline!"));
        }

        return 1;
    }
}
