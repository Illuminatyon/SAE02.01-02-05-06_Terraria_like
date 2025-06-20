package fr.iut.hev.root.model.enums;

public enum ActorEnum {
    PLAYER("jhon_fallout","Player",10),
    ZOMBIE("evil and intimidating codsworth","mob",5),
    POULET("poulet","mob",4),
    HOMPS("homps","pnj",10),
    ARROW("arrow","projectile",1);

    private String name;
    private String Type;
    private int maxHealth;

    ActorEnum(String name, String type, int maxHealth) {
        this.name = name;
        Type = type;
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }
}
