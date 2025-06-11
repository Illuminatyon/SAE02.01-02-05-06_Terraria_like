package fr.iut.hev.root.model.enums;

public enum ToolStatsEnum {

    PIOCHE_BOIS(2, BlockTypesEnum.ROCK_TYPE);

    private int miningSpeed;
    private BlockTypesEnum efficientAgainst;

    ToolStatsEnum(int miningSpeed, BlockTypesEnum efficientAgainst) {
        this.miningSpeed = miningSpeed;
        this.efficientAgainst = efficientAgainst;
    }
}
