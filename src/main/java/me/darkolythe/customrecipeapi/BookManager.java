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
        Inventory inv = Bukkit.createInventory(player, 54, LanguageManager.getValue("recipebook"));

        createBottomRow(inv, page);

        int i = 0;
        int count = 0;
        while (count < 45 && (page * 45) + i < APIManager.getRecipes().size()) {
            CustomRecipe recipe = APIManager.getRecipes().get((page * 45) + i);
            if (player.hasPermission("crapi.craft." + recipe.getPermission()) || player.hasPermission("crapi.craftall")) {
                inv.setItem(inv.firstEmpty(), createRecipe(recipe, player));
                count += 1;
            }
            i += 1;
        }
        return inv;
    }

    private static ItemStack createRecipe(CustomRecipe recipe, Player player) {
        ItemStack item = recipe.getResult().clone();
        ItemMeta meta = item.getItemMeta();

        if (item.getType() == Material.AIR) {
            return new ItemStack(Material.AIR);
        }

        List<String> lore = meta.getLore();
        if (meta.getLore() == null) {
            lore = new ArrayList<>();
        }
        lore.add("");
        lore.add(LanguageManager.getValue("leftclicktoview"));

        if (player.hasPermission("crapi.giveitem")) {
            lore.add(LanguageManager.getValue("rightclicktogive"));
        }
        if (player.hasPermission("crapi.op")) {
            lore.add("");
            lore.add(ChatColor.RED + LanguageManager.getValue("permission") + ":" + ChatColor.GRAY + " crapi.craft." + recipe.getPermission());
        }
        lore.add(ChatColor.DARK_GRAY + "id: " + recipe.getID());
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void createBottomRow(Inventory inv, int page) {
        if (APIManager.getRecipes().size() > (45 * (page + 1))) {
            ItemStack invstack = createArrow(LanguageManager.getValue("nextpage"), LanguageManager.getValue("Current Page"), page);
            inv.setItem(53, invstack);
        }
        if (page >= 1) {
            ItemStack invstack = createArrow(LanguageManager.getValue("previouspage"), LanguageManager.getValue("currentpage"), page);
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

            Inventory inv = Bukkit.createInventory(player, 45, LanguageManager.getValue("customrecipeview"));

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

        meta.setDisplayName(LanguageManager.getValue("backtomenu"));
        item.setItemMeta(meta);

        return item;
    }

    static ItemStack createDeleteButton() {
        ItemStack del = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta delmeta = del.getItemMeta();
        delmeta.setDisplayName(LanguageManager.getValue("deleterecipe"));
        delmeta.setLore(Arrays.asList(LanguageManager.getValue("cannotbeundone")));
        del.setItemMeta(delmeta);
        return del;
    }

    static void fillEmpty(Inventory inv) {
        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = empty.getItemMeta();
        meta.setDisplayName(LanguageManager.getValue("recipeview"));
        empty.setItemMeta(meta);

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, empty.clone());
        }
    }
}
