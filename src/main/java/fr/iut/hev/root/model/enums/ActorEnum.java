package fr.iut.hev.root.model.enums;

public enum ActorEnum {
    PLAYER("jhon_fallout","Player",10),
    ZOMBIE("evil and intimidating codsworth","mob",5),
    POULET("poulet","mob",4),
    HOMPS("homps","pnj",10),
    ARROW("arrow","projectile",1),
    MARINE("marine","mob",100);

    // Corriger toute la classe

    private String name;
    private String Type; // a retirer
    private int maxHealth;

    ActorEnum(String name, String type, int maxHealth) {
        this.name = name;
        Type = type;
        this.maxHealth = maxHealth;
    } // a modifier tout ça

    public String getName() {
        return name;
    }
}
