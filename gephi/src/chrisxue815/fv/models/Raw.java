package chrisxue815.fv.models;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Raw {

  public HashMap<String, String> icons;
  
  public HashMap<String, Recipe> recipes;
  
  public static class Deserializer implements JsonDeserializer<Raw> {

    @Override
    public Raw deserialize(JsonElement json, Type typeOfT,
        JsonDeserializationContext context) throws JsonParseException {
      Raw raw = new Raw();
      raw.icons = new HashMap<String, String>();
      raw.recipes = new HashMap<String, Recipe>();
      JsonObject rawObj = json.getAsJsonObject();
      
      for (Map.Entry<String, JsonElement> type : rawObj.entrySet()) {
        if (type.getKey().equals("recipe")) {
          JsonObject recipesObj = type.getValue().getAsJsonObject();
          for (Map.Entry<String, JsonElement> recipeEle : recipesObj.entrySet()) {
            Recipe recipe = context.deserialize(recipeEle.getValue(), Recipe.class);
            raw.recipes.put(recipeEle.getKey(), recipe);
          }
        }
        else {
          JsonObject typeObj = type.getValue().getAsJsonObject();
          for (Map.Entry<String, JsonElement> entity : typeObj.entrySet()) {
            JsonObject entityObj = entity.getValue().getAsJsonObject();
            if (entityObj.has("icon")) {
              String name = entity.getKey();
              String iconPath = entityObj.get("icon").getAsString();
              iconPath = iconPath.replaceFirst("__core__", "/data/core");
              iconPath = iconPath.replaceFirst("__base__", "/data/base");
              
              if (name != null && iconPath != null) {
                raw.icons.put(name, iconPath);
              }
            }
          }
        }
      }
      
      return raw;
    }
  }
}
