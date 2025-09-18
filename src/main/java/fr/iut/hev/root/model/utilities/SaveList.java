package fr.iut.hev.root.model.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveList { // Encore une class pour la save on peut aussi delete

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
        }

        return jsonFiles;
    }
}
