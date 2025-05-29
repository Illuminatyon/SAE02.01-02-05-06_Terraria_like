package fr.iut.hev.root.Sauvegarde;

import fr.iut.hev.root.model.TileMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveWritter {

    public static void save(TileMap map, String savename) throws IOException {
        String message;
        savename = savename+".json";
        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();
                for (int i=0; i< map.getWidth();i++){
                    for (int j=0; j< map.getHeight(); j++){
                        array.put(map.getTile(i, j));
                    }
                }
        message = array.toString();
        ArrayList<File> save = SaveList.listJsonFiles("src/main/resources/fr/iut/hev/root/data");
        for (int i=0; i< save.size(); i++){
            if (savename == String.valueOf(save.get(i))){
                save.get(i).delete();
            }
        }
        FileWriter game = new FileWriter("src/main/resources/fr/iut/hev/root/data/"+savename);
        game.write(message);
        game.close();

    }

}

