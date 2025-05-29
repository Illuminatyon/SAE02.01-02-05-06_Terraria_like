package fr.iut.hev.root.Sauvegarde;

import java.io.File;
import java.util.ArrayList;

public class SaveList {

    public static ArrayList<File> listJsonFiles(String directoryPath) {
        ArrayList<File> jsonFiles = new ArrayList<>();
        File directory = new File(directoryPath);


        if (directory.isDirectory()) {

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {

                    if (file.isFile() && file.getName().endsWith(".json")) {
                        jsonFiles.add(file);
                    }
                }
            }
        } else {
            System.err.println("Le chemin spécifié n'est pas un dossier valide.");
        }

        return jsonFiles;
    }
}
