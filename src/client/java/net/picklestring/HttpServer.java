package net.picklestring;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;

public class HttpServer {
    public static String url;
    public static boolean Ping() {
        try {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("api_key", config.apiKey)
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return response.body().equals("Pong");
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
    public static void downloadResourcePack() throws IOException {
        if (FabricLoader.getInstance().getGameDir().resolve("resourcepacks/AutoSyncedPack.zip").toFile().exists()) {
            //Files.createFile(FabricLoader.getInstance().getGameDir().resolve("resourcepacks/AutoSyncedPack.zip"));
            Files.delete(FabricLoader.getInstance().getGameDir().resolve("resourcepacks/AutoSyncedPack.zip"));
        }

        try {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url+"resource_pack"))
                    .header("api_key", config.apiKey)
                    .GET()
                    .build();
            HttpResponse<InputStream> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream());

            Files.copy(
                    response.body(),
                    FabricLoader.getInstance().getGameDir().resolve("resourcepacks/AutoSyncedPack.zip"));

            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().reloadResources();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTexture(String textureUrl, String fileName) throws IOException, InterruptedException {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        String requestBody = "url="+textureUrl+"&fileName="+fileName;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"create/texture"))
                .header("api_key", config.apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void createPropFile(String item, String displayName, String texture, String fileName) throws IOException, InterruptedException {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        String requestBody = "item="+item+"&displayName="+displayName+"&texture="+texture+"&fileName="+fileName;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"create/prop"))
                .header("api_key", config.apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void createElytraPropFile(String displayName, String texture, String fileName) throws IOException, InterruptedException {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        String requestBody = "displayName="+displayName+"&texture="+texture+"&fileName="+fileName;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"create/elytra_prop"))
                .header("api_key", config.apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
