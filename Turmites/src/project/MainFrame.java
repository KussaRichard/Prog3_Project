package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    private Grid grid; // A rács
    private Display display; // A megjelenítő felület
    private Turmite turmite; // A turmesz vagy hangya
    private Save saver; // A kép készítő
    private int size; // A rács mérete (mindig négyzet alapú)
    private int speed; // A turmesz (vagy hangya) sebessége
    private String fileName; // A beolvasás forrása
    private void init() {
        JPanel mainPanel = new JPanel(new GridLayout(8, 1));

        /// Komponensek létrehozása ///
        JButton exit = new JButton("Kilép");
        JLabel label = new JLabel(" Fájl neve és kiterjesztése: ");
        JTextField input = new JTextField();
        JButton read = new JButton("Beolvas");
        JLabel label2 = new JLabel(" Turmesz sebessége: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner speedValue = new JSpinner(spinnerModel);
        JButton setSpeed = new JButton("Beállít");
        JButton startNStop = new JButton("Start");
        JButton screenShot = new JButton("Pillanatkép");
        JButton clear = new JButton("Töröl");
        JLabel label3 = new JLabel(" Rács mérete: ");
        String[] data = {"25x25", "125x125", "250x250", "500x500"};
        JComboBox<String> sizeValue = new JComboBox<>(data);
        sizeValue.setSelectedIndex(1);
        JButton setGrid = new JButton("Beállít");

        /// Rendszerüzenetek ///
        JLabel output = new JLabel("    Itt fognak majd megjelenni a rendszerüzenetek");

        /// Kilépés a programból ///
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /// Beolvasás helye ///
        read.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setForeground(Color.BLACK);
                output.setText("    Nincs új üzenet..");
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
            }
        });

        /// Turmesz sebességének beállítása ///
        setSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setForeground(Color.BLACK);
                speed = (int)speedValue.getValue();
                if(!turmite.isTerminated()) {
                    turmite.setSpeed(speed); // Menet közben is át tudjuk állítani
                }
                output.setText("    A turmesz sebessége sikeresen beállítva!");
            }
        });

        /// Rács méretének beállítása ///
        setGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                output.setText("    A rács mérete sikeresen beállítva!");
            }
        });

        /// Turmesz elindítása, illetve megállítása ///
        startNStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setForeground(Color.BLACK);
                output.setText("    Nincs új üzenet..");
                if(startNStop.getText().equals("Start")) { // Amikor a turmesz alaphelyzetben van
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
            }
        });

        /// Grid letörlése, turmesz alaphelyzetbe állítása ///
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turmite.terminate(); // Az eddig futó hangyát mindenképp megállítjuk
                grid.resize(size, size); // A rácsot letöröljük
                try { // Turmesz alaphelyzetbe állítása
                    turmite = new Turmite(grid, display, fileName, speed);
                } catch (IOException NoException) { /* A fileName mindig csak olyan értéket vesz fel, ami olvasható */ }
                display.refresh(grid, turmite.getX(), turmite.getY()); // Megjelenítő frissítése
                startNStop.setText("Start");
                output.setForeground(Color.BLACK);
                output.setText("    Rács és turmesz alaphelyzetbe állítva!");
            }
        });

        /// Pillanatkép készítése ///
        screenShot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turmite.sleepNWakeUp(); // Megállítjuk a turmeszt egy pillanatra, hogy ne írjon a rácsra, amíg készül a kép
                saver.print(grid, output);
                turmite.sleepNWakeUp();
            }
        });

        /// Komponensek felvétele ///
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
    private JPanel createPanelWithOneComponent(Component comp) {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(comp);
        return panel;
    }
    private JPanel createPanelWithThreeComponent(Component a, Component b, Component c) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(a);
        panel.add(b);
        panel.add(c);
        return panel;
    }
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
    public MainFrame() {
        /// Felépítjük az ablakot ///
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true); // Nem lesz szükség rá, mert lesz egy külön gomb a program leállítására
        init();
        setVisible(true);

        /// Rács létrehozása ///
        size = 125;
        grid = new Grid(size, size); // Lehetne másmilyen is, de ebben a programban négyzet alapú rácsot fogunk használni

        /// Megjelenítés ///
        display = new Display(grid, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
        Point mfLocation = getLocation();
        display.setLocation((int) mfLocation.getX() + getWidth(), (int) mfLocation.getY());
        display.setVisible(true); // Külön ablaka van

        /// Turmesz létrehozása ///
        speed = 1;
        fileName = "input\\LangtonSAnt.txt";
        try { // A "read" funkció miatt kell csak a try-catch blokk
            turmite = new Turmite(grid, display, fileName, speed);
        } catch (IOException exception) { /* A default bemenetnek jónak kell lennie */ }

        /// Pillanatkép készítő osztály létrehozása ///
        saver = new Save();
    }
    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
    }
}