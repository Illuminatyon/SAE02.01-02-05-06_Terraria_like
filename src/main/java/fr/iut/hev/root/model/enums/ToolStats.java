package fr.iut.hev.root.model.enums;

public enum ToolStats {

    PIOCHE_BOIS(2,BlockType.ROCK_TYPE);

    private int miningSpeed;
    private BlockType efficientAgainst;

    ToolStats(int miningSpeed,BlockType efficientAgainst) {
        this.miningSpeed = miningSpeed;
        this.efficientAgainst = efficientAgainst;
    }
}
