package net.picklestring;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "picklesclientsideutils")
class ModConfig implements ConfigData {
    String syncingServerAddress = "127.0.0.1";
    String syncingServerPort = "3333";
    String apiKey = "";
    boolean syncResourcePackFromServer = false;
    boolean glyphCopy = false;
}
