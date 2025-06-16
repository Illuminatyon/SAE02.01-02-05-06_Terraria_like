package fr.iut.hev.root.model.enums;

public enum DialogueEnum {
    INTRO("Oui bonjour"),
    LORE ("feur");
    private String text;

     DialogueEnum(String text) {
        this.text = text;
    }
     public String getTexte() {
        return text;
    }


}
