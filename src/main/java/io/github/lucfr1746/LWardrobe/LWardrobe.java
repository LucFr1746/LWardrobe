package io.github.lucfr1746.LWardrobe;

import io.github.lucfr1746.LWardrobe.Commands.CommandsRegister;
import io.github.lucfr1746.LWardrobe.DataManager.DataManager;
import io.github.lucfr1746.LWardrobe.Metrics.UpdateChecker;
import io.github.lucfr1746.LWardrobe.Metrics.bStatsMetrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWardrobe extends JavaPlugin {

    private static LWardrobe plugin;

    @Override
    public void onEnable() {
        plugin = this;
    // Metrics
        new UpdateChecker(this);
        new bStatsMetrics(this, 24103);
    // Data Manager
        new DataManager(this);
    // Commands
        new CommandsRegister(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getInstance() {
        return plugin;
    }
}
