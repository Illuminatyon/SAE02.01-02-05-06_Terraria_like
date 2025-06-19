package fr.iut.hev.root.model.enums;

public enum DialogueEnum {
    INTRO("Jhon, Le temps m'est compté tu le sais, J'ai confiance en toi. \n Je t'ai transmis au moins le necessaire sur ce monde et ces immondices synthetique. \n" +
            " N'oublie pas, Q pour aller a gauche D pour aller a droite et ESPACE pour sauter, Approche, prend ça"),
    CRAFT ("Parfait, tu te souviens : E pour l'inventaire et R pour fabriquer. \n Tu dois te tenir sur un atelier pour t'en servir. Molette pour changer d'objet et clique gauche pour t'en servir"),
    LORE("Je me sens partir, des sombres images de contrôle de graphes au résultat désastreux, bref.\n" +
            "John, la guerre ne change jamais. Ce monde en est la conséquence :\n" +
            "d’absurdes massacres au nom d’idéaux toujours plus ignobles,\n" +
            "la science qui n’est jamais plus productive que lorsqu’elle est mise au service de la violence.\n" +
            "Et de là naissent ces ignobles machines. Elle commence par donner du code exécrable, puis elle supplante l’humanité.\n" +
            "Tout ça à cause de Marine. "),
    DED("Vas Jhon, ne cesse jamais d'etre libre face a ces monstres \n et souviens toi, clique gauche pour taper  "),
    FIN("")
    ;
    private String text;

     DialogueEnum(String text) {
        this.text = text;
    }
     public String getTexte() {
        return text;
    }


}
