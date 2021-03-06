package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

import static me.darkolythe.customrecipeapi.CustomRecipe.getClosestMatch;

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
                if (APIManager.getWorkbench() != null
                        && APIManager.getWorkbench().getResult().hasItemMeta()
                        && player.getOpenInventory().getTitle().equals(APIManager.getWorkbench().getResult().getItemMeta().getDisplayName())) {
                    if (eventInventory.getType() == InventoryType.DISPENSER) {
                        if (lastRecipe != null) {
                            if (lastRecipe.checkRecipe(eventInventory)
                                    && (player.hasPermission("crapi.craft." + lastRecipe.getPermission()) || player.hasPermission("crapi.craftall"))) {
                                if (inv.addItem(lastRecipe.getResult().clone()).keySet().size() == 0) {
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
                                    if (inv.addItem(lastRecipe.getResult().clone()).keySet().size() == 0) {
                                        removeRecipeFromTable(lastRecipe);
                                    } else {
                                        player.sendMessage(LanguageManager.getValue("fullinv"));
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
        if (recipe.getShaped()) {
            for (int i = 0; i < 9; i++) {
                if (eventInventory.getItem(i) != null) {
                    eventInventory.getItem(i).setAmount(eventInventory.getItem(i).getAmount() - recipe.getItem(i).getAmount());
                }
            }
        } else {
            ArrayList<ItemStack> invItems = new ArrayList<>(Arrays.asList(eventInventory.getContents()));
            ArrayList<ItemStack> recipeItems = new ArrayList<>(Arrays.asList(recipe.getRecipe()));
            recipeItems.removeIf(i -> (i.getType() == Material.AIR));

            for (ItemStack r : recipeItems) {
                int index = getClosestMatch(r, invItems);
                eventInventory.getItem(index).setAmount(eventInventory.getItem(index).getAmount() - r.getAmount());
                invItems.set(index, null);
            }
        }
    }
}
