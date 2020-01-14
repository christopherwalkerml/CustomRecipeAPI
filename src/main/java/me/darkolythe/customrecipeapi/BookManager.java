package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookManager {

    public static Inventory getRecipeBook(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Recipe Book");

        createBottomRow(inv, page);

        for (int i = 0; i < 45; i++) {
            if ((page * 45) + i < APIManager.getRecipes().size()) {
                inv.addItem(createRecipe(APIManager.getRecipes().get((page * 45) + i), player));
            }
        }
        return inv;
    }

    private static ItemStack createRecipe(CustomRecipe recipe, Player player) {
        ItemStack item = recipe.getResult().clone();
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add("");
        lore.add(ChatColor.GRAY + "Left click to view recipe.");

        if (player.hasPermission("deepstorageplus.giveitem")) {
            lore.add(ChatColor.GRAY + "Right click to give yourself this item.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void createBottomRow(Inventory inv, int page) {
        if (APIManager.getRecipes().size() > (45 * (page + 1))) {
            ItemStack invstack = createArrow("Next Page", "Current page", page);
            inv.setItem(53, invstack);
        }
        if (page > 1) {
            ItemStack invstack = createArrow("Previous Page", "Current page", page);
            inv.setItem(45, invstack);
        }
    }

    private static ItemStack createArrow(String name, String lore, int page) {
        ItemStack invstack = new ItemStack(Material.ARROW, 1);
        ItemMeta invmeta = invstack.getItemMeta();
        invmeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + name);
        invmeta.setLore(Arrays.asList(ChatColor.GREEN.toString() + lore + ": " + page));
        invstack.setItemMeta(invmeta);

        return invstack;
    }

    static void openRecipe(Player player, ItemStack item) {
        CustomRecipe recipe = getRecipeFromItem(item, player);
        if (recipe != null) {

            Inventory inv = Bukkit.createInventory(player, 45, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Custom Recipe View");

            ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = empty.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Recipe View");
            empty.setItemMeta(meta);

            for (int i = 0; i < 45; i++) {
                inv.setItem(i, empty.clone());
            }

            inv.setItem(11, recipe.getItem(0));
            inv.setItem(12, recipe.getItem(1));
            inv.setItem(13, recipe.getItem(2));
            inv.setItem(20, recipe.getItem(3));
            inv.setItem(21, recipe.getItem(4));
            inv.setItem(22, recipe.getItem(5));
            inv.setItem(29, recipe.getItem(6));
            inv.setItem(30, recipe.getItem(7));
            inv.setItem(31, recipe.getItem(8));
            inv.setItem(24, recipe.getResult());

            player.openInventory(inv);
        }
    }

    static CustomRecipe getRecipeFromItem(ItemStack item, Player player) {
        for (CustomRecipe recipe : APIManager.getRecipes()) {
            if (createRecipe(recipe, player).equals(item)) {
                return recipe;
            }
        }
        return null;
    }
}
