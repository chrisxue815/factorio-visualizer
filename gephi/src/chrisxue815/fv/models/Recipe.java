package chrisxue815.fv.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Recipe {

  public String name;
  public float energy_required;
  public List<Item> ingredients;
  public List<Item> results;

  public static class Deserializer implements JsonDeserializer<Recipe> {

    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT,
        JsonDeserializationContext context) throws JsonParseException {
      JsonObject obj = json.getAsJsonObject();

      Recipe recipe = new Recipe();
      recipe.name = obj.get("name").getAsString();
      recipe.ingredients = deserializeIngredients(obj.get("ingredients"));
      recipe.results = deserializeResults(obj);
      
      if (obj.has("energy_required")) {
        recipe.energy_required = obj.get("energy_required").getAsFloat();
      }
      else {
        recipe.energy_required = 0.5f;
      }

      return recipe;
    }

    private List<Item> deserializeIngredients(JsonElement json) {
      JsonArray ingredientArray = json.getAsJsonArray();
      
      List<Item> ingredients = new ArrayList<Item>(ingredientArray.size());
      
      for (JsonElement ele : ingredientArray) {
        Item ingredient = new Item();

        if (ele.isJsonArray()) {
          JsonArray array = ele.getAsJsonArray();
          ingredient.name = array.get(0).getAsString();
          ingredient.count = array.get(1).getAsFloat();
        } else {
          JsonObject obj = ele.getAsJsonObject();
          ingredient.name = obj.get("name").getAsString();
          ingredient.count = obj.get("amount").getAsFloat();
        }
        
        ingredients.add(ingredient);
      }

      return ingredients;
    }

    private List<Item> deserializeResults(JsonObject obj) {
      List<Item> results;
      
      if (obj.has("result")) {
        Item result = new Item();
        result.name = obj.get("result").getAsString();
        
        if (obj.has("result_count")) {
          result.count = obj.get("result_count").getAsFloat();
        }
        else {
          result.count = 1;
        }
        
        results = new ArrayList<Item>(1);
        results.add(result);
      }
      else {
        JsonArray array = obj.get("results").getAsJsonArray();
        results = new ArrayList<Item>(array.size());
        
        for (JsonElement ele : array) {
          JsonObject resultObj = ele.getAsJsonObject();
          Item result = new Item();
          result.name = resultObj.get("name").getAsString();
          result.count = resultObj.get("amount").getAsFloat();
          
          results.add(result);
        }
      }
      
      return results;
    }
  }
}
