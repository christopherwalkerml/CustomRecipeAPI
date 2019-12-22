package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class RecipeListener implements Listener {

    Map<Player, BukkitTask> taskList = new HashMap<>();

    private CustomRecipeAPI main;
    RecipeListener(CustomRecipeAPI plugin) {
        main = plugin;
    }

    @EventHandler
    private void recipeCheck(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                CraftItemTask task = new CraftItemTask(player, event.getClickedInventory(), main);
                BukkitTask id = task.runTaskTimer(main, 1L, 400L);
                taskList.put(player, id);
            }
        }
    }

    @EventHandler
    private void onCraftingClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (taskList.containsKey(player)) {
                Bukkit.getServer().getScheduler().cancelTask(taskList.get(player).getTaskId());
                taskList.remove(player);
            }
        }
    }
}
