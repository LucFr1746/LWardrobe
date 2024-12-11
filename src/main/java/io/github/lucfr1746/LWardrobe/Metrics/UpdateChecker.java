package io.github.lucfr1746.LWardrobe.Metrics;

import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final LWardrobe plugin;

    public UpdateChecker(LWardrobe plugin) {
        this.plugin = plugin;
        check(-1);
    }

    public void getLatestVersion(Consumer<String> consumer, int resourceId) {
        try (InputStream inputStream = new URI("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).toURL().openStream();
             Scanner scanner = new Scanner(inputStream)) {

            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException | URISyntaxException exception) {
            plugin.getLogger().info("Update checker is broken, can't find an update! " + exception.getMessage());
        }
    }

    public void check(int resourceId) {
//        getLatestVersion(version -> {
//            PluginDescriptionFile pdf = plugin.getDescription();
//            // Check if the current version matches the latest version.
//            if (pdf.getVersion().equalsIgnoreCase(version)) {
//                this.plugin.getLogger().info("Version: " + ChatColor.GREEN + pdf.getVersion());
//                this.plugin.getLogger().info("Plugin is up to date!");
//            } else {
//                this.plugin.getLogger().info("Version: " + ChatColor.RED + pdf.getVersion());
//                this.plugin.getLogger().info("Plugin has an update! Download at: https://www.spigotmc.org/resources/" + resourceId);
//            }
//        }, resourceId);
    }
}
