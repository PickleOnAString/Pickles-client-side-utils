package net.picklestring;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GlyphCopy {

    public static void GlyphCopyCheck(MinecraftClient client)
    {
        if (PicklesClientSideUtilsClient.GlyphCopyIsEnabled) {
            while (PicklesClientSideUtilsClient.keyBinding.wasPressed() && hasControlDown()) {
                Identifier itemId = Registries.ITEM.getId(client.player.getMainHandStack().getItem());
                Identifier path = new Identifier(PicklesClientSideUtilsClient.MOD_ID, "glyphs/" + itemId.getNamespace() + "/database.json");
                Resource res = MinecraftClient.getInstance().getResourceManager().getResource(path).get();

                InputStream in;
                try {
                    in = res.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                JsonElement je = PicklesClientSideUtilsClient.gson.fromJson(reader, JsonElement.class);
                JsonObject json = je.getAsJsonObject();

                String str = json.get(itemId.getPath()).getAsString();
                client.keyboard.setClipboard(str);
            }
        }
    }

    public static boolean hasControlDown() {
        if (MinecraftClient.IS_SYSTEM_MAC) {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 343) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 347);
        } else {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 341) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 345);
        }
    }
}
