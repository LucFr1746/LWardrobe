package io.github.lucfr1746.lwardrobe.Utils.Placeholders;

import io.github.lucfr1746.lwardrobe.LWardrobe;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderRegister extends PlaceholderExpansion {

    private final LWardrobe plugin;

    public PlaceholderRegister(LWardrobe plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "lwardrobe";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return null;
        if (params.equalsIgnoreCase("total_slots")) {
            return "text1";
        }

        if (params.equalsIgnoreCase("placeholder2")) {
            return "text2";
        }
        return null;
    }
}
