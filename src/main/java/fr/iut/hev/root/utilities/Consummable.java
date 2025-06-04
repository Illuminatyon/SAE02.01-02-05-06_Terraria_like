package fr.iut.hev.root.utilities;

/*
 * Représente un objet consommable (ex : potion, nourriture...) avec un effet de soin.
 */
public class Consummable {
    private int id;
    private String name;
    private String type;
    private int heal;

    public Consummable(int id, String name, String type, int heal){
        this.id = id;
        // peut être rajouter un compteur pour les id ?
        this.name = name;
        this.type = type;
        this.heal = heal;
    }

    public int getId() { return id;}
    public String getName() { return name;}
    public String getType() { return type;}
    public int getHeal() { return heal;}

    @Override
    public String toString() {
        return "Consommable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", heal=" + heal +
                '}';
    }
}


