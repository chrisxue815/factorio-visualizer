package chrisxue815.fv;

import java.io.FileNotFoundException;
import java.io.FileReader;

import chrisxue815.fv.models.Prototype;
import chrisxue815.fv.models.Raw;
import chrisxue815.fv.models.Recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Program {
  
  public static void main(String[] args) throws FileNotFoundException {
    Prototype prototype = loadPrototype();
    
    GraphGenerator.exec(prototype);
  }
  
  private static Prototype loadPrototype() throws FileNotFoundException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Raw.class, new Raw.Deserializer());
    gsonBuilder.registerTypeAdapter(Recipe.class, new Recipe.Deserializer());
    
    Gson gson = gsonBuilder.create();
    
    FileReader reader = new FileReader("bin/prototypes.json");
    
    return gson.fromJson(reader, Prototype.class);
  }

}
