package fr.iut.hev.root.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
/*
Classe permettant de comprendre le fonctionnement de jsonReader
 */
public class jsonReader {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = jsonReader.class.getClassLoader().getResourceAsStream("data.json")) {
            if (input == null) {
                System.err.println("❌ Le fichier data.json est introuvable dans resources !");
                return;
            }

            List<Personne> personnes = mapper.readValue(input, new TypeReference<>() {});
            personnes.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
