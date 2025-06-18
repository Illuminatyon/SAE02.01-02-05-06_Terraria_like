package fr.iut.hev.root.utils;

import fr.iut.hev.root.model.World;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SaveManager {
    public static List<World> loadAllWorlds() {
        List<World> worlds = new ArrayList<>();
        File savesFolder = new File("./saves/");
        if (!savesFolder.exists() || !savesFolder.isDirectory()) return worlds;

        for (File worldFolder : Objects.requireNonNull(savesFolder.listFiles())) {
            if (worldFolder.isDirectory()) {
                File worldJson = new File(worldFolder, "world.json");
                if (worldJson.exists()) {
                    try {
                        World world = JsonManager.readJson(worldJson.getPath(), World.class);
                        worlds.add(world);
                    } catch (Exception e) {
                        System.err.println("Failed to load world: " + worldFolder.getName());
                    }
                }
            }
        }

        worlds.sort(Comparator.comparingLong(World::getLastPlayed).reversed());
        return worlds;
    }

    public static void saveWorld(World world) throws IOException {
        String basePath = "./saves/" + world.getName();

        File saveFolder = new File(basePath);
        if (!saveFolder.exists()) {
            boolean created = saveFolder.mkdirs();
            if (!created) {
                throw new IOException("Failed to create save directory: " + basePath);
            }
        }

        // Sauvegarde des données
        JsonManager.writeJson(basePath + "/world.json", world);
        JsonManager.writeJson(basePath + "/map.json", world.getTileMap());
        JsonManager.writeJson(basePath + "/player.json", world.getPlayer());
        JsonManager.writeJson(basePath + "/entities.json", world.getAliveActors());
    }

    public static boolean deleteWorldFolder(String worldName) {
        File worldFolder = new File("./saves/" + worldName);
        return deleteDirectoryRecursively(worldFolder);
    }

    private static boolean deleteDirectoryRecursively(File dir) {
        if (!dir.exists()) return false;
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryRecursively(file);
            }
        }
        return dir.delete();
    }
}
