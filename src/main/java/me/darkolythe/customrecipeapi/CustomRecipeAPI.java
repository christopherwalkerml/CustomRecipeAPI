package me.darkolythe.customrecipeapi;

import org.bukkit.block.Block;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public final class CustomRecipeAPI {

    private List<CustomRecipe> recipes;
    private ShapedRecipe workbench;

    private RecipeListener recipelistener;

    private List<Integer[]> workbenchCoords;

    /**
     * CustomRecipeAPI constructor.
     */
    public CustomRecipeAPI() {
        recipes = new ArrayList<>();
        workbenchCoords = new ArrayList<>();
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
