package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

public class ItemFactory {

    private HitboxManager hitboxManager;

    public ItemFactory(HitboxManager hitboxManager) {
        this.hitboxManager = hitboxManager;
    }

    public Item createItem(ItemsEnum itemsEnum) {
        switch (itemsEnum.getItemType()) {
            case ItemTypesEnum.BLOCK -> {
                return createBlock(itemsEnum);
            }
            case ItemTypesEnum.RESOURCES -> {
                return createResource(itemsEnum);
            }
            case ItemTypesEnum.UTILITY -> {
                return createUtility(itemsEnum);
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

    public Utility createUtility(ItemsEnum itemsEnum) {
        return new Utility(itemsEnum,hitboxManager);
    }

    public Consumable createConsumable(ItemsEnum itemsEnum) {
        return new Consumable(itemsEnum);
    }

    public Tool createTool(ItemsEnum itemsEnum) {
        return new Tool(itemsEnum);
    }

    public Weapon createWeapon(ItemsEnum itemsEnum) {
        /*switch (itemsEnum) {
            case ItemsEnum.DAGGER -> {
                return new Dagger(itemsEnum,hitboxManager);
            }
            default -> {
            return null;}
        }*/
        return  new Weapon(itemsEnum,hitboxManager);
    }

    public ArmorPiece createArmorPiece(ItemsEnum itemsEnum) {
        return new ArmorPiece(itemsEnum);
    }
}
