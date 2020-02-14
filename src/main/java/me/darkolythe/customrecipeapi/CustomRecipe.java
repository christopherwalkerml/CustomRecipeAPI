package me.darkolythe.customrecipeapi;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomRecipe {

    private List<ItemStack> recipe;
    private ItemStack result;
    private boolean forced;
    private String ID;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CustomRecipe)) {
            return false;
        }
        CustomRecipe recipe = (CustomRecipe) o;
        if (recipe.getResult().equals(((CustomRecipe) o).getResult())) {
            for (int i = 0; i < 9; i++) {
                if (!recipe.getItem(i).equals(this.getItem(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String str = System.lineSeparator() + "ID: " + getID() + System.lineSeparator() + "Result:" + System.lineSeparator();
        str += "    " + this.getResult() + System.lineSeparator();
        str += "Recipe" + System.lineSeparator();
        for (int i = 0; i < 9; i++) {
            str += "    " + this.getItem(i) + System.lineSeparator();
        }
        return str;
    }

    /**
     * Create a new recipe with ItemStacks. The order is the same as the ShapedRecipe order. For blank spots, use AIR as an ItemStack.
     * The recipe must first be created, then added to the CustomRecipeAPI to be in use.
     * @param newResult the ItemStack to return when crafted.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    CustomRecipe(ItemStack newResult, ItemStack... newRecipe) {
        result = newResult;
        recipe = new ArrayList<>();
        forced = true;
        setRecipeID();
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

    void setForced(boolean setForced) {
        forced = setForced;
    }

    boolean getForced() {
        return forced;
    }

    ItemStack getResult() {
        return result;
    }

    ItemStack[] getRecipe() {
        ItemStack items[] = {recipe.get(0), recipe.get(1), recipe.get(2), recipe.get(3), recipe.get(4), recipe.get(5), recipe.get(6), recipe.get(7), recipe.get(8)};
        return items;
    }

    ItemStack getItem(int i) {
        return recipe.get(i);
    }

    private ItemStack cloneOne(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        return clone;
    }

    private void setRecipeID() { //a request id is given to every unique request. this makes it so players have the correct amount of contributions
        String genstr;

        int min = 97; //char 'a'
        int max = 122; //char 'z'
        int strlen = 16; //the length of the item ID
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(strlen);
        for (int i = 0; i < strlen; i++) {
            int randomLimitedInt = min + (int)
                    (random.nextFloat() * (max - min + 1));
            buffer.append((char) randomLimitedInt);
        }
        genstr = buffer.toString();

        ID = genstr;
    }

    public String getID() {
        return ID;
    }

    /**
     * DO NOT USE.
     * @param id String recipe id
     */
    void setID(String id) {
        ID = id;
    }
}
