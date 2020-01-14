package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomRecipeAPI extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "CRAPI" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    static CustomRecipeAPI plugin;

    private static RecipeListener recipelistener;
    private static BookListener booklistener;
    private static APIManager apimanager;

    @Override
    public void onEnable() {
        plugin = this;

        apimanager = new APIManager();

        recipelistener = new RecipeListener(this);
        booklistener = new BookListener();

        getServer().getPluginManager().registerEvents(recipelistener, this);
        getServer().getPluginManager().registerEvents(booklistener, this);

        getCommand("crapi").setExecutor(new CommandHandler());

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

    static CustomRecipeAPI getInstance() {
        return plugin;
    }
}
