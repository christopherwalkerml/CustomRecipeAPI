package me.darkolythe.customrecipeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

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
                    event.getPlayer().openInventory(Bukkit.getServer().createInventory(null, InventoryType.DISPENSER, ChatColor.BOLD.toString() + "Special Crafting"));
                }
            }
        }
    }

    @EventHandler
    private void recipeCheck(InventoryClickEvent event) {

    }
}
