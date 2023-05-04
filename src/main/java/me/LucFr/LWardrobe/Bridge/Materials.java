package me.LucFr.LWardrobe.Bridge;

import java.util.HashMap;

public class Materials {

    public static HashMap<Integer, String> Material_1_13_newer = new HashMap<>();
    public static HashMap<Integer, Short> Material_1_12_older = new HashMap<>();

    public static HashMap<String, String> Material_1_14_newer_Button = new HashMap<>();
    public static HashMap<String, String> Material_1_13_newer_Button = new HashMap<>();
    public static HashMap<String, Short> Material_1_12_older_Button = new HashMap<>();

    public Materials() {
        Material_1_13_newer.put(1, "RED_STAINED_GLASS_PANE");
        Material_1_13_newer.put(2, "ORANGE_STAINED_GLASS_PANE");
        Material_1_13_newer.put(3, "YELLOW_STAINED_GLASS_PANE");
        Material_1_13_newer.put(4, "LIME_STAINED_GLASS_PANE");
        Material_1_13_newer.put(5, "GREEN_STAINED_GLASS_PANE");
        Material_1_13_newer.put(6, "LIGHT_BLUE_STAINED_GLASS_PANE");
        Material_1_13_newer.put(7, "BLUE_STAINED_GLASS_PANE");
        Material_1_13_newer.put(8, "MAGENTA_STAINED_GLASS_PANE");
        Material_1_13_newer.put(9, "PURPLE_STAINED_GLASS_PANE");

        Material_1_12_older.put(1, (short) 14);
        Material_1_12_older.put(2, (short) 1);
        Material_1_12_older.put(3, (short) 4);
        Material_1_12_older.put(4, (short) 5);
        Material_1_12_older.put(5, (short) 13);
        Material_1_12_older.put(6, (short) 3);
        Material_1_12_older.put(7, (short) 11);
        Material_1_12_older.put(8, (short) 2);
        Material_1_12_older.put(9, (short) 10);

        Material_1_13_newer_Button.put("Locked", "ROSE_RED");

        Material_1_14_newer_Button.put("Empty", "GRAY_DYE");
        Material_1_14_newer_Button.put("Ready", "PINK_DYE");
        Material_1_14_newer_Button.put("Equipped", "LIME_DYE");
        Material_1_14_newer_Button.put("Locked", "RED_DYE");
        Material_1_14_newer_Button.put("Delete", "RED_DYE");

        Material_1_12_older_Button.put("Empty", (short) 8);
        Material_1_12_older_Button.put("Ready", (short) 9);
        Material_1_12_older_Button.put("Equipped", (short) 10);
        Material_1_12_older_Button.put("Locked", (short) 1);
        Material_1_12_older_Button.put("Delete", (short) 1);
    }
}