package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomRecipeAPI extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "CRAPI" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    private static RecipeListener recipelistener;
    private static APIManager apimanager;

    @Override
    public void onEnable() {
        apimanager = new APIManager();

        recipelistener = new RecipeListener(this);

        getServer().getPluginManager().registerEvents(recipelistener, this);

        System.out.println(prefix + ChatColor.GREEN + "CustomRecipeAPI enabled!");
    }

    public static APIManager getManager() {
        return apimanager;
    }

    /**
     * Create a new recipe with ItemStacks. The order is the same as the ShapedRecipe order. For blank spots, use AIR as an ItemStack.
     * The recipe must first be created, then added to the CustomRecipeAPI to be in use.
     * @param newResult the ItemStack to return when crafted.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    public static CustomRecipe createRecipe(ItemStack newResult, ItemStack... newRecipe) {
        CustomRecipe newCustomRecipe = new CustomRecipe(newResult, newRecipe);

        APIManager.addRecipe(newCustomRecipe);
        return newCustomRecipe;
    }
}
