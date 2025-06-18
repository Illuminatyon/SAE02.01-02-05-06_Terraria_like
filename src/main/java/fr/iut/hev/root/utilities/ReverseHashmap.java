package fr.iut.hev.root.utilities;

import fr.iut.hev.root.model.enums.Tiles;
import org.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ReverseHashmap {

    public static HashMap HashMapReader() throws IOException {


        String filePath = "src/main/resources/fr/iut/hev/root/data/reversesave.json";

        FileReader reader = new FileReader(filePath);
        StringBuilder jsonContent = new StringBuilder();
        int i;
        while ((i = reader.read()) != -1) {
            jsonContent.append((char) i);
        }
        reader.close();


        JSONObject jsonObject = new JSONObject(jsonContent.toString());


        HashMap<Tiles, Integer> tileMap = new HashMap<>();


        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String Stringkey = (key);
            int value = Integer.parseInt(key);

            Tiles tile = Tiles.valueOf(Stringkey);


            tileMap.put(tile, value);
        }


        return tileMap;
    }
}

