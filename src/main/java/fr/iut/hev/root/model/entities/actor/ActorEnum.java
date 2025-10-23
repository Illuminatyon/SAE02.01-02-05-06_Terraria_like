package fr.iut.hev.root.model.entities.actor;

public enum ActorEnum {
  ZOMBIE("evil and intimidating codsworth",32,54,2,15,3,5,1,1500,20),
    PLAYER("jhon_fallout",32,64,3,10,3,10),
    POULET("poulet",32,32,2,15,3,4),
    HOMPS("homps",32,64,3,10,3,10);



    private String name;
    private int maxHealth;
    private int width;
    private int height;
    private int moveSpeed;
    private int jumpForce;
    private int reach;
    private int damage;
    private int aggrodistance;
    private int attackcooldown;

    ActorEnum(String name, int width, int height, int moveSpeed, int jumpForce, int reach, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.width = width;
        this.height = height;
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.reach = reach;
    }
    ActorEnum(String name, int width, int height, int moveSpeed, int jumpForce, int reach, int maxHealth, int damage, int attackcooldown, int aggrodistance) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.width = width;
        this.height = height;
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.reach = reach;
        this.damage = damage;
        this.attackcooldown = attackcooldown;
        this.aggrodistance = aggrodistance;
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
    public int getMoveSpeed() {
        return moveSpeed;
    }
    public int getJumpForce() {
        return jumpForce;
    }
    public int getReach() {
        return reach;
    }

    public String getName() {
        return name;
    }
    public int getDamage() {
        return damage;
    }
    public int getAggrodistance() {
        return aggrodistance;
    }
    public int getAttackcooldown() {
        return attackcooldown;
    }
}
