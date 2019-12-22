package me.darkolythe.customrecipeapi;

import org.bukkit.block.Block;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class CustomRecipeAPI {

    private List<CustomRecipe> recipes;
    private ShapedRecipe workbench;

    private RecipeListener recipelistener;
    public Plugin plugin;

    private List<Integer[]> workbenchCoords;

    /**
     * CustomRecipeAPI constructor.
     * @param newPlugin Pass the plugin through to the API
     */
    public CustomRecipeAPI(Plugin newPlugin) {
        recipes = new ArrayList<>();
        workbenchCoords = new ArrayList<>();
        plugin = newPlugin;
    }

    /**
     * Add a recipe to the custom recipe list.
     * @param newRecipe CustomRecipe object to add
     */
    public void addRecipe(CustomRecipe newRecipe) {
        recipes.add(newRecipe);
    }

    /**
     * Remove a recipe from the custom recipe list.
     * @param newRecipe CustomRecipe object to remove
     */
    public void removeRecipe(CustomRecipe newRecipe) {
        recipes.remove(newRecipe);
    }

    List<CustomRecipe> getRecipes() {
        return recipes;
    }

    /**
     * Sets the recipe and item for the workbench with the recipe.
     * @param newRecipe ShapedRecipe for workbench
     */
    public void setWorkBench(ShapedRecipe newRecipe) {
        workbench = newRecipe;
    }

    ShapedRecipe getWorkbench() {
        return workbench;
    }

    void addWorkbench(Block block) {
        Integer[] coords = {block.getX(), block.getY(), block.getZ()};
        workbenchCoords.add(coords);
    }

    void removeWorkbench(Block block) {
        Integer[] coords = {block.getX(), block.getY(), block.getZ()};
        workbenchCoords.remove(coords);
    }

    boolean isWorkbench(Block block) {
        Integer[] coords = {block.getX(), block.getY(), block.getZ()};
        return workbenchCoords.contains(coords);
    }
}
