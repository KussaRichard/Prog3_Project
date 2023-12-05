package project;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Ő a főosztály, ahol létrehozzuk a menü ablakát.
 * Innen adunk ki parancsokat a többi osztály felé.
 */
public class MainFrame extends JFrame {
    /**
     * A rács amin a hangya dolgozik.
     */
    private Grid grid;

    /**
     * A megjelenítő osztály.
     */
    private Display display;

    /**
     * A hangya.
     */
    private Turmite turmite;

    /**
     * A kép mentését végző osztály.
     */
    private Save saver;

    /**
     * A rács mérete (mindig négyzet alapú).
     */
    private int size;

    /**
     * A hangya sebessége.
     */
    private int speed;

    /**
     * A beolvasás forrása.
     */
    private String fileName;

    /**
     * Inicializáló függvény, ami létrehozza a menü ablakát
     */
    private void init() {
        JPanel mainPanel = new JPanel(new GridLayout(8, 1));

        // Komponensek létrehozása //
        JButton exit = new JButton("Kilép");
        JLabel label = new JLabel(" Fájl neve: ");
        JTextField input = new JTextField();
        JButton read = new JButton("Beolvas");
        JLabel label2 = new JLabel(" Hangya sebessége: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner speedValue = new JSpinner(spinnerModel);
        JButton setSpeed = new JButton("Beállít");
        JButton startNStop = new JButton("Start");
        JButton screenShot = new JButton("Pillanatkép");
        JButton clear = new JButton("Töröl");
        JLabel label3 = new JLabel(" Pálya mérete: ");
        String[] data = {"25x25", "125x125", "250x250", "500x500"};
        JComboBox<String> sizeValue = new JComboBox<>(data);
        sizeValue.setSelectedIndex(1);
        JButton setGrid = new JButton("Beállít");

        // Rendszerüzenetek //
        JLabel output = new JLabel("    Itt fognak majd megjelenni a rendszerüzenetek");

        // Kilépés a programból //
        exit.addActionListener(e -> {
            turmite.terminate();
            saver.print(grid, output); // Egy utolsó képet még készít, hogy fennmaradjon a hangya munkája
            System.exit(0);
        });

        // Beolvasás helye //
        read.addActionListener(e -> {
            output.setForeground(Color.BLACK);
            output.setText("    Sikeres beolvasás!");
            String old = fileName;
            String path = "input\\" + input.getText() + ".txt";
            File f = new File(path);
            if(f.exists()) { // Csak akkor végezzük el a beolvasást, ha létezik a fájl
                fileName = path;
                if(!turmite.isTerminated()) {
                    turmite.terminate();
                }
                try {
                    turmite = new Turmite(grid, display, fileName, speed);
                } catch (IOException exception) { // Hibás bemenetnél visszatöltjük a korábbi bemenetet
                    output.setForeground(Color.RED);
                    output.setText("    Hibás bemenet!");
                    fileName = old;
                    try {
                        grid.resize(size, size);
                        display.refresh(grid, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
                        turmite = new Turmite(grid, display, fileName, speed);
                    } catch (IOException noException) { /* A default bemenetnek jónak kell lennie */ }
                }
                grid.resize(size, size);
                display.refresh(grid, turmite.getX(), turmite.getY());
                startNStop.setText("Start");
            }
            else {
                output.setForeground(Color.RED);
                output.setText("    A beolvasni kívánt fájl nem létezik!");
            }
        });

        // Turmesz sebességének beállítása //
        setSpeed.addActionListener(e -> {
            output.setForeground(Color.BLACK);
            speed = (int)speedValue.getValue();
            if(!turmite.isTerminated()) {
                turmite.setSpeed(speed); // Menet közben is át tudjuk állítani
            }
            output.setText("    A hangya sebessége sikeresen beállítva!");
        });

        // Rács méretének beállítása //
        setGrid.addActionListener(e -> {
            output.setForeground(Color.BLACK);
            newSize(sizeValue);
            grid.resize(size, size); // A rácsot alaphelyzetbe állítjuk
            if(turmite.isTerminated()) {
                startNStop.setText("Start");
            }
            else { // Egy működő turmeszt nincs oka leállítani, csak alaphelyzetbe állítja
                turmite.setDirection(0);
                turmite.setStartingPosition(grid);
            }
            display.refresh(grid, turmite.getX(), turmite.getY()); // Frissítjük a megjelenítőt
            output.setText("    A pálya mérete sikeresen beállítva!");
        });

        // Turmesz elindítása, illetve megállítása //
        startNStop.addActionListener(e -> {
            output.setForeground(Color.BLACK);
            output.setText("    Nincs új üzenet..");
            if(startNStop.getText().equals("Start")) { // Amikor a turmesz alaphelyzetben van
                if(turmite.isTerminated()) {
                    try {
                        display.refresh(grid, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
                        turmite = new Turmite(grid, display, fileName, speed);
                    } catch (IOException noException) { /* Nem kell külön kezelni, mert fileName csak olyan lehet, ami beolvasható */ }
                }
                turmite.start();
                startNStop.setText("Stop");
            }
            else if(startNStop.getText().equals("Stop")){ // Amikor a turmesz éppen dolgozik
                turmite.sleepNWakeUp();
                startNStop.setText("Folytat");
            }
            else { // Amikor a turmesz már egyszer elindult, de most éppen áll
                turmite.sleepNWakeUp();
                startNStop.setText("Stop");
            }
        });

        // Grid letörlése, turmesz alaphelyzetbe állítása //
        clear.addActionListener(e -> {
            turmite.terminate(); // Az eddig futó hangyát mindenképp megállítjuk
            grid.resize(size, size); // A rácsot letöröljük
            try { // Turmesz alaphelyzetbe állítása
                turmite = new Turmite(grid, display, fileName, speed);
            } catch (IOException NoException) { /* A fileName mindig csak olyan értéket vesz fel, ami olvasható */ }
            display.refresh(grid, turmite.getX(), turmite.getY()); // Megjelenítő frissítése
            startNStop.setText("Start");
            output.setForeground(Color.BLACK);
            output.setText("    Pálya és hangya alaphelyzetbe állítva!");
        });

        // Pillanatkép készítése //
        screenShot.addActionListener(e -> {
            turmite.sleepNWakeUp(); // Megállítjuk a turmeszt egy pillanatra, hogy ne írjon a rácsra, amíg készül a kép
            saver.print(grid, output);
            turmite.sleepNWakeUp();
        });

        // Komponensek felvétele //
        mainPanel.add(createPanelWithOneComponent(startNStop));
        mainPanel.add(createPanelWithThreeComponent(label, input, read));
        mainPanel.add(createPanelWithThreeComponent(label2, speedValue, setSpeed));
        mainPanel.add(createPanelWithThreeComponent(label3, sizeValue, setGrid));
        mainPanel.add(createPanelWithOneComponent(screenShot));
        mainPanel.add(createPanelWithOneComponent(clear));
        mainPanel.add(createPanelWithOneComponent(exit));
        mainPanel.add(createPanelWithOneComponent(output));

        add(mainPanel);
        pack();
        setLocation(0, 0);
        setVisible(true);
    }

    /**
     * Visszaad egy JPanel -t a megadott komponenssel
     * @param comp A komponens, amit a panelnak tartalmaznia kell
     * @return
     */
    private JPanel createPanelWithOneComponent(Component comp) {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(comp);
        return panel;
    }

    /**
     * Visszaad egy JPanel -t a három megadott komponenssel
     * @param a Az egyik komponens, amit a panelnak tartalmaznia kell
     * @param b A másik komponens, amit a panelnak tartalmaznia kell
     * @param c A harmadik komponens, amit a panelnak tartalmaznia kell
     * @return
     */
    private JPanel createPanelWithThreeComponent(Component a, Component b, Component c) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(a);
        panel.add(b);
        panel.add(c);
        return panel;
    }

    /**
     * Átállítja a size értékét.
     * @param sizeValue Az ő választott eleme mondja meg milyen értéket adjon size -nak
     */
    private void newSize(JComboBox<String> sizeValue) { // Ezúttal csak négyzet alapú rácsot használunk
        Object selectedItem = sizeValue.getSelectedItem();
        if (selectedItem.equals("25x25")) {
            size = 25;
        } else if (selectedItem.equals("250x250")) {
            size = 250;
        } else if (selectedItem.equals("500x500")) {
            size = 500;
        } else {
            size = 125;
        }
    }

    /**
     * Felveszi a szükséges osztályokat.
     * Inicializálja az ablakokat.
     */
    public MainFrame() {
        // Felépítjük az ablakot //
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true); // Nem lesz szükség rá, mert lesz egy külön gomb a program leállítására
        init();
        setVisible(true);

        // Rács létrehozása //
        size = 125;
        grid = new Grid(size, size); // Lehetne másmilyen is, de ebben a programban négyzet alapú rácsot fogunk használni

        /// Megjelenítés ///
        display = new Display(grid, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
        Point mfLocation = getLocation();
        display.setLocation((int) mfLocation.getX() + getWidth(), (int) mfLocation.getY());
        display.setVisible(true); // Külön ablaka van

        // Turmesz létrehozása //
        speed = 1;
        fileName = "input\\LangtonSAnt.txt";
        try { // A "read" funkció miatt kell csak a try-catch blokk
            turmite = new Turmite(grid, display, fileName, speed);
        } catch (IOException exception) { /* A default bemenetnek jónak kell lennie */ }

        // Pillanatkép készítő osztály létrehozása //
        saver = new Save();
    }

    /**
     * Main függvény, amivel működésbe hozzuk a programot.
     * @param args Bemeneti argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
    }
}