package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static me.darkolythe.customrecipeapi.BookManager.createBackButton;

public class BookListener implements Listener {

    @EventHandler
    private void onBookClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Recipe Book")) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getItemMeta() != null) {
                    if (item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Next Page") && item.getType() == Material.ARROW) {
                        int page = Integer.parseInt(item.getItemMeta().getLore().get(0).replaceAll("[^\\d]", ""));
                        BookManager.getRecipeBook(player, page - 1);
                    } else if (item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Previous Page") && item.getType() == Material.ARROW) {
                        int page = Integer.parseInt(item.getItemMeta().getLore().get(0).replaceAll("[^\\d]", ""));
                        BookManager.getRecipeBook(player, page + 1);
                    } else {
                        if (!event.getClickedInventory().equals(player.getInventory())) {
                            if (event.getClick() == ClickType.RIGHT && player.hasPermission("crapi.giveitem")) {
                                player.getInventory().addItem(BookManager.getRecipeFromItem(item, player).getResult());
                            } else {
                                BookManager.openRecipe(player, item);
                            }
                        }
                    }
                }
            } else if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Custom Recipe View")) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                if (item != null && item.equals(createBackButton())) {
                    player.openInventory(BookManager.getRecipeBook(player, 0));
                }
            }
        }
    }
}
