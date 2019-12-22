package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftItemTask extends BukkitRunnable {

    private Player player;
    private Inventory eventInventory;
    private CustomRecipeAPI main;

    public CraftItemTask(Player newPlayer, Inventory newEventInventory, CustomRecipeAPI newMain) {
        player = newPlayer;
        eventInventory = newEventInventory;
        main = newMain;
    }

    @Override
    public void run() {
        Inventory inv = player.getInventory();
        if (player.getOpenInventory().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Special Crafting")) {
            if (eventInventory.getType() == InventoryType.DISPENSER) {
                for (CustomRecipe recipe : main.getManager().getRecipes()) {
                    if (recipe.checkRecipe(eventInventory)) {
                        if (getFirst(inv, recipe.getResult()) != -1) {
                            int index = getFirst(inv, recipe.getResult());
                            if (inv.getItem(index) != null && inv.getItem(index).getType() != Material.AIR) {
                                ItemStack item = inv.getItem(index);
                                item.setAmount(item.getAmount() + recipe.getResult().getAmount());
                            } else {
                                inv.setItem(index, recipe.getResult());

                                for (int i = 0; i < 9; i++) {
                                    inv.getItem(i).setAmount(inv.getItem(i).getAmount() - recipe.getItem(i).getAmount());
                                }

                                this.cancel();
                            }
                        } else {
                            player.sendMessage(ChatColor.RED.toString() + "Inventory is full. Cannot craft item.");
                        }
                    }
                }
            }
        }
    }


    /**
     * Get the first slot in the player's inventory that can hold the requested item. -1 for no slot.
     * @param inv Inventory - Player's Inventory
     * @param item ItemStack to fit in the inventory
     * @return
     */
    private int getFirst(Inventory inv, ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
                if (cloneOne(inv.getItem(i)).equals(cloneOne(item))) {
                    if ((inv.getItem(i).getAmount() + item.getAmount()) <= inv.getItem(i).getMaxStackSize()) {
                        return i;
                    }
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
