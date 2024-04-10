package io.github.lucfr1746.LWardrobe.Metrics;

import io.github.lucfr1746.LWardrobe.LWardrobe;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final LWardrobe plugin;

    public UpdateChecker(LWardrobe plugin) {
        this.plugin = plugin;
        check(plugin);
    }

    public void getLatestVersion(Consumer<String> consumer) {
        int resourceId = 94186;
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
                .openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            plugin.getLogger().info("Update checker is broken, can't find an update!" + exception.getMessage());
        }
    }

    public void check(LWardrobe plugin) {
        getLatestVersion(version -> {
            PluginDescriptionFile pdf = plugin.getDescription();
            if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage("[LWardrobe] Version: " + ChatColor.GREEN + pdf.getVersion());
                Bukkit.getConsoleSender().sendMessage("[LWardrobe] Plugin is up to date!");
            } else {
                Bukkit.getConsoleSender().sendMessage("[LWardrobe] Version: " + ChatColor.RED + pdf.getVersion());
                Bukkit.getConsoleSender().sendMessage("[LWardrobe] Plugin has an update! download at: https://www.spigotmc.org/resources/94186/");
            }
        });
    }
}
