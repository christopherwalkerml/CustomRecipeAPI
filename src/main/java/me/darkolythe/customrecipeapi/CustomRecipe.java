package me.darkolythe.customrecipeapi;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipe {

    private List<ItemStack> recipe;
    private ItemStack result;

    /**
     * Create a new recipe with ItemStacks. The order is the same as the ShapedRecipe order. For blank spots, use AIR as an ItemStack.
     * The recipe must first be created, then added to the CustomRecipeAPI to be in use.
     * @param result the item to return when crafted.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    public CustomRecipe(ItemStack newResult, ItemStack... newRecipe) {
        result = newResult;
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

    boolean checkRecipe(Inventory inv) {
        for (int i = 0; i < recipe.size(); i++) {
            if ((inv.getItem(i) == null && recipe.get(i).getType() != Material.AIR) || !inv.getItem(i).equals(recipe.get(i))) {
                return false;
            }
        }
        return true;
    }

    ItemStack getResult() {
        return result;
    }
}
