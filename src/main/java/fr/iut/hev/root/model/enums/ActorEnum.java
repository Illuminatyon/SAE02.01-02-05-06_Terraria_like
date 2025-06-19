package fr.iut.hev.root.model.enums;

public enum ActorEnum {
    PLAYER("jhon_fallout","Player",10 ,32,64,10,2,3),
    ZOMBIE("evil and intimidating codsworth","mob",5,40,54,10,1,3),
    POULET("poulet","mob",4,32,32,10,2,3),
    HOMPS("homps","pnj",10,32,64,10,1,3),
    ARROW("arrow","projectile",1);

    private String name;
    private String Type;
    private int maxHealth;
    private int width;
    private int height;
    private int jumpForce;
    private int speed;
    private int reach;

    ActorEnum(String name, String type, int maxHealth) {
        this.name = name;
        Type = type;
        this.maxHealth = maxHealth;
    }
    ActorEnum(String name, String type, int maxHealth, int widht, int height, int jumpForce, int speed, int reach) {
        this.name = name;
        Type = type;
        this.maxHealth = maxHealth;
        this.width = widht;
        this.height = height;
        this.jumpForce = jumpForce;
        this.speed = speed;
        this.reach = reach;
    }

    public String getName() {
        return name;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getJumpForce() {
        return jumpForce;
    }
    public int getSpeed() {
        return speed;
    }
    public int getReach() {
        return reach;
    }
}
