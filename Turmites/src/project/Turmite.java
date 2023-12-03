package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Turmite extends Thread {
    private Grid grid; // A rács
    private Display display; // A megjelenítő
    private List<List<String>> stateMachine; // Az állapotgép
    private String state; // Az aktuális állapot
    private int direction; // A hangya iránya, amerre néz, csak négy értéket vehet fel: {0 = É, 1 = K, 2 = D, 3 = NY}
    private int x; // A hangya x koordinátája a rácson
    private int y; // A hangya y koordinátája a rácson
    private int speed; // A hangya sebessége
    private boolean terminated; // Igaz, ha egy hangyát végleg leállítottuk
    private boolean sleep; // Igaz, ha egy hangyát ideiglenesen megállítottuk
    public Turmite(Grid g, Display d, String fileName, int s) throws IOException{
        grid = g;
        display = d;
        setStartingPosition(g);
        state = "0";
        direction = 0;
        speed = s;
        read(fileName);
        terminated = false;
        sleep = false;
    }
    /// A hangya dolgozik ///
    @Override
    public void run() {
        while (!terminated) {
            /// Alszik vagy dolgozik? ///
            synchronized (this) { // Ha éppen alszik, akkor addig alszik, amíg fel nem ébresztik
                if(sleep) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            /// Megnézi melyik státuszban van és milyen mezőn áll ///
            for(List<String> s : stateMachine) {
                if(s.get(0).equals(state) && Integer.parseInt(s.get(1)) == grid.getValueAt(x, y)) { // Beállítja az új irányt
                    if(s.get(2).equals("L")) {
                        if(--direction < 0) { // Elforgatjuk, de ha olyan értéket vettünk fel, amit nem szabadott volna shiftelünk
                            direction = 3;
                        }
                    } else if(s.get(2).equals("R")) {
                        if(++direction > 3) {
                            direction = 0;
                        }
                    } else if(s.get(2).equals("U")) {
                        direction+=2;
                        if(direction == 4) {
                            direction = 0;
                        } else if(direction == 5) {
                            direction = 1;
                        }
                    } else { /* Nem fordul */ }
                    /// Ír a cellába ///
                    grid.setValueAt(x, y, Integer.parseInt(s.get(3)));
                    /// Lép ///
                    if(direction == 0) {
                        x--;
                    } else if(direction == 1) {
                        y++;
                    } else if(direction == 2) {
                        x++;
                    } else {
                        y--;
                    }
                    /// Ha lement a pályáról, akkor megszakítjuk a folyamatot ///
                    if(x < 0 || y < 0 || x >= grid.getWidth() || y >= grid.getHeight()) {
                        terminated = true;
                    }
                    /// Új státuszt vesz fel ///
                    state = s.get(4);
                    /// Frissíti a képernyőt ///
                    display.refresh(grid, x, y);
                    /// Várakozik kicsit a következő lépés előtt ///
                    try { // Jelenleg öt különböző sebesség van, de ez bővíthető
                        if(speed == 4) {
                            sleep(0,1);
                        } else if(speed == 3) {
                            sleep(1);
                        } else if(speed == 2) {
                            sleep(10);
                        } else if(speed == 1) {
                            sleep(100);
                        } // Ha a "speed" értéke ettől eltérő, akkor egyáltalán nem vár, tehát olyankor a leggyorsabb
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
    /// Az állapotgépet beolvassuk ///
    private void read(String fileName) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            stateMachine = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder input = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    if(line.charAt(i) != '-') { // Az összes elválasztót ignoráljuk
                        input.append(line.charAt(i));
                    }
                }
                if(input.toString().length() != 5) { // Ha rossz az egyik sor hossza, akkor visszatölti a default bemenetet
                    read("input\\LangtonSAnt.txt");
                    throw new IOException(); // Azért dob egy kivételt is, hogy tudjuk, baj volt a bemenettel
                }
                List<String> state = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    state.add(String.valueOf(input.charAt(i)));
                }
                stateMachine.add(state);
            }
        }
    }
    /// Hangya helyzete alapállapotba ///
    public void setStartingPosition(Grid g) { // Az alapállapot az, amikor nagyjából a rács közepén helyezkedik el
        x = (int) Math.floor((double) g.getWidth() /2);
        y = (int) Math.floor((double) g.getHeight() /2);
    }
    /// A hangyát végleg leállítjuk ///
    public void terminate() {
        terminated = true;
    }
    /// Megkérdezzük a hangya végleg megállt-e ///
    public boolean isTerminated() {
        return terminated;
    }
    /// A hangyát elaltatjuk vagy éppen felébresztjük ///
    public synchronized void sleepNWakeUp() {
        if(sleep) {
            sleep = false;
            notify();
        }
        else {
            sleep = true;
        }
    }
    /// Beállítjuk a hangya sebességét ///
    public void setSpeed(int s) {
        speed = s;
    }
    /// Beállítjuk a hangya irányát ///
    public void setDirection(int d) {
        if(d >= 0 && d <= 3) {
            direction = d;
        }
    }
    /// Lekérdezzük a hangya x koordinátáját ///
    public int getX() {
        return x;
    }
    /// Lekérdezzük a hangya y koordinátáját ///
    public int getY() {
        return y;
    }
}