package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame {
    private Grid grid;
    private Display display;
    private Turmite turmite;
    private Save saver;
    private int size;
    private int speed;
    private String fileName;
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
                String path = "input\\" + input.getText() + ".txt";
                File f = new File(path);
                if(f.exists()) {
                    fileName = path;
                    if(!turmite.isTerminated()) {
                        turmite.terminate();
                    }
                    grid.resize(size, size);
                    turmite.setStartingPosition(grid);
                    display.refresh(grid, turmite.getX(), turmite.getY());
                    startNStop.setText("Start");
                    output.setText("    Sikeres beolvasás!");
                }
                else {
                    output.setText("    A beolvasni kívánt fájl nem létezik!");
                    output.setForeground(Color.RED);
                }
            }
        });

        /// Turmesz sebességének beállítása ///
        setSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setForeground(Color.BLACK);
                speed = (int)speedValue.getValue();
                if(turmite.isTerminated()) {
                    turmite = new Turmite(grid, display, fileName, speed);
                }
                else {
                    turmite.setSpeed(speed);
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
                grid.resize(size, size);
                turmite.setStartingPosition(grid);
                display.refresh(grid, turmite.getX(), turmite.getY());
                if(turmite.isTerminated()) {
                    startNStop.setText("Start");
                }
                else {
                    turmite.setDirection(0);
                }
                output.setText("    A rács mérete sikeresen beállítva!");
            }
        });

        /// Turmesz elindítása, illetve megállítása ///
        startNStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setForeground(Color.BLACK);
                if(startNStop.getText().equals("Start")) {
                    if(turmite.isTerminated()) {
                        turmite = new Turmite(grid, display, fileName, speed);
                    }
                    turmite.start();
                    startNStop.setText("Stop");
                }
                else if(startNStop.getText().equals("Stop")){
                    turmite.sleepNWakeUp();
                    startNStop.setText("Folytat");
                }
                else {
                    turmite.sleepNWakeUp();
                    startNStop.setText("Stop");
                }
                output.setText("    Nincs új üzenet..");
            }
        });

        /// Grid letörlése, turmesz alaphelyzetbe állítása ///
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turmite.terminate();
                grid.resize(size, size);
                turmite.setStartingPosition(grid);
                display.refresh(grid, turmite.getX(), turmite.getY());
                startNStop.setText("Start");
                output.setText("    Rács és turmesz alaphelyzetbe állítva!");
            }
        });

        /// Pillanatkép készítése ///
        screenShot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turmite.sleepNWakeUp();
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
    private void newSize(JComboBox<String> sizeValue) {
        Object selectedItem = sizeValue.getSelectedItem();
        if (selectedItem.equals("25x25")) {
            size = 25;
        } else if (selectedItem.equals("250x250")) {
            size = 250;
        } else if (selectedItem.equals("500x500")) {
            size = 500;
        }
        else {
            size = 125;
        }
    }
    public MainFrame() {
        /// Felépítjük az ablakot ///
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true);
        init();
        setVisible(true);

        /// Rács létrehozása ///
        size = 125;
        grid = new Grid(size, size);

        /// Megjelenítés ///
        display = new Display(grid, (int) Math.floor(grid.getWidth()/2), (int) Math.floor(grid.getHeight()/2));
        Point mfLocation = getLocation();
        display.setLocation((int) mfLocation.getX() + getWidth(), (int) mfLocation.getY());
        display.setVisible(true);

        /// Turmesz létrehozása ///
        speed = 1;
        fileName = "input\\LangtonSAnt.txt";
        turmite = new Turmite(grid, display, fileName, speed);

        /// Pillanatkép készítő osztály létrehozása ///
        saver = new Save();
    }

    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
    }
}