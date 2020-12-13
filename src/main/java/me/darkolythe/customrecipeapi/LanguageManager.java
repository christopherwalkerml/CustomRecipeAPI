package me.darkolythe.customrecipeapi;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static Map<String, String> translateMap = new HashMap<>();

    public static void setup(CustomRecipeAPI main) {
        translateMap.put("workbenchtitle", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("workbenchtitle")));
        translateMap.put("recipebook", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("recipebook")));
        translateMap.put("nextpage", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("nextpage")));
        translateMap.put("previouspage", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("previouspage")));
        translateMap.put("customrecipeview", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("customrecipeview")));
        translateMap.put("customrecipecreator", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("customrecipecreator")));
        translateMap.put("fullinv", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("fullinv")));
        translateMap.put("invalidrecipe", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("invalidrecipe")));
        translateMap.put("createrecipe", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("createrecipe")));
        translateMap.put("toggleshapeless", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("toggleshapeless")));
        translateMap.put("shaped", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("shaped")));
        translateMap.put("leftclicktoview", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("leftclicktoview")));
        translateMap.put("rightclicktogive", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("rightclicktogive")));
        translateMap.put("permission", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("permission")));
        translateMap.put("backtomenu", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("backtomenu")));
        translateMap.put("deleterecipe", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("deleterecipe")));
        translateMap.put("cannotbeundone", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("cannotbeundone")));
        translateMap.put("recipeview", ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("recipeview")));
    }

    public static String getValue(String key) {
        if (translateMap.containsKey(key)) {
            return translateMap.get(key);
        } else {
            return "[Invalid Translate Key]";
        }
    }
}