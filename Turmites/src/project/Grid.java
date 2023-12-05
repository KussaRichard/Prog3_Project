package project;

/**
 * A rács osztálya.
 */
public class Grid {
    private int[][] matrix; // A rács
    private int width; // A rács szélessége
    private int height; // A rács magassága

    /**
     * A rács konstruktora.
     * @param w Az új rács szélessége (oszlopok száma).
     * @param h Az új rács magassága (sorok száma).
     */
    public Grid(int h, int w) {
        resize(w, h);
    }

    /**
     * Visszaadja a megadott cella értékét.
     * @param x Cella x koordinátája.
     * @param y Cella y koordinátája.
     * @return A cella értéke.
     */
    public int getValueAt(int x, int y) { return matrix[x][y]; }

    /**
     * Beállítja a megadott cella értékét.
     * @param x Cella x koordinátája.
     * @param y Cella y koordinátája.
     * @param value A beállítani kívánt érték.
     */
    public void setValueAt(int x, int y, int value) { matrix[x][y] = value; }

    /**
     * Létrehoz egy új rácsot a megadott paraméterekkel.
     * @param w Az új rács szélessége (oszlopok száma).
     * @param h Az új rács magassága (sorok száma).
     */
    public void resize(int w, int h) { // Új rácsot hoz létre, mert a méret átállításakor nem mindig van lehetőség az összes cella átmásolására
        width = w;
        height = h;
        matrix = new int[height][width];
    }

    /**
     * Visszaadja a rács szélességét.
     * @return Magasság int értéke (oszlopok száma).
     */
    public int getWidth() {
        return width;
    }

    /**
     * Visszaadja a rács magasságát.
     * @return Magasság int értéke (sorok száma).
     */
    public int getHeight() {
        return height;
    }
}