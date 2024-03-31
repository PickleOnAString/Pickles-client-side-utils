package net.picklestring;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;

public class ServerStartedInject implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        PicklesClientSideUtilsClient.LOGGER.info("HEHEHEHEHEH");
        if (HttpServer.Ping()) {
            try {
                HttpServer.downloadResourcePack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
