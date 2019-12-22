package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RecipeListener implements Listener {

    private CustomRecipeAPI main;
    RecipeListener(CustomRecipeAPI plugin) {
        main = plugin;
    }

    @EventHandler
    private void workbenchPlaced(BlockPlaceEvent event) {
        if (event.getItemInHand().equals(main.getWorkbench().getResult())) {
            Block block = event.getBlockPlaced();
            main.addWorkbench(block);
        }
    }

    @EventHandler
    private void workbenchDestroyed(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(main.getWorkbench().getResult().getType())) {
            main.removeWorkbench(event.getBlock());
        }
    }

    @EventHandler
    private void workbenchInteracted(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (main.isWorkbench(event.getClickedBlock())) {
                    event.setCancelled(true);
                    event.getPlayer().openInventory(Bukkit.getServer().createInventory(null, InventoryType.DISPENSER, ChatColor.BOLD.toString() + "Special Crafting"));
                }
            }
        }
    }

    @EventHandler
    private void recipeCheck(InventoryClickEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main.plugin, new Runnable() {
            @Override
            public void run() {
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    Inventory inv = player.getInventory();
                    if (player.getOpenInventory().getTitle().equals(ChatColor.BOLD.toString() + "Special Crafting")) {
                        if (event.getInventory().getType() == InventoryType.DISPENSER) {
                            for (CustomRecipe recipe : main.getRecipes()) {
                                if (recipe.checkRecipe(event.getInventory())) {
                                    if (getFirst(inv, recipe.getResult()) != -1) {
                                        int index = getFirst(inv, recipe.getResult());
                                        if (inv.getItem(index) != null && inv.getItem(index).getType() != Material.AIR) {
                                            ItemStack item = inv.getItem(index);
                                            item.setAmount(item.getAmount() + recipe.getResult().getAmount());
                                        } else {
                                            inv.setItem(index, recipe.getResult());
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED.toString() + "Inventory is full. Cannot craft item.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 1, 40);
    }

    private int getFirst(Inventory inv, ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
                if (cloneOne(inv.getItem(i)).equals(cloneOne(item))) {
                    return i;
                }
            } else {
                return i;
            }
        }
        return -1;
    }

    private ItemStack cloneOne(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        return clone;
    }
}
