package fr.iut.hev.root.Sauvegarde;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;

public class SaveReader {

    public static int[][] map(String filePath) throws IOException {


        FileReader reader = new FileReader(filePath);
        StringBuilder jsonContent = new StringBuilder();
        int i;
        while ((i = reader.read()) != -1) {
            jsonContent.append((char) i);
        }
        reader.close();


        JSONObject jsonObject = new JSONObject(jsonContent.toString());


        JSONArray tilesArray = jsonObject.getJSONArray("map");
        int[][] map = new int[tilesArray.length()][];


        for (int row = 0; row < tilesArray.length(); row++) {
            JSONArray rowArray = tilesArray.getJSONArray(row);
            map[row] = new int[rowArray.length()];

            for (int col = 0; col < rowArray.length(); col++) {
                map[row][col] = rowArray.getInt(col);
            }
        }


        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                System.out.print(map[row][col] + " ");
            }
            System.out.println();
        }

    return map;
    }

    public static int[][] newgame() throws IOException {
        int[][] map = SaveReader.map("fr/iut/hev/root/data/spawn.json");
        return map;


    }
}