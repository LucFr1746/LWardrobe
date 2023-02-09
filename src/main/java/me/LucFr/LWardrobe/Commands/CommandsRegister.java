package me.LucFr.LWardrobe.Commands;

import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.FileManager.Lang;
import me.LucFr.LWardrobe.GUI.WardrobeMenu;
import me.LucFr.LWardrobe.Utils.TextWork;
import me.LucFr.LWardrobe.LWardrobe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandsRegister implements CommandExecutor {

    public LWardrobe plugin;
    public static WardrobeMenu menu;

    public static ArrayList<String> args0 = new ArrayList<>();

    public CommandsRegister(LWardrobe plugin) {
        this.plugin = plugin;
        plugin.getCommand("wardrobe").setExecutor(this);
        addCommands();
        menu = new WardrobeMenu();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                menu.openPlayerWardrobe((Player) sender, 1);
                return true;
            } else {
                sender.sendMessage(Lang.commandExecutorDenied);
                return false;
            }
        }
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "open":
                    if (sender instanceof Player) {
                        menu.openPlayerWardrobe((Player) sender, 1);
                        return true;
                    } else {
                        sender.sendMessage(Lang.playerRequire);
                        return false;
                    }
                case "reset":
                case "check":
                    return false;
            }
            if (sender instanceof Player) {
                if (!CommandsRegister.args0.contains(args[0].toLowerCase()) && isNumber(args[0], sender)) {
                    if (Integer.parseInt(args[0]) > 0 && Integer.parseInt(args[0]) <= Config.totalPage) {
                        menu.openPlayerWardrobe((Player) sender, Integer.parseInt(args[0]));
                        return true;
                    } else sender.sendMessage(Lang.pageNotExists);
                } return false;
            } else sender.sendMessage(Lang.errorAtCommandSection.replace("%error%", args[0]));
        } return false;
    }

    public void addCommands() {
        args0.add("open");
        args0.add("reset");
        args0.add("check");
    }

    public static boolean isNumber(String input, CommandSender sender) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(TextWork.Convert("&cInvalid number!"));
        }
        return false;
    }
}
