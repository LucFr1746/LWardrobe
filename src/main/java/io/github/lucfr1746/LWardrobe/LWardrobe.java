package io.github.lucfr1746.LWardrobe;

import io.github.lucfr1746.LWardrobe.Commands.CommandsRegister;
import io.github.lucfr1746.LWardrobe.DataManager.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWardrobe extends JavaPlugin {

    @Override
    public void onEnable() {
    // Data Manager
        new DataManager(this);
    // Commands
        new CommandsRegister(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
