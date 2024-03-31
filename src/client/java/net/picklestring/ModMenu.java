package net.picklestring;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            /*ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("title.examplemod.config"));
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Download new updates from server"), config.downloadNewUpdatesFromServer)
                    .setDefaultValue(false) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.literal("Whether or not to download new updates from the custom syncing server.")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> config.downloadNewUpdatesFromServer = newValue) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config
            return builder.build();*/
            return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
        };
    }
}