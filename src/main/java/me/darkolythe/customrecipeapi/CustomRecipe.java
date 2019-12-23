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
     * @param newResult the ItemStack to return when crafted.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    CustomRecipe(ItemStack newResult, ItemStack... newRecipe) {
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
            ItemStack invitem = inv.getItem(i);
            ItemStack recitem = recipe.get(i);
            if (invitem == null) {
                invitem = new ItemStack(Material.AIR);
            }

            if (invitem.getType() != recitem.getType()) {
                return false;
            }
            if (!cloneOne(invitem).equals(cloneOne(recitem)) || invitem.getAmount() < recitem.getAmount()) {
                return false;
            }
        }
        return true;
    }

    ItemStack getResult() {
        return result;
    }

    ItemStack getItem(int i) {
        return recipe.get(i);
    }

    private ItemStack cloneOne(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        return clone;
    }
}
