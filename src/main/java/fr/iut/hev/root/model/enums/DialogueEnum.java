package fr.iut.hev.root.model.enums;

public enum DialogueEnum {
    INTRO("Jhon, le temps m'est compté, tu le sais. J'ai confiance en toi. \n Je t'ai transmis au moins le nécessaire sur ce monde et ces immondices d'IA. \n N'oublie pas : Q pour aller à gauche, D pour aller à droite, et ESPACE pour sauter. Approche."),
    CRAFT("Parfait, tu te souviens : E pour l'inventaire et R pour fabriquer. \n Tu dois te tenir sur un atelier pour t'en servir."),
    LORE("Je me sens partir, des sombres images de contrôle de graphes au résultat désastreux... Bref.\n" +
            "Jhon, la guerre ne change jamais. Ce monde en est la conséquence :\n" +
            "d’absurdes massacres au nom d’idéaux toujours plus ignobles,\n" +
            "la science qui n’est jamais plus productive que lorsqu’elle est mise au service de la violence.\n" +
            "Et de là naissent ces ignobles machines. Elles commencent par donner du code exécrable, puis elles supplantent l’humanité.\n" +
            "Tout ça à cause de Marine."),
    DED("Vas, Jhon. Ne cesse jamais d’être libre face à ces monstres");
    private String text;

     DialogueEnum(String text) {
        this.text = text;
    }
     public String getTexte() {
        return text;
    }


}
