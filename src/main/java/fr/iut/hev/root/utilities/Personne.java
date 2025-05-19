package fr.iut.hev.root.utilities;

public class Personne {
    private String nom;
    private int age;
    private boolean etudiant;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public boolean isEtudiant() { return etudiant; }
    public void setEtudiant(boolean etudiant) { this.etudiant = etudiant; }

    @Override
    public String toString() {
        return "Personne{" +
                "nom='" + nom + '\'' +
                ", age=" + age +
                ", etudiant=" + etudiant +
                '}';
    }
}

