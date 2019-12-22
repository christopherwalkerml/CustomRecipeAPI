package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomRecipeAPI extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "CRAPI" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    private static RecipeListener recipelistener;
    private static APIManager apimanager;

    @Override
    public void onEnable() {
        apimanager = new APIManager(this);

        recipelistener = new RecipeListener(this);

        getServer().getPluginManager().registerEvents(recipelistener, this);

        System.out.println(prefix + ChatColor.GREEN + "CustomRecipeAPI enabled!");
    }

    public static APIManager getManager() {
        return apimanager;
    }
}
