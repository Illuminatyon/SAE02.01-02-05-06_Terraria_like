package fr.iut.hev.root.utilities;

import fr.iut.hev.root.model.enums.Tiles;
import org.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class CreateHashmap {

    public static HashMap HashMapReader() throws IOException {


            String filePath = "src/main/resources/fr/iut/hev/root/data/MapSave.json";

            FileReader reader = new FileReader(filePath);
            StringBuilder jsonContent = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                jsonContent.append((char) i);
            }
            reader.close();


            JSONObject jsonObject = new JSONObject(jsonContent.toString());


            HashMap<Integer, Tiles> tileMap = new HashMap<>();


            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int intKey = Integer.parseInt(key);
                String value = jsonObject.getString(key);

                Tiles tile = Tiles.valueOf(value);


                tileMap.put(intKey, tile);
            }


            return tileMap;
        }
    }

