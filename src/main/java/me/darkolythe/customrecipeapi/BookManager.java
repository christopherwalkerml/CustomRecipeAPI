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

import static me.darkolythe.customrecipeapi.RecipeCreator.createShapelessItem;

public class BookManager {

    public static Inventory getRecipeBook(Player player, int page) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Recipe Book");

        createBottomRow(inv, page);

        for (int i = 0; i < 45; i++) {
            if ((page * 45) + i < APIManager.getRecipes().size()) {
                inv.setItem(inv.firstEmpty(), createRecipe(APIManager.getRecipes().get((page * 45) + i), player));
            } else {
                break;
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

        if (player.hasPermission("crapi.giveitem")) {
            lore.add(ChatColor.GRAY + "Right click to give yourself this item.");
        }
        if (player.hasPermission("crapi.op")) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Permission: crapi.craft." + recipe.getPermission());
        }
        lore.add(ChatColor.DARK_GRAY + "id: " + recipe.getID());
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void createBottomRow(Inventory inv, int page) {
        if (APIManager.getRecipes().size() > (45 * (page + 1))) {
            ItemStack invstack = createArrow("Next Page", "Current page", page);
            inv.setItem(53, invstack);
        }
        if (page >= 1) {
            ItemStack invstack = createArrow("Previous Page", "Current page", page);
            inv.setItem(45, invstack);
        }
    }

    private static ItemStack createArrow(String name, String lore, int page) {
        ItemStack invstack = new ItemStack(Material.ARROW, 1);
        ItemMeta invmeta = invstack.getItemMeta();
        invmeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + name);
        invmeta.setLore(Arrays.asList(ChatColor.GRAY.toString() + lore + ": " + ChatColor.GREEN.toString() + page));
        invstack.setItemMeta(invmeta);

        return invstack;
    }

    static void openRecipe(Player player, ItemStack item) {
        CustomRecipe recipe = getRecipeFromItem(item);
        if (recipe != null) {

            Inventory inv = Bukkit.createInventory(player, 45, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Custom Recipe View");

            fillEmpty(inv);

            for (int i = 0; i < 9; i++) {
                inv.setItem((i % 3) + 11 + (9 * (i / 3)), recipe.getItem(i));
            }
            inv.setItem(24, recipe.getResult());

            if (player.hasPermission("crapi.new")) {
                if (!recipe.getForced()) {
                    inv.setItem(36, createDeleteButton());
                }
            }

            inv.setItem(8, createShapelessItem(recipe.getShaped()));

            inv.setItem(44, createBackButton());

            player.openInventory(inv);
        }
    }

    static CustomRecipe getRecipeFromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        String id = meta.getLore().get(meta.getLore().size() - 1).replaceAll("^[^_]*:", "").replace(" ", "");
        for (CustomRecipe recipe : APIManager.getRecipes()) {
            if (recipe.getID().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    static ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Back to Menu");
        item.setItemMeta(meta);

        return item;
    }

    static ItemStack createDeleteButton() {
        ItemStack del = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta delmeta = del.getItemMeta();
        delmeta.setDisplayName(ChatColor.RED + "Delete Recipe");
        delmeta.setLore(Arrays.asList(ChatColor.GRAY + "cannot be undone"));
        del.setItemMeta(delmeta);
        return del;
    }

    static void fillEmpty(Inventory inv) {
        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = empty.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Recipe View");
        empty.setItemMeta(meta);

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, empty.clone());
        }
    }
}
