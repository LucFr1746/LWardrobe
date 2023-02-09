package me.LucFr.LWardrobe.FileManager;

import me.LucFr.LWardrobe.DataManager.Database.MySQL.MySQLConnect;
import me.LucFr.LWardrobe.LWardrobe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Config {

// Data base
    public static Boolean dbEnabled;
    public static String dbType;
    public static String dbHost;
    public static String dbPort;
    public static String dbDatabase;
    public static String dbUsername;
    public static String dbPassword;
// Config
    public static String adminPermission;
    public static String title;
    public static int totalPage;
    public static int totalSlot;
    public static boolean enableRequirePrefix;
    public static final HashMap<Integer, String> permissions = new HashMap<>();
    public static final HashMap<Integer, String> requirePrefix = new HashMap<>();

    final LWardrobe plugin;
    private FileConfiguration configConfig = null;
    private File configFile = null;

    public Config(LWardrobe plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void ReloadConfig() {
        if (this.configFile == null)
            saveDefaultConfig();
        this.configConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("config.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configConfig.setDefaults(defaultConfig);
        }
        readConfig();
    }

    public FileConfiguration getConfig() {
        if (this.configConfig == null)
            ReloadConfig();
        return this.configConfig;
    }

    public void saveDefaultConfig() {
        if (this.configConfig == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
        ReloadConfig();
    }

    public void readConfig() {
        // read config
        dbEnabled = getConfig().getBoolean("Data-Base.Enabled");
        dbType = getConfig().getString("Data-Base.Type");
        dbHost = getConfig().getString("Data-Base.Host");
        dbPort = getConfig().getString("Data-Base.Port");
        dbDatabase = getConfig().getString("Data-Base.Database");
        dbUsername = getConfig().getString("Data-Base.Username");
        dbPassword = getConfig().getString("Data-Base.Password");
        if (dbEnabled && dbType.equals("MySQL")) {
            new MySQLConnect();
        }

        adminPermission = getConfig().getString("Admin-Permission");
        totalPage = (int) Math.ceil(getConfig().getDouble("Total-Slots")/9);
        totalSlot = getConfig().getInt("Total-Slots");
        title = getConfig().getString("Wardrobe-Title");
        enableRequirePrefix = getConfig().getBoolean("Enable-Require-Prefix");
        // add permissions and prefix
        permissions.clear();
        requirePrefix.clear();
        for (String path : getConfig().getConfigurationSection("Slot-Permission").getKeys(false)) {
            String[] Path = path.split("-");
            if (Path.length == 1) {
                permissions.put(Integer.valueOf(Path[0]), getConfig().getString("Slot-Permission." + path + ".Permission"));
                requirePrefix.put(Integer.valueOf(Path[0]), getConfig().getString("Slot-Permission." + path + ".Require-Prefix"));
            }
            if (Path.length == 2) {
                for (int i = Integer.parseInt(Path[0]); i <= Integer.parseInt(Path[1]); i++) {
                    permissions.put(i, getConfig().getString("Slot-Permission." + path + ".Permission"));
                    requirePrefix.put(i, getConfig().getString("Slot-Permission." + path + ".Require-Prefix"));
                }
            }
        }
        for (int i = 1; i <= totalSlot; i++) {
            if (!permissions.containsKey(i)) {
                permissions.put(i, "wardrobe.slot-disabled");
                requirePrefix.put(i, "wardrobe.slot-disabled");
            }
        }
    }

    public void shutdown() {
        if (dbEnabled && dbType.equals("MySQL")) {
            new MySQLConnect().shutdown();
        }
    }
}