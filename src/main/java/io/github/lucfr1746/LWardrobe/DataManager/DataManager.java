package io.github.lucfr1746.LWardrobe.DataManager;

import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Config;
import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Languages;
import io.github.lucfr1746.LWardrobe.LWardrobe;

public class DataManager {

    public DataManager(LWardrobe plugin) {
        plugin.getLogger().info("Registering and initializing files...");
            new Config(plugin);
            new Languages(plugin);
        plugin.getLogger().info("Completed registering and initializing files!");
    }
}
