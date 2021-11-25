package me.darkolythe.customrecipeapi;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CommandHandler implements CommandExecutor {

    private CustomRecipeAPI main = CustomRecipeAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {

        if (giveCommand(sender, args)) {
            return true;
        }

        if (sender instanceof Player) {

            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("crapi")) {
                if (player.hasPermission("crapi.command")) {
                    if (args.length == 0) {
                        if (player.hasPermission("crapi.new")) {
                            player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [book, new, setworkbench, workbench, give, items]");
                        } else {
                            player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [book, workbench]");
                        }
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("book")) {
                            if (player.hasPermission("crapi.book")) {
                                player.openInventory(BookManager.getRecipeBook(player, 0));
                            } else {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                            }
                        } else if (args[0].equalsIgnoreCase("new")) {
                            if (player.hasPermission("crapi.new")) {
                                player.openInventory(RecipeCreator.openCreator(player));
                            } else {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                            }
                        } else if (args[0].equalsIgnoreCase("setworkbench")) {
                            if (player.hasPermission("crapi.setworkbench")) {
                                player.openInventory(WorkbenchManager.createWorkbenchCreator());
                            } else {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                            }
                        } else if (args[0].equalsIgnoreCase("workbench")) {
                            if (player.hasPermission("crapi.book")) {
                                player.openInventory(WorkbenchManager.createWorkbenchViewer());
                            } else {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                            }
                        } else {
                            if (player.hasPermission("crapi.new")) {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [book, new, setworkbench, workbench, give, items]");
                            } else {
                                player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [book, workbench]");
                            }
                        }
                    }
                } else {
                    player.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                }
            }
        }
        return true;
    }


    private boolean giveCommand(CommandSender sender, String[] args) {
        if (args.length == 0 || (!args[0].equalsIgnoreCase("give") && !args[0].equalsIgnoreCase("items"))) {
            return false;
        }
        if (sender instanceof Player || sender instanceof BlockCommandSender) {
            // Example give commands
            // give storagecell16k 6
            // give joe storagecontainer1M 2
            // give wrench
            if (args.length == 1 && args[0].equalsIgnoreCase("items") && sender.hasPermission("crapi.give")) {
                String items = "";
                for (CustomRecipe s : APIManager.getRecipes()) {
                    items += ChatColor.GREEN + s.getPermission() + ChatColor.BLUE + ", ";
                }
                sender.sendMessage(CustomRecipeAPI.prefix + items);
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("give") && (sender instanceof BlockCommandSender || sender.hasPermission("crapi.give"))) {
                Optional<Player> player = Bukkit.getServer().getOnlinePlayers().stream().map(x -> (Player) x).filter(x -> x.getDisplayName().equalsIgnoreCase(args[1])).findAny();
                String itemName = null;
                int quantity = 1;
                if (player.isPresent()) { // A recipient player was specified
                    if (args.length >= 4) {
                        itemName = args[2];
                        quantity = StringUtils.isNumeric(args[3]) ? Integer.parseInt(args[3]) : 1;
                    }
                    else if (args.length >= 3) {
                        itemName = args[2];
                    }
                } else {
                    if (sender instanceof BlockCommandSender) {
                        return false;
                    }

                    if (args.length >= 3) {
                        itemName = args[1];
                        quantity = StringUtils.isNumeric(args[2]) ? Integer.parseInt(args[2]) : 1;
                    }
                    else {
                        itemName = args[1];
                    }
                }

                ItemStack item = getItemWithName(itemName);
                if (item != null) {
                    for (int i = 0; i < quantity; i++) {
                        player.orElseGet(() -> (Player) sender).getInventory().addItem(item);
                    }
                    return true;
                } else {
                    sender.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi give <user> <item> <amt>");
                    return false;
                }
            } else {
                if (sender.hasPermission("crapi.give")) {
                    sender.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "Invalid Arguments: /crapi [(give <user> <item> <amt>), items]");
                } else {
                    sender.sendMessage(CustomRecipeAPI.prefix + ChatColor.RED + "You do not have permission to do that!");
                }
            }
        }
        return true;
    }

    private ItemStack getItemWithName(String itemName) {
        for (CustomRecipe recipe : APIManager.getRecipes()) {
            if (itemName.equals(recipe.getPermission())) {
                return recipe.getResult();
            }
        }
        return null;
    }
}
