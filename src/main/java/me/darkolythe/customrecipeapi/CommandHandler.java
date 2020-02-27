package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private CustomRecipeAPI main = CustomRecipeAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "The console cannot open the CustomRecipeAPI Recipe Book!");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("crapi")) { //if the player has permission, and the command is right
            if (player.hasPermission("crapi.command")) {
                if (args.length == 0) {
                    player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [book, new]");
                } else {
                    if (args[0].equalsIgnoreCase("book")) { //if there's more than one argument, and it's book
                        if (player.hasPermission("crapi.book")) { //if the command executor has permission
                            player.openInventory(BookManager.getRecipeBook(player, 0));
                        }
                    } else if (args[0].equalsIgnoreCase("new")) { //if there's more than one argument, and it's new
                        if (player.hasPermission("crapi.new")) { //if the command executor has permission
                            player.openInventory(RecipeCreator.openCreator(player));
                        }
                    } else if (args[0].equalsIgnoreCase("setworkbench")) {
                        if (player.hasPermission("crapi.setworkbench")) {
                            player.openInventory(WorkbenchManager.createWorkbenchCreator());
                        }
                    } else if (args[0].equalsIgnoreCase("workbench")) {
                        if (player.hasPermission("crapi.viewworkbench")) {
                            player.openInventory(WorkbenchManager.createWorkbenchViewer());
                        }
                    }
                }
            }
        }
        return true;
    }
}
