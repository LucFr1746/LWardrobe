package io.github.lucfr1746.lwardrobe;

import io.github.lucfr1746.lwardrobe.Metrics.UpdateChecker;
import io.github.lucfr1746.lwardrobe.Metrics.bStatsMetrics;
import io.github.lucfr1746.lwardrobe.Utils.Placeholders.PlaceholderRegister;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWardrobe extends JavaPlugin {

    public static Plugin getInstance;

    @Override
    public void onEnable() {
        getInstance = this;
    // Metrics
        new UpdateChecker(this);
        new bStatsMetrics(this, 21558);
    // Placeholder Registers
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderRegister(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
