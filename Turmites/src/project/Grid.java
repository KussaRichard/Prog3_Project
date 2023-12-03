package project;

public class Grid {
    private int[][] matrix; // A rács
    private int width; // A rács szélessége
    private int height; // A rács magassága
    /// Konstruktor ///
    public Grid(int h, int w) {
        resize(w, h);
    }
    /// Cella értékének lekérdezése ///
    public int getValueAt(int x, int y) { return matrix[x][y]; }
    /// Cella értékének beállítása ///
    public void setValueAt(int x, int y, int value) { matrix[x][y] = value; }
    /// Rács méretezése ///
    public void resize(int w, int h) { // Új rácsot hoz létre, mert a méret átállításakor nem mindig van lehetőség az összes cella átmásolására
        width = w;
        height = h;
        matrix = new int[height][width];
    }
    /// Oszlopok számának lekérdezése ///
    public int getWidth() {
        return width;
    }
    /// Sorok számának lekérdezése ///
    public int getHeight() {
        return height;
    }
}