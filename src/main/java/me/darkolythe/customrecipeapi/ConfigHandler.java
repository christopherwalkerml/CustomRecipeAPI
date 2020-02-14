package me.darkolythe.customrecipeapi;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private CustomRecipeAPI plugin = CustomRecipeAPI.getInstance();

    private FileConfiguration recipeDataConfig;
    private File recipeData;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        recipeData = new File(plugin.getDataFolder(), "RecipeData.yml");

        if (!recipeData.exists()) {
            try {
                recipeData.createNewFile();
                System.out.println(CustomRecipeAPI.prefix + ChatColor.GREEN + "RecipeData.yml has been created");
            } catch (IOException e) {
                System.out.println(CustomRecipeAPI.prefix + ChatColor.RED + "Could not create RecipeData.yml");
            }
        }
        recipeDataConfig = YamlConfiguration.loadConfiguration(recipeData);
    }

    public void loadRecipes() {
        if (recipeDataConfig.contains("recipes")) {
            for (String recipes : recipeDataConfig.getConfigurationSection("recipes").getKeys(false)) {
                ItemStack result = recipeDataConfig.getItemStack("recipes." + recipes + ".result");
                ItemStack recipe[] = (recipeDataConfig.getList("recipes." + recipes + ".recipe")).toArray(new ItemStack[9]);
                CustomRecipe r = new CustomRecipe(result, recipe);
                r.setForced(recipeDataConfig.getBoolean("recipes." + recipes + ".forced"));
                r.setID(recipes);
                CustomRecipeAPI.getManager().addRecipe(r);
            }
        }
    }

    public void saveRecipe(CustomRecipe recipe) {
        recipeDataConfig = YamlConfiguration.loadConfiguration(recipeData);

        String path = "recipes." + recipe.getID();
        recipeDataConfig.set(path + ".result", recipe.getResult());
        recipeDataConfig.set(path + ".recipe", recipe.getRecipe());
        recipeDataConfig.set(path + ".forced", recipe.getForced());
        try {
            recipeDataConfig.save(recipeData);
        } catch (IOException e) {
            System.out.println(CustomRecipeAPI.prefix + ChatColor.RED + "Could not save recipes");
        }
    }

    public void unsaveRecipe(CustomRecipe recipe) {
        recipeDataConfig.set("recipes." + recipe.getID(), null);
        try {
            recipeDataConfig.save(recipeData);
        } catch (IOException e) {
            System.out.println(CustomRecipeAPI.prefix + ChatColor.RED + "Could not save recipes");
        }
    }
}
