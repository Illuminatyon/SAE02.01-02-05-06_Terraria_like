package fr.iut.hev.root.utilities;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Classe utilitaire pour lire des fichiers JSON.
 *
 * Fournit deux méthodes statiques pour lire :
 * - un tableau JSON (JSONArray)
 * - un objet JSON (JSONObject)
 *
 * Le contenu est lu depuis un fichier à partir de son chemin.
 */

public class JsonReader {

    /**
     * Lit un fichier JSON contenant un tableau (JSONArray).
     *
     * @param filePath le chemin du fichier JSON
     * @return un JSONArray représentant le contenu du fichier
     * @throws IOException si une erreur de lecture survient
     */

    public static JSONArray readJsonArrayFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONArray(content);
    }

    public static JSONObject readJsonObjectFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(content);
    }
}