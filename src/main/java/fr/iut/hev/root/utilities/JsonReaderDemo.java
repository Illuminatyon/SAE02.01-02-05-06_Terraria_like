package fr.iut.hev.root.utilities;

import fr.iut.hev.root.utilities.Consummable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * La classe {@code JsonReaderDemo} sert de démonstration pour la lecture,
 * le filtrage et le tri d'un fichier JSON contenant des objets d'inventaire.
 *
 * <p>Elle lit un fichier JSON local, sélectionne les objets ayant un champ "heal",
 * les convertit en objets {@code Consommable}, les trie, et les affiche.
 *
 * <p>Exemple d'utilisation :
 * {@code JsonReaderDemo.main(new String[]{});}
 *
 * @author Fabio
 */
public class JsonReaderDemo {

    /**
     * Méthode principale.
     *
     * Lit le fichier JSON, extrait les objets consommables, les trie par "heal",
     * et affiche leurs informations dans la console.
     *
     * @param args non utilisé ici
     */
    public static void main(String[] args) {
        try {
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

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture ou du tri des items : " + e.getMessage());
        }
    }
}
