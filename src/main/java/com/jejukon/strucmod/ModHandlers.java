package com.jejukon.strucmod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ModHandlers {
    public static String getStructorName(String new_string){
        String out_string = new_string.substring(12);
        out_string = ("ad_" + out_string);
        return out_string;
    }

    public static File(String path){
        File directory = new File("");
        directory = new File(directory.getAbsolutePath() + ("/kubejs/data/" + StrucMod.MOD_ID + "/additions/" + foldername));

        StrucMod.LOGGER.log(Level.WARN, "FileName: {}", directory.toString());

        if (!directory.isDirectory()){
            StrucMod.LOGGER.log(Level.ERROR, "Directory not found: {}", directory.toString());
        }
        else {
            File[] files = directory.listFiles(); //list all content in directory

            for (File file : files) {
                StrucMod.LOGGER.log(Level.ERROR, "File found 1: {}", file);
                if (file.isFile()) { //Checks if it's a file
                    if (file.getName().equals(kubejs_file_name + ".json")) { //Check if file name matches



                        try (FileReader fileReader = new FileReader(file)){
                            JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                            //Get the info
                            min_y = jsonObject.get("min_y").getAsInt();
                            max_y = jsonObject.get("max_y").getAsInt();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        StrucMod.LOGGER.log(Level.ERROR, "File found 2: {}", kubejs_file_name);
                    }
                }
            }
        }
    }
}
