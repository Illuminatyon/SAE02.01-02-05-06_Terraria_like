package fr.iut.hev.root.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * La classe {@code JsonReaderDemo} sert de démonstration pour la lecture,
 * le filtrage et le tri d'un fichier JSON contenant des objets d'inventaire.
 *
 * <p>Cette classe lit un fichier JSON local contenant une liste d'objets (items),
 * sélectionne ceux qui possèdent une propriété "heal", puis les trie par valeur
 * croissante de soin. Enfin, elle affiche les informations de ces objets triés
 * dans la console.
 *
 * <p>Le fichier JSON doit être un tableau contenant des objets ayant au minimum
 * les champs suivants pour être sélectionnés :
 * <ul>
 *     <li><b>id</b> : identifiant numérique de l'objet</li>
 *     <li><b>name</b> : nom de l'objet</li>
 *     <li><b>type</b> : type de l'objet</li>
 *     <li><b>heal</b> : quantité de soin que l'objet procure</li>
 * </ul>
 *
 * <p>Exemple d'utilisation :
 * <pre>
 * {@code
 * JsonReaderDemo.main(new String[]{});
 * }
 * </pre>
 *
 * @author Fabio
 **/
public class JsonReaderDemo {

    /**
     * Méthode principale.
     *
     * Elle lit le fichier JSON, récupère les objets qui ont un champ "heal",
     * les trie par valeur de soin, puis affiche leurs informations.
     *
     * @param args non utilisé ici
     */

    public static void main(String[] args) {

        try {
            String filePath = "C:\\Users\\fabio\\OneDrive\\Bureau\\SAE02.01-02-05-06_Terraria_like\\src\\main\\resources\\fr\\iut\\hev\\root\\data\\items.json";
            JSONArray items = JsonReader.readJsonArrayFromFile(filePath);

            // Filtrer les items avec un champ "heal"
            List<JSONObject> healingItems = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("heal")) {
                    healingItems.add(item);
                }
            }

            // Trier les objets selon la valeur de "heal" (ordre croissant)
            healingItems.sort(Comparator.comparingInt(o -> o.getInt("heal")));

            // Affichage
            System.out.println("=== Items triés par soin (ordre croissant) ===");
            for (JSONObject item : healingItems) {
                System.out.println("ID: " + item.getInt("id"));
                System.out.println("Name: " + item.getString("name"));
                System.out.println("Type: " + item.getString("type"));
                System.out.println("Heal: " + item.getInt("heal"));
                System.out.println("-----");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture ou du tri des items : " + e.getMessage());
        }
    }
}
