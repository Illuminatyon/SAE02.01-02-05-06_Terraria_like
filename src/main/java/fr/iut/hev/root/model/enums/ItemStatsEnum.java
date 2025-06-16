package fr.iut.hev.root.model.enums;

public enum ItemStatsEnum {

    /**
     * This enum store the statistics of each item enum.
     * <p>
     *     They are respresented as a code :
     *     <br>
     *     [<b>item type</b> id (one digit)][its <b>main stats</b> (three digits)][its <b>durability</b> (one digit)]
     *     <br>
     *     Each item type (except for <b>Resources</b> and <b>Blocks</b>) has one main statistic except for tools :
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
     *         <li>
     *             {@link BlockTypesEnum#BACKGROUND_CUSTOM}
     *         </li>
     *     </ol>
     *     <br>
     *     In addition, all kind of equipments have a <b>durability</b>. Items that do not need this stat have this digit initialized at 0. The real amount of durability of an item equals to its stat durability points power 4.
     * </p>
     * <br>
     * <h3>Exemple</h3>
     * <p>
     *     Stats code of {@link #RAW_CHICKEN} : 40030
     *     <ul>
     *         <li>
     *             <b>4</b> -> Item type consumable
     *         </li>
     *         <li>
     *             <b>003</b> -> 3hp of health restored at consumption
     *         </li>
     *         <li>
     *             <b>0</b> -> no durability since it's a <b>consumable</b>.
     *         </li>
     *     </ul>
     *     Stats code of {@link #WOODEN_PICKAXE} : 10213
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
     *             <b>3</b> -> 3 points of durability which means 81 of durability
     *         </li>
     *     </ul>
     * </p>
     * <br>
     * <p>
     *     In order to access to the stats of your item in general, use {@link #getItemMainStat()}. For durability, use {@link #getDurability()} instead and if your item is a tool, you shall use the {@link #getMiningSpeed()} and {@link #getEfficientBlockAgainst()} methods to get their stats since they're encoded differently.
     * </p>
     *
     */

    WOODEN_PICKAXE(10213),
    WOODEN_HAX(10233),
    WOODEN_SHOVEL(10223),

    HAMMER(10443),

    DAGGER(20033),

    IRON_HELMET(30013),
    IRON_CHESTPLATE(30013),
    IRON_LEGGINGS(30013),

    RAW_CHICKEN(40030);

    private int statCode;

    ItemStatsEnum(int statCode) {
        this.statCode = statCode;
    }

    public int getItemMainStat() {
        if (statCode / 10000 != 1) {
            return statCode % 10000;
        }else {
            return 0;
        }
    }

    public int getMiningSpeed() {
        if (statCode / 10000 == 1)
            return 2 * (statCode % 10000 / 100);
        else
            return 0;
    }

    public int getDurability() {
        if (statCode / 10000 < 4)
            return (int) Math.pow(statCode % 10,4);
        else
            return 0;
    }

    public boolean isEquipment() {return statCode / 10000 < 4;}
    public int getItemTypeFromStat() {return statCode / 10000;}

    public BlockTypesEnum getEfficientBlockAgainst() {
        BlockTypesEnum returnedEnum;
        int enumIndex = statCode % 100 / 10;
        switch (enumIndex) {
            case 1 -> returnedEnum = BlockTypesEnum.ROCK_TYPE;
            case 2 -> returnedEnum = BlockTypesEnum.GROUND_TYPE;
            case 3 -> returnedEnum = BlockTypesEnum.WOOD_TYPE;
            case 4 -> returnedEnum = BlockTypesEnum.BACKGROUND_CUSTOM;
            default -> returnedEnum = null;
        }
        return returnedEnum;
    }


}
