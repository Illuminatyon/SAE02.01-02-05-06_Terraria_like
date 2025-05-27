package fr.iut.hev.root.utilities;

import fr.iut.hev.root.model.TileMap;
import org.json.JSONArray;
import org.json.JSONObject;
import fr.iut.hev.root.model.TileMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveWritter {

    public static void save(TileMap map, String savename) throws IOException {
        String message;
        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();
                for (int i=0; i< map.getWidth();i++){
                    for (int j=0; j< map.getHeight(); j++){
                        array.put(map.getTile(i, j));
                    }
                }
        message = array.toString();
        System.out.println(message);
        FileWriter game = new FileWriter("src/main/resources/fr/iut/hev/root/data/"+savename+".json");
        game.write(message);
        game.close();

    }

}

