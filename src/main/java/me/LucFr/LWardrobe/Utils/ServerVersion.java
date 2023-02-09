package me.LucFr.LWardrobe.Utils;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ServerVersion {
    public static boolean isNewerThan(String ver) {

        String serverVersion = Bukkit.getBukkitVersion();
        StringBuilder Ver = new StringBuilder();
        List<Integer> currentVersion = new ArrayList<>();
        for (int i = 0; i < serverVersion.length(); i++) {
            if (serverVersion.charAt(i) == '.') {
                currentVersion.add(Integer.valueOf(Ver.toString().replace(".", "")));
                Ver = new StringBuilder();
            }
            if (serverVersion.charAt(i) == '-') {
                currentVersion.add(Integer.valueOf(Ver.toString().replace(".", "")));
                break;
            }
            Ver.append(serverVersion.charAt(i));
        }
        Ver = new StringBuilder();
        List<Integer> compareVersion = new ArrayList<>();
        for (int i = 0; i < ver.length(); i++) {
            if (ver.charAt(i) == '.') {
                compareVersion.add(Integer.valueOf(Ver.toString().replace(".", "")));
                Ver = new StringBuilder();
            }
            Ver.append(ver.charAt(i));
        }
        compareVersion.add(Integer.valueOf(Ver.toString().replace(".", "")));
        if (compareVersion.size() == 1)
            compareVersion.add(0);
        if (compareVersion.size() == 2)
            compareVersion.add(0);
        for (int i = 0; i < 3; i++) {
            if (currentVersion.get(i) < compareVersion.get(i))
                return false;
        }
        return true;
    }

    public static boolean isVersion(String ver) {
        String serverVersion = Bukkit.getBukkitVersion();
        return serverVersion.contains(ver);
    }
}
