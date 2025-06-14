package fr.iut.hev.root.model.enums;

public enum DialogueEnum {
    INTRO("Oui bonjour");

    private String text;

     DialogueEnum(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

}
