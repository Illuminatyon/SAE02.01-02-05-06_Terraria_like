package fr.iut.hev.root.utilities;

import fr.iut.hev.root.utilities.Consummable;
import org.json.JSONObject;

/**
 * Classe utilitaire pour créer un objet Consommable à partir d'un JSONObject.
 */
public class ConsommableFactory {
    /**
     * Crée un Consommable à partir d’un JSONObject.
     *
     * @param json l’objet JSON contenant les données
     * @return un objet Consommable
     */
    public static Consummable fromJson(JSONObject json) {
        int id = json.getInt("id");
        String name = json.getString("name");
        String type = json.getString("type");
        int heal = json.getInt("heal");

        return new Consummable(id, name, type, heal);
    }
}
