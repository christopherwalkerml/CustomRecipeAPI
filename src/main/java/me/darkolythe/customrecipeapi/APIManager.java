package me.darkolythe.customrecipeapi;

import org.bukkit.block.Block;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class APIManager {

    private static List<CustomRecipe> recipes;
    private static ShapedRecipe workbench;

    public static Plugin plugin;

    /**
     * CustomRecipeAPI constructor.
     */
    public APIManager(CustomRecipeAPI newPlugin) {
        recipes = new ArrayList<>();
        plugin = newPlugin;
    }

    /**
     * Add a recipe to the custom recipe list.
     * @param newRecipe CustomRecipe object to add
     */
    public static void addRecipe(CustomRecipe newRecipe) {
        recipes.add(newRecipe);
    }

    /**
     * Remove a recipe from the custom recipe list.
     * @param newRecipe CustomRecipe object to remove
     */
    public static void removeRecipe(CustomRecipe newRecipe) {
        recipes.remove(newRecipe);
    }

    List<CustomRecipe> getRecipes() {
        return recipes;
    }

    /**
     * Sets the recipe and item for the workbench with the recipe.
     * @param newRecipe ShapedRecipe for workbench
     */
    public static void setWorkBench(ShapedRecipe newRecipe) {
        workbench = newRecipe;
    }

    ShapedRecipe getWorkbench() {
        return workbench;
    }
}
