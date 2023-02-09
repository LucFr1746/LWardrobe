package me.LucFr.LWardrobe.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextWork {

    public static String Convert(String input) {
        if (ServerVersion.isNewerThan("1.16")) {
            input = gradient(input);
            input = convertHex(input);
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }
    public static String Convert(Player p, String input) {
        input = setPlaceholder(p, input);
        if (ServerVersion.isNewerThan("1.16")) {
            input = gradient(input);
            input = convertHex(input);
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

    public static String Convert(OfflinePlayer p, String input) {
        input = setPlaceholder(p, input);
        if (ServerVersion.isNewerThan("1.16")) {
            input = gradient(input);
            input = convertHex(input);
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

    public static String gradient(String input) {
        String[] path1 = input.split("<gradient>");
        List<String> path2 = new ArrayList<>();
        for (int i = 0; i < path1.length; i++) {
            if (i % 2 != 0)
                path2.add(path1[i]);
        }

        List<String> converted = new ArrayList<>();
        for (String path : path2) {
            StringBuilder result = new StringBuilder();
            String[] path3 = path.split("-");
            double R1 = Color.decode(path3[0]).getRed();
            double G1 = Color.decode(path3[0]).getGreen();
            double B1 = Color.decode(path3[0]).getBlue();
            double R2 = Color.decode(path3[1]).getRed();
            double G2 = Color.decode(path3[1]).getGreen();
            double B2 = Color.decode(path3[1]).getBlue();

            String text = path3[2];
            int length = text.length()-1;
            double R = (R2-R1)/length;
            double G = (G2-G1)/length;
            double B = (B2-B1)/length;

            result.append(ChatColor.of(path3[0])).append(text.charAt(0));
            for (int i = 1; i < text.length()-1; i++) {
                result.append(ChatColor.of(new Color((int) Math.ceil(R1 + R), (int) Math.ceil(G1 + G), (int) Math.ceil(B1 + B)))).append(text.charAt(i));
                R1 = (int) Math.round(R1+R);
                G1 = (int) Math.round(G1+G);
                B1 = (int) Math.round(B1+B);
            }
            result.append(ChatColor.of(path3[1])).append(text.charAt(text.length() - 1));
            converted.add(result.toString());
        }
        for (int i = 0; i < path2.size(); i++) {
            String convert = "<gradient>" + path2.get(i) + "<gradient>";
            input = input.replace(convert, converted.get(i));
        }
        return input;
    }

    public static String convertHex(String input) {
        StringBuilder result = new StringBuilder();
        String[] string = input.split("#");
        if (string.length == 0)
            return input;
        for (String path : string) {
            if (path.charAt(0) != '&' && !path.equals(string[0])) {
                path = "#" + path;
                String hexColor = path.substring(0, 7);
                path = path.replace(path.substring(0, 7), "");
                path = ChatColor.of(hexColor) + path;
            }
            result.append(path);
        }
        return result.toString();
    }

    public static String setPlaceholder(Player p, String input) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            input = PlaceholderAPI.setPlaceholders(p, input);
        return input;
    }
    public static String setPlaceholder(OfflinePlayer p, String input) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            input = PlaceholderAPI.setPlaceholders(p, input);
        return input;
    }
}