package fr.iut.hev.root.model.enums;

public enum ItemStatsEnum {

    /**
     * This enum store the statistics of each item enum.
     * <p>
     *     They are respresented as a code :
     *     <br>
     *     [<b>item type</b> id (on one digit)][its <b>main stats</b> (on three digits)][its <b>cooldown</b> stat]
     *     <br>
     *     Each item type has only one main statistic except for tools :
     *     <ul>
     *         <li>
     *             <b>Tools</b> -> mining speed and the block type this tool is efficient against [id = 1]
     *         </li>
     *         <li>
     *             <b>Weapons</b> -> damage [id = 2]
     *         </li>
     *         <li>
     *             <b>Armor</b> -> absorbed damage [id = 3]
     *         </li>
     *         <li>
     *             <b>Consumable</b> -> restored health [id = 4]
     *         </li>
     *         <li>
     *             <b>Resources</b> -> only its cooldown [id = 5]
     *         </li>
     *     </ul>
     *     <br>
     *     Regarding tools, its main stat code is represented as :
     *     <br>
     *     [<b>mining speed</b> (two digits) and <b>efficient block against</b> id (one digit)]
     *     <br>
     *     Possible efficient block against ids :
     *     <ol>
     *         <li>
     *             {@link BlockTypesEnum#ROCK_TYPE}
     *         </li>
     *         <li>
     *             {@link BlockTypesEnum#GROUND_TYPE}
     *         </li>
     *         <li>
     *             {@link BlockTypesEnum#WOOD_TYPE}
     *         </li>
     *     </ol>
     * </p>
     * <br>
     * <h3>Exemple</h3>
     * <p>
     *     Stats code of {@link #RAW_CHICKEN} : 40031
     *     <ul>
     *         <li>
     *             <b>4</b> -> Item type consumable
     *         </li>
     *         <li>
     *             <b>003</b> -> 3hp of health restored at consumption
     *         </li>
     *         <li>
     *             <b>1</b> -> cooldown of its use
     *         </li>
     *     </ul>
     *     Stats code of {@link #WOODEN_PICKAXE} : 10211
     *     <ul>
     *         <li>
     *             <b>1</b> -> Item type tool
     *         </li>
     *         <li>
     *             <b>02</b> -> mining speed
     *         </li>
     *         <li>
     *             <b>1</b> -> efficient block against ({@link BlockTypesEnum#ROCK_TYPE} here)
     *         </li>
     *         <li>
     *             <b>1</b> -> cooldown of its use
     *         </li>
     *     </ul>
     * </p>
     *
     */

    RAW_CHICKEN(40031),
    WOODEN_PICKAXE(10211);

    private int statCode;

    ItemStatsEnum(int statCode) {
        this.statCode = statCode;
    }
    public int getItemMainStat() {
        if (statCode / 10000 != 1)
            return statCode % 10000 / 10;
        else
            return 0;
    }

    public int getCoolDownStat() {
        return statCode % 10;
    }

    public int getMiningSpeed() {
        if (statCode / 10000 == 1)
            return statCode % 10000 / 100;
        else
            return 0;
    }

    public BlockTypesEnum getEfficientBlockAgainst() {
        BlockTypesEnum returnedEnum;
        int enumIndex = statCode % 100 / 10;
        switch (enumIndex) {
            case 1 -> returnedEnum = BlockTypesEnum.ROCK_TYPE;
            case 2 -> returnedEnum = BlockTypesEnum.GROUND_TYPE;
            case 3 -> returnedEnum = BlockTypesEnum.WOOD_TYPE;
            default -> returnedEnum = null;
        }
        return returnedEnum;
    }
}
