package io.github.lucfr1746.LWardrobe;

import io.github.lucfr1746.LWardrobe.Metrics.UpdateChecker;
import io.github.lucfr1746.LWardrobe.Metrics.bStatsMetrics;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
