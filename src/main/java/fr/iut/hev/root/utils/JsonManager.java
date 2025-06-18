package fr.iut.hev.root.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.iut.hev.root.model.Actor;
import fr.iut.hev.root.model.Player;
import org.hildan.fxgson.FxGsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class JsonManager {
    private static final Gson gson = new FxGsonBuilder().builder()
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .create();

    public static <T> T readJson(String path, Class<T> classOfT) throws IOException {
        FileReader reader = new FileReader(path);
        return gson.fromJson(reader, classOfT);
    }

    public static <T> ArrayList<T> readJsonList(String filePath, Class<T> clazz) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
            return gson.fromJson(reader, listType);
        }
    }

    public static void writeJson(String pathStr, Object object) throws IOException {
        Path path = Paths.get(pathStr);
        Path parent = path.getParent();

        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        // Crée le fichier s'il n'existe pas (et l'écrase si déjà là)
        try (var writer = Files.newBufferedWriter(path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            gson.toJson(object, writer);
        }
    }
}
