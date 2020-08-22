package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.util.*;

import static me.darkolythe.customrecipeapi.CustomRecipeAPI.workbenchName;

public class WorkbenchManager implements Listener {

    private static CustomRecipeAPI main = CustomRecipeAPI.getInstance();

    static Inventory createWorkbenchCreator() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Workbench Recipe (close to update)");
        fillInventory(inv);
        return inv;
    }

    static Inventory createWorkbenchViewer() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Workbench Recipe View");
        fillInventory(inv);
        return inv;
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getView().getTopInventory();
        if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Workbench Recipe (close to update)")) {
            Map<Character, ItemStack> map = new HashMap<>();
            List<Character> order = new ArrayList<>();
            char c = 'A';
            for (ItemStack i : inv.getContents()) {
                if (i == null) {
                    i = new ItemStack(Material.AIR);
                }
                if (!map.containsValue(i)) {
                    c++;
                    map.put((char) ((int) c), i);
                }
                for (char chr : map.keySet()) {
                    if (map.get(chr).equals(i)) {
                        order.add(chr);
                        break;
                    }
                }
            }

            setRecipe(order, map);

            CustomRecipeAPI.getConfigHandler().saveWorkbench();

            event.getPlayer().sendMessage(CustomRecipeAPI.prefix + ChatColor.GREEN + "Custom Crafting table recipe updated.");
        }
    }

    public static void setRecipe(List<Character> order, Map<Character, ItemStack> map) {
        ItemStack workbenchItem = new ItemStack(Material.DISPENSER);
        ItemMeta meta = workbenchItem.getItemMeta();
        meta.setDisplayName(workbenchName);
        workbenchItem.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(main, "custom_workbench"), workbenchItem);
        recipe.shape("" + order.get(0) + order.get(1) + order.get(2), "" + order.get(3) + order.get(4) + order.get(5), "" + order.get(6) + order.get(7) + order.get(8));
        for (char chr : map.keySet()) {
            recipe.setIngredient(chr, map.get(chr).getType());
        }

        if (APIManager.getWorkbench() != null) {
            Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
            Recipe r;
            while (it.hasNext()) {
                r = it.next();
                if (r != null && r.getResult().equals(APIManager.getWorkbench().getResult())) {
                    it.remove();
                }
            }
        }
        Bukkit.getServer().addRecipe(recipe);

        APIManager.setWorkBench(recipe);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Workbench Recipe View")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.discoverRecipe(new NamespacedKey(main, "custom_workbench"));
    }

    private static void fillInventory(Inventory inv) {
        if (APIManager.getWorkbench() != null) {
            int index = 0;
            for (ItemStack item : recipeList()) {
                inv.setItem(index, item);
                index++;
            }
        }
    }

    public static List<ItemStack> recipeList() {
        List<ItemStack> items = new ArrayList<>();
        Map<Character, ItemStack> im = APIManager.getWorkbench().getIngredientMap();
        for (String s : APIManager.getWorkbench().getShape()) {
            for (Character c : s.toCharArray()) {
                items.add(im.get(c));
            }
        }
        return items;
    }


}
