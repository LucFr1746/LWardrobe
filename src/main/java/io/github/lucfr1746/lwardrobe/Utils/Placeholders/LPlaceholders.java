package io.github.lucfr1746.lwardrobe.Utils.Placeholders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class LPlaceholders {

    private static String getIdentifier() {
        return "lwardrobe";
    }
    private static String onRequest(Player player, @NotNull String params) {
        if (player == null) return null;
        if (params.equalsIgnoreCase("placeholder1")) {
            return "text1";
        }

        if (params.equalsIgnoreCase("placeholder2")) {
            return "text2";
        }
        return null;
    }
    
    public static String setPlaceholder(Player player, String input) {
        for (String section : input.split("%")) {
            if (!section.isEmpty()) {
                List<String> finalSection = Arrays.asList(section.split("_"));
                if (finalSection.size() > 1 && finalSection.getFirst().equalsIgnoreCase(getIdentifier())) {
                    StringBuilder identifierBuilder = new StringBuilder();
                    identifierBuilder.append(finalSection.get(1));
                    for (int i = 2; i < finalSection.size(); i++) identifierBuilder.append("_").append(finalSection.get(i));
                    String identifier = identifierBuilder.toString();
                    if (onRequest(player, identifier) != null) {
                        input = replace(input, identifier, onRequest(player, identifier));
                    }
                }
            }
        }
        return input;
    }

    private static String replace(String input, String identifier, String holder) {
        return input.replace("%" + getIdentifier() + "_" + identifier + "%", holder);
    }
}
