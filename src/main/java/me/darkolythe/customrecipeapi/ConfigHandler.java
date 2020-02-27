package me.darkolythe.customrecipeapi;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.darkolythe.customrecipeapi.WorkbenchManager.recipeList;
import static me.darkolythe.customrecipeapi.WorkbenchManager.setRecipe;

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

    public void saveWorkbench() {
        recipeDataConfig = YamlConfiguration.loadConfiguration(recipeData);

        recipeDataConfig.set("workbench.result", APIManager.getWorkbench().getResult());
        recipeDataConfig.set("workbench.recipe", recipeList());
        try {
            recipeDataConfig.save(recipeData);
        } catch (IOException e) {
            System.out.println(CustomRecipeAPI.prefix + ChatColor.RED + "Could not save workbench recipe");
        }
    }

    public void loadWorkbench() {
        if (recipeDataConfig.contains("workbench")) {
            ItemStack result = recipeDataConfig.getItemStack("workbench.result");
            ItemStack recipe[] = (recipeDataConfig.getList("workbench.recipe")).toArray(new ItemStack[9]);

            Map<Character, ItemStack> map = new HashMap<>();
            List<Character> order = new ArrayList<>();
            char c = 'A';
            for (ItemStack i : recipe) {
                if (!map.containsValue(i)) {
                    map.put((char) ((int) c + 1), i);
                    c++;
                }
                for (char chr : map.keySet()) {
                    if (map.get(chr).equals(i)) {
                        order.add(chr);
                        break;
                    }
                }
            }
            setRecipe(order, map);
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
