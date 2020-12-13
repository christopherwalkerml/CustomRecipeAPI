package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class RecipeCreator implements Listener {

    private CustomRecipeAPI main = CustomRecipeAPI.getInstance();

    static Inventory openCreator(Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, LanguageManager.getValue("customrecipecreator"));
        BookManager.fillEmpty(inv);
        inv.setItem(11, null);
        inv.setItem(12, null);
        inv.setItem(13, null);
        inv.setItem(20, null);
        inv.setItem(21, null);
        inv.setItem(22, null);
        inv.setItem(29, null);
        inv.setItem(30, null);
        inv.setItem(31, null);
        inv.setItem(24, null);

        inv.setItem(8, createShapelessItem(true));
        inv.setItem(44, createCreateItem());

        return inv;
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getView().getTopInventory() && event.getView().getTitle().equals(LanguageManager.getValue("customrecipecreator"))) {

            Inventory inv = event.getClickedInventory();
            int slot = event.getRawSlot();
            if (slot != 11 && slot != 12 && slot != 13 && slot != 20 && slot != 21 && slot != 22 && slot != 29 && slot != 30 && slot != 31 && slot != 24) {
                event.setCancelled(true);
                if (inv.getItem(slot) != null && inv.getItem(slot).equals(createCreateItem())) {
                    if (inv.getItem(24) != null && inv.getItem(24).getType() != Material.AIR) {
                        CustomRecipe recipe = createRecipeFromInventory(inv);
                        if (recipe != null) {
                            CustomRecipeAPI.getManager().addRecipe(recipe);
                            CustomRecipeAPI.getConfigHandler().saveRecipes();
                            event.getWhoClicked().closeInventory();
                            return;
                        }
                    }
                    event.getWhoClicked().sendMessage(CustomRecipeAPI.prefix + LanguageManager.getValue("invalidrecipe"));
                } else if (inv.getItem(slot) != null && slot == 8) {
                    inv.setItem(8, createShapelessItem(!getCurrentShape(inv.getItem(slot))));
                }
            }
        }
    }

    static CustomRecipe createRecipeFromInventory(Inventory inv) {
        CustomRecipe r = null;
        if (inv.getItem(24) != null && inv.getItem(24).getType() != Material.AIR) {

            boolean valid = false;
            ItemStack recipe[] = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                ItemStack item = inv.getItem((i % 3) + 11 + (9 * (i / 3)));
                if (item == null || item.getType() == Material.AIR) {
                    item = new ItemStack(Material.AIR);
                }
                if (item.getType() != Material.AIR) {
                    valid = true;
                }
                recipe[i] = item;
            }
            if (valid) {
                r = new CustomRecipe(inv.getItem(24), recipe[0], recipe[1], recipe[2], recipe[3], recipe[4], recipe[5], recipe[6], recipe[7], recipe[8]);
                r.setForced(false);
                r.setShaped(getCurrentShape(inv.getItem(8)));
            }
        }
        return r;
    }

    private static ItemStack createCreateItem() {
        ItemStack create = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta meta = create.getItemMeta();
        meta.setDisplayName(LanguageManager.getValue("createrecipe"));
        create.setItemMeta(meta);
        return create;
    }

    public static ItemStack createShapelessItem(boolean isShaped) {
        ItemStack shapeless = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = shapeless.getItemMeta();
        meta.setDisplayName(LanguageManager.getValue("toggleshapeless"));
        meta.setLore(Arrays.asList("", ChatColor.GRAY + LanguageManager.getValue("shaped") + ": " + ChatColor.BLUE + isShaped));
        shapeless.setItemMeta(meta);
        return shapeless;
    }

    private static boolean getCurrentShape(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(1).contains("true")) {
            return true;
        }
        return false;
    }
}
