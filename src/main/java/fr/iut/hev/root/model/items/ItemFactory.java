package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class ItemFactory {

    public ItemFactory() {}

    public Item createItem(ItemsEnum itemsEnum) {
        switch (itemsEnum.getItemType()) {
            case ItemTypesEnum.BLOCK -> {
                return createBlock(itemsEnum);
            }
            case ItemTypesEnum.RESOURCES -> {
                return createResource(itemsEnum);
            }
            case ItemTypesEnum.CONSUMABLE -> {
                return createConsumable(itemsEnum);
            }
            case ItemTypesEnum.TOOL -> {
                return createTool(itemsEnum);
            }
            case ItemTypesEnum.WEAPON -> {
                return createWeapon(itemsEnum);
            }
            case ItemTypesEnum.ARMOR_PIECE -> {
                return createArmorPiece(itemsEnum);
            }
            default -> {
                return null;
            }
        }
    }

    public Block createBlock(ItemsEnum itemsEnum) {
        return new Block(itemsEnum);
    }

    public Resource createResource(ItemsEnum itemsEnum) {
        return new Resource(itemsEnum);
    }

    public Consumable createConsumable(ItemsEnum itemsEnum) {
        return new Consumable(itemsEnum);
    }

    public Tool createTool(ItemsEnum itemsEnum) {
        return new Tool(itemsEnum);
    }

    public Weapon createWeapon(ItemsEnum itemsEnum) {
        return new Weapon(itemsEnum);
    }

    public ArmorPiece createArmorPiece(ItemsEnum itemsEnum) {
        return new ArmorPiece(itemsEnum);
    }
}
