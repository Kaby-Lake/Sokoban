package Business;


/**
 * Game object which emun though different GameObject located in GameGrid
 *
 */
public enum GameObject {
    /**
     * Wall (cannot walk through)
     */
    WALL('W'),

    /**
     * Floor (walkable space)
     */
    FLOOR(' '),

    /**
     * Box to be moved
     */
    CRATE('C'),

    /**
     * Destination and Target of the Box
     */
    DIAMOND('D'),

    /**
     * User that can play
     */
    KEEPER('S'),

    /**
     * Space where Box has already been moved onto Destination
     */
    CRATE_ON_DIAMOND('O'),

    DEBUG_OBJECT('=');

    public final char symbol;
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * Generate the GameObject by given char, with each object by different char
     * @param c
     * @return the generated GameObject
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }
        return WALL;
    }

    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    public char getCharSymbol() {
        return symbol;
    }
}