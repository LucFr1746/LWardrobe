package me.LucFr.LWardrobe.FileManager;

import me.LucFr.LWardrobe.Utils.TextWork;
import me.LucFr.LWardrobe.LWardrobe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Lang {

    public static String pluginPrefix;
    public static String permissionDenied;
    public static String disable;
    public static String pageNotExists;
    public static String slotNotExists;
    public static String deniedModifyArmor;
    public static String noSpace;
    public static String openGuiError;
    public static String playerNotFound;
    public static String successOpenGUI;
    public static String unsuccessfulOpenGUI;
    public static String playerRequire;
    public static String numberRequire;
    public static String unknownCommand;
    public static String playerToOpenRequire;
    public static String error;
    public static String errorAtCommandSection;
    public static String successResetPlayer;
    public static String confirm;
    public static String commandExecutorDenied;
    public static String checking;
    public static String goBackCommand;

    final LWardrobe plugin;
    private FileConfiguration langConfig = null;
    private File lang = null;

    public Lang(LWardrobe plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void ReloadConfig() {
        if (this.lang == null)
            this.lang = new File(this.plugin.getDataFolder(), "lang.yml");
        this.langConfig = YamlConfiguration.loadConfiguration(this.lang);

        InputStream defaultStream = this.plugin.getResource("lang.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.langConfig.setDefaults(defaultConfig);
        }
        readConfig();
    }

    public FileConfiguration getConfig() {
        if (this.langConfig == null)
            ReloadConfig();

        return this.langConfig;
    }

    public void saveDefaultConfig() {
        if (this.langConfig == null)
            this.lang = new File(this.plugin.getDataFolder(), "lang.yml");

        if (!this.lang.exists()) {
            this.plugin.saveResource("lang.yml", false);
        }
        readConfig();
    }
    public void readConfig() {
        pluginPrefix = TextWork.Convert(getConfig().getString("Plugin-Prefix"));
        permissionDenied = pluginPrefix + TextWork.Convert(getConfig().getString("Permission-Denied"));
        disable = pluginPrefix + TextWork.Convert(getConfig().getString("Disable"));
        pageNotExists = pluginPrefix + TextWork.Convert(getConfig().getString("Page-Not-Exists"));
        slotNotExists = pluginPrefix + TextWork.Convert(getConfig().getString("Slot-Not-Exists"));
        deniedModifyArmor = pluginPrefix + TextWork.Convert(getConfig().getString("Denied-Modify-Armor"));
        noSpace = pluginPrefix + TextWork.Convert(getConfig().getString("No-Space"));
        openGuiError = pluginPrefix + TextWork.Convert(getConfig().getString("Open-GUI-Error"));
        playerNotFound = pluginPrefix + TextWork.Convert(getConfig().getString("Player-Not-Found"));
        successOpenGUI = pluginPrefix + TextWork.Convert(getConfig().getString("Success-Open-GUI"));
        unsuccessfulOpenGUI = pluginPrefix + TextWork.Convert(getConfig().getString("Unsuccessful-Open-GUI"));
        playerRequire = pluginPrefix + TextWork.Convert(getConfig().getString("Player-Require"));
        numberRequire = pluginPrefix + TextWork.Convert(getConfig().getString("Number-Require"));
        unknownCommand = TextWork.Convert(getConfig().getString("Unknown-Command"));
        playerToOpenRequire = pluginPrefix + TextWork.Convert(getConfig().getString("Player-To-Open-Require"));
        error = pluginPrefix + TextWork.Convert(getConfig().getString("Error"));
        errorAtCommandSection = pluginPrefix + TextWork.Convert(getConfig().getString("Error-At-Command-Section"));
        successResetPlayer = pluginPrefix + TextWork.Convert(getConfig().getString("Success-Reset-Player"));
        confirm = pluginPrefix + TextWork.Convert(getConfig().getString("Confirm"));
        commandExecutorDenied = pluginPrefix + TextWork.Convert(getConfig().getString("Command-Executor-Denied"));
        checking = pluginPrefix + TextWork.Convert(getConfig().getString("Checking"));
        goBackCommand = getConfig().getString("Button.Go-Back.Command");
    }
}
