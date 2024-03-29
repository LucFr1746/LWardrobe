package me.LucFr.LWardrobe;

import me.LucFr.LWardrobe.Commands.CommandsRegister;
import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.FileManager.Lang;
import me.LucFr.LWardrobe.GUI.LWardrobeMenu;
import me.LucFr.LWardrobe.Listener.GUIOpenAndClose;
import me.LucFr.LWardrobe.Listener.GUIFunction;
import me.LucFr.LWardrobe.Metrics.UpdateChecker;
import me.LucFr.LWardrobe.Metrics.bStatsMetrics;
import me.LucFr.LWardrobe.Bridge.Materials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWardrobe extends JavaPlugin {

    public static Lang lang;
    public static Config config;

    public static Plugin getInstance;

    @Override
    public void onEnable() {
        getInstance = this;
        // Metrics
            new UpdateChecker(this);
            new bStatsMetrics(this, 17186);
        // Files
            lang = new Lang(this);
            config = new Config(this);
        // NMS Material
            new Materials();
        // Listener
            new GUIFunction(this);
            new GUIOpenAndClose(this);
        // Commands
            new CommandsRegister(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof LWardrobeMenu) {
                GUIOpenAndClose.saveData(player, player.getOpenInventory().getTopInventory());
                player.closeInventory();
            }
        });
        config.shutdown();
    }
}
