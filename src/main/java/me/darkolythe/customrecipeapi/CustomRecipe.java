package me.darkolythe.customrecipeapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipe {

    private List<ItemStack> recipe;

    /**
     * Create a new recipe with ItemStacks. The order is the same as the ShapedRecipe order. For blank spots, use AIR as an ItemStack.
     * The recipe must first be created, then added to the CustomRecipeAPI to be in use.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    public CustomRecipe(ItemStack... newRecipe) {
        recipe = new ArrayList<>();
        int size = newRecipe.length;
        for (int i = 0; i < size; i++) {
            if (i == 9) {
                break;
            }
            recipe.add(newRecipe[i]);
        }

        for (int i = 0; i < 9 - recipe.size(); i++) {
            recipe.add(new ItemStack(Material.AIR));
        }
    }
}
