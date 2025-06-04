package fr.iut.hev.root.items;

import org.json.JSONObject;

public class ItemFactory {
    public static Item fromJson(JSONObject json) {
        String id = json.getString("id");
        String name = json.getString("name");
        String type = json.getString("type");
        return new Item(id, name, type);
    }
}
