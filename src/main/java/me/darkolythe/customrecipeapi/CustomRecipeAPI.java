package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CustomRecipeAPI extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "CRAPI" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    private List<CustomRecipe> recipes;

    @Override
    public void onEnable() {

        recipes = new ArrayList<>();

        System.out.println(prefix + ChatColor.GREEN + "CRAPI enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println(prefix + ChatColor.RED + "CRAPI disabled!");
    }

    void addRecipe(CustomRecipe newRecipe) {
        recipes.add(newRecipe);
    }

    void removeRecipe(CustomRecipe newRecipe) {
        recipes.remove(newRecipe);
    }
}
