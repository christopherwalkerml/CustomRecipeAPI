package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftItemTask extends BukkitRunnable {

    private Player player;
    private Inventory eventInventory;
    private CustomRecipe lastRecipe = null;
    private int delay = 20;
    private int counter = 0;

    public CraftItemTask(Player newPlayer, Inventory newEventInventory) {
        player = newPlayer;
        eventInventory = newEventInventory;
    }

    @Override
    public void run() {
        if (counter < delay) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CustomRecipeAPI.getInstance(), new Runnable() {
                @Override
                public void run() {
                    counter++;
                }
            }, 1L);
        } else {
            counter = 0;
            Inventory inv = player.getInventory();
            if (player.hasPermission("crapi.craft")) {
                if (((!APIManager.getWorkbench().getResult().hasItemMeta() || !APIManager.getWorkbench().getResult().getItemMeta().hasDisplayName()) && player.getOpenInventory().getTitle().equals(APIManager.getWorkbench().getResult().getType().toString()))
                        || player.getOpenInventory().getTitle().equals(APIManager.getWorkbench().getResult().getItemMeta().getDisplayName())) {
                    if (eventInventory.getType() == InventoryType.DISPENSER) {
                        if (lastRecipe != null) {
                            if (lastRecipe.checkRecipe(eventInventory)
                                    && (player.hasPermission("crapi.craft." + lastRecipe.getPermission()) || player.hasPermission("crapi.craftall"))) {
                                if (inv.addItem(lastRecipe.getResult()).keySet().size() == 0) {
                                    removeRecipeFromTable(lastRecipe);
                                }
                            } else {
                                lastRecipe = null;
                                delay = 20;
                            }
                        } else {
                            for (CustomRecipe recipe : APIManager.getRecipes()) {
                                if (recipe.checkRecipe(eventInventory)
                                        && (player.hasPermission("crapi.craft." + recipe.getPermission()) || player.hasPermission("crapi.craftall"))) {
                                    lastRecipe = recipe;
                                    delay = 0;
                                    if (inv.addItem(lastRecipe.getResult()).keySet().size() == 0) {
                                        removeRecipeFromTable(lastRecipe);
                                    } else {
                                        player.sendMessage(ChatColor.RED.toString() + "Inventory is full. Cannot craft item.");
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void removeRecipeFromTable(CustomRecipe recipe) {
        for (int i = 0; i < 9; i++) {
            if (eventInventory.getItem(i) != null) {
                eventInventory.getItem(i).setAmount(eventInventory.getItem(i).getAmount() - recipe.getItem(i).getAmount());
            }
        }
    }
}
