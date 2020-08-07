package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomRecipeAPI extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "CRAPI" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";
    public static String workbenchName = ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Special Crafting";

    static CustomRecipeAPI plugin;

    private static RecipeListener recipelistener;
    private static BookListener booklistener;
    private static RecipeCreator recipecreator;
    private static APIManager apimanager;
    private static WorkbenchManager workbenchmanager;
    private static ConfigHandler confighandler;

    @Override
    public void onEnable() {
        plugin = this;

        ConfigurationSerialization.registerClass(CustomRecipe.class);

        apimanager = new APIManager();

        recipelistener = new RecipeListener(this);
        booklistener = new BookListener();
        recipecreator = new RecipeCreator();
        confighandler = new ConfigHandler();
        workbenchmanager = new WorkbenchManager();

        confighandler.setup();
        getServer().getPluginManager().registerEvents(recipelistener, this);
        getServer().getPluginManager().registerEvents(booklistener, this);
        getServer().getPluginManager().registerEvents(recipecreator, this);
        getServer().getPluginManager().registerEvents(workbenchmanager, this);

        getCommand("crapi").setExecutor(new CommandHandler());

        saveDefaultConfig();
        workbenchName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("workbenchtitle"));

        confighandler.loadRecipes();
        confighandler.loadWorkbench();

        System.out.println(prefix + ChatColor.GREEN + "CustomRecipeAPI enabled!");
    }

    @Override
    public void onDisable() {
        confighandler.saveRecipes();
        confighandler.saveWorkbench();
    }

    public static APIManager getManager() {
        return apimanager;
    }

    public static ConfigHandler getConfigHandler() {
        return confighandler;
    }

    /**
     * Create a new recipe with ItemStacks. The order is the same as the ShapedRecipe order. For blank spots, use AIR as an ItemStack.
     * The recipe must first be created, then added to the CustomRecipeAPI to be in use.
     * @param newResult the ItemStack to return when crafted.
     * @param newRecipe ItemStacks to use in recipe. max of 9. 10+ will be ignored.
     */
    public static CustomRecipe createRecipe(ItemStack newResult, ItemStack... newRecipe) {
        CustomRecipe newCustomRecipe = new CustomRecipe(newResult, newRecipe);
        newCustomRecipe.setFromPlugin(true);

        APIManager.addRecipe(newCustomRecipe);
        return newCustomRecipe;
    }

    static CustomRecipeAPI getInstance() {
        return plugin;
    }
}
