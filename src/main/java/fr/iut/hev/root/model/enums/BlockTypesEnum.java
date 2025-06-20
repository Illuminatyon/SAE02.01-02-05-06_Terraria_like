package fr.iut.hev.root.model.enums;

/**
 * Énumération des différents types de blocs dans le jeu.
 * Cette classification permet de regrouper les tuiles par matériau
 * ou par catégorie fonctionnelle.
 */
public enum BlockTypesEnum {
    /**
     * Représente les blocs de type sol, comme la terre et l'herbe.
     * Ces blocs sont généralement faciles à miner et constituent la surface du monde.
     */
    GROUND_TYPE,

    /**
     * Représente les blocs de type rocheux, comme la pierre et les minerais.
     * Ces blocs sont généralement plus résistants et se trouvent sous la surface.
     */
    ROCK_TYPE,

    /**
     * Représente les blocs de type bois, comme les arbres et les structures en bois.
     * Ces blocs sont inflammables et servent souvent de matériaux de construction.
     */
    WOOD_TYPE,

    /**
     * Représente les blocs d'arrière-plan personnalisés.
     * Ces blocs sont utilisés pour des éléments décoratifs ou spéciaux.
     */
    BACKGROUND_CUSTOM,

    /**
     * Représente les blocs non destructibles
     * Ces blocs sont utilisés pour faire des limitations
     */
    INDESTRUCTIBLE_TYPE
}
