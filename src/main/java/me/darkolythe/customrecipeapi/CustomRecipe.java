package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomRecipe implements ConfigurationSerializable {

    private List<ItemStack> recipe;
    private ItemStack result;
    private boolean forced;
    private String ID = "";
    private String permission = "";
    private boolean fromPlugin = false;
    private boolean isShaped = true;

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
        String str = System.lineSeparator() + "ID: " + getID() + System.lineSeparator();
        str += "Shaped: " + isShaped + System.lineSeparator();
        str += "Result: " + System.lineSeparator();
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
        setPermission();
        int size = newRecipe.length;
        for (int i = 0; i < size; i++) {
            if (i == 9) {
                break;
            }
            recipe.add(newRecipe[i]);
        }

        for (int i = 0; i < 9 - recipe.size(); i++) {
            recipe.add(new ItemStack(Material.AIR, 1));
        }

        for (int i = 0; i < recipe.size(); i++) {
            if (recipe.get(i).getAmount() == 0) {
                recipe.get(i).setAmount(1);
            }
        }
    }

    CustomRecipe(ItemStack newResult, List<ItemStack> newRecipe, boolean newForced, String newID, boolean shaped) {
        result = newResult;
        recipe = newRecipe;
        forced = newForced;
        ID = newID;
        isShaped = shaped;
        setPermission();

        for (int i = 0; i < recipe.size(); i++) {
            if (recipe.get(i).getAmount() == 0) {
                recipe.set(i, new ItemStack(Material.AIR, 1));
            }
        }
    }

    public void setFromPlugin(boolean val) {
        fromPlugin = val;
    }

    public boolean getFromPlugin() {
        return fromPlugin;
    }

    public void setShaped(boolean shapeless) {
        isShaped = shapeless;
    }

    public boolean getShaped() {
        return isShaped;
    }

    boolean checkRecipe(Inventory inv) {
        if (isShaped) {
            for (int i = 0; i < recipe.size(); i++) {
                ItemStack invitem = inv.getItem(i);
                ItemStack recitem = recipe.get(i);
                if (invitem == null) {
                    invitem = new ItemStack(Material.AIR, 1);
                }

                if (invitem.getType() != recitem.getType()) {
                    return false;
                }
                if (!cloneOne(invitem).equals(cloneOne(recitem)) || invitem.getAmount() < recitem.getAmount()) {
                    return false;
                }
            }
        } else {
            List<ItemStack> invItems = new ArrayList<>(Arrays.asList(inv.getContents()));
            List<ItemStack> recipeItems = new ArrayList<>(recipe);

            invItems.removeIf(i -> (i == null));
            recipeItems.removeIf(i -> (i.getType() == Material.AIR));

            if (invItems.size() != recipeItems.size()) {
                return false;
            }

            for (ItemStack r : recipeItems) {
                int index = getClosestMatch(r, invItems);

                if (index != -1) {
                    invItems.remove(index);
                } else {
                    return false;
                }
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

    public static ItemStack cloneOne(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(0);
        return clone;
    }

    public static int getClosestMatch(ItemStack item, List<ItemStack> list) {
        int amount = 64;
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            ItemStack litem = list.get(i);
            if (litem != null) {
                if (!cloneOne(item).equals(cloneOne(litem)))
                    continue;

                if (item.getAmount() > litem.getAmount())
                    continue;

                if (litem.getAmount() - item.getAmount() < amount) {
                    amount = litem.getAmount() - item.getAmount();
                    index = i;
                }
            }
        }
        return index;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission() {
        if (result.hasItemMeta() && result.getItemMeta().hasDisplayName()) {
            permission = ChatColor.stripColor(result.getItemMeta().getDisplayName()).replaceAll("([^a-zA-Z0-9])", "").toLowerCase();
            return;
        }
        permission = result.getType().toString().toLowerCase();
    }

    private void setRecipeID() { //a recipe id is given to every unique recipe
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


    /*
        private List<ItemStack> recipe;
    private ItemStack result;
    private boolean forced;
    private String ID = "";
    private String permission = "";
     */

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("recipe", this.recipe);
        result.put("result", this.result);
        result.put("forced", this.forced);
        result.put("id", this.ID);
        result.put("shaped", this.isShaped);

        return result;
    }

    public static CustomRecipe deserialize(Map<String, Object> args) {
        List<ItemStack> newRecipe = new ArrayList<>();
        ItemStack newResult;
        boolean newForced;
        String newID;
        boolean shaped = true;

        List<?> list = (List<?>) args.get("recipe");
        for (Object o : list) {
            newRecipe.add((ItemStack) o);
        }
        newResult = (ItemStack) args.get("result");
        newForced = (Boolean) args.get("forced");
        newID = (String) args.get("id");

        if (args.containsKey("shaped")) {
            shaped = (boolean) args.get("shaped");
        }

        return new CustomRecipe(newResult, newRecipe, newForced, newID, shaped);
    }
}
