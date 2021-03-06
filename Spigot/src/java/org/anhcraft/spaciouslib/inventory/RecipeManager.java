package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage recipes
 */
public class RecipeManager {

    /**
     * Registers the given recipe
     * @param recipe the Recipe object
     */
    public static void register(Recipe recipe){
        Bukkit.getServer().addRecipe(recipe);
    }

    /**
     * Unregisters the given recipe
     * @param recipe the Recipe object
     */
    public static void unregister(Recipe recipe){
        try {
            Class craftingManagerClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".CraftingManager");
            Class recipeClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".IRecipe");
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", craftingManagerClass);
            Method nmsRecipesMethod = craftingManagerClass.getDeclaredMethod("getRecipes");
            List<Object> newNmsRecipes = new ArrayList<>();
            List<Object> nmsRecipes = (List<Object>) nmsRecipesMethod.invoke(craftingManager);
            for(Object nr : nmsRecipes){
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", recipeClass, nr);
                if(compare(recipeBukkit, recipe)){
                    continue;
                }
                newNmsRecipes.add(nr);
            }
            ReflectionUtils.setField("recipes", craftingManagerClass, craftingManager, newNmsRecipes);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks is the given recipe registered
     * @param recipe the Recipe object
     * @return true if yes
     */
    public static boolean isRegistered(Recipe recipe){
        try {
            Class<?> craftingManagerClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".CraftingManager");
            Class<?> recipeClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".IRecipe");
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", craftingManagerClass);
            List<Object> nmsRecipes = (List<Object>) ReflectionUtils.getMethod("getRecipes", craftingManagerClass, craftingManager);
            for(Object nr : nmsRecipes){
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", recipeClass, nr);
                if(compare(recipeBukkit, recipe)){
                    return true;
                }
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compares two recipes
     * @param recipeA the first recipe
     * @param recipeB the second recipe
     * @return true if these recipes are same
     */
    public static boolean compare(Recipe recipeA, Recipe recipeB){
        if(recipeA instanceof ShapedRecipe && recipeB instanceof ShapedRecipe) {
            ShapedRecipe a = (ShapedRecipe) recipeA;
            ShapedRecipe b = (ShapedRecipe) recipeB;
            if(InventoryUtils.compare(a.getResult(), b.getResult())){
                List<ItemStack> ai = new ArrayList<>();
                List<ItemStack> bi = new ArrayList<>();
                for(String as : a.getShape()){
                    for(char ac : as.toCharArray()){
                        ai.add(a.getIngredientMap().get(ac));
                    }
                }
                for(String bs : b.getShape()){
                    for(char bc : bs.toCharArray()){
                        bi.add(b.getIngredientMap().get(bc));
                    }
                }
                return InventoryUtils.compare(ai, bi);
            }
        }
        if(recipeA instanceof ShapelessRecipe && recipeB instanceof ShapelessRecipe){
            ShapelessRecipe a = (ShapelessRecipe) recipeA;
            ShapelessRecipe b = (ShapelessRecipe) recipeB;
            if(InventoryUtils.compare(a.getResult(), b.getResult())){
                return InventoryUtils.compare(a.getIngredientList(), b.getIngredientList());
            }
        }
        if(recipeA instanceof FurnaceRecipe && recipeB instanceof FurnaceRecipe){
            FurnaceRecipe a = (FurnaceRecipe) recipeA;
            FurnaceRecipe b = (FurnaceRecipe) recipeB;
            if(a.getExperience() == b.getExperience()
                    && InventoryUtils.compare(a.getInput(), b.getInput())
                    && InventoryUtils.compare(a.getResult(), b.getResult())){
                return true;
            }
        }
        return false;
    }
}