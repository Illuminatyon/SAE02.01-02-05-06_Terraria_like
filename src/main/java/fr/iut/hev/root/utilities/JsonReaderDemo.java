package fr.iut.hev.root.utilities;

import fr.iut.hev.root.utilities.Consummable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JsonReaderDemo {

    public static void main(String[] args) throws IOException {

            // Utilisation d'un chemin relatif
            String filePath = "src/main/resources/fr/iut/hev/root/data/items.json";
            JSONArray items = JsonReader.readJsonArrayFromFile(filePath);

            // Création d'une liste de consommables à partir des objets JSON
            List<Consummable> healingItems = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("heal")) {
                    Consummable consommable = ConsommableFactory.fromJson(item);
                    healingItems.add(consommable);
                }
            }

            // Tri des consommables par valeur de soin
            healingItems.sort(Comparator.comparingInt(Consummable::getHeal));

            // Affichage
            System.out.println("=== Consommables triés par soin (ordre croissant) ===");
            for (Consummable c : healingItems) {
                System.out.println(c);
            }

        }
    }

