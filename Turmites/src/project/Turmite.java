package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Turmite extends Thread {
    Grid grid;
    Display display;
    List<List<String>> stateMachine;
    private String state;
    private int direction;
    private int x;
    private int y;
    int speed;
    boolean terminated;
    boolean sleep;
    public Turmite(Grid g, Display d, String fileName, int s) {
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
    @Override
    public void run() {
        while (!terminated) {
            synchronized (this) {
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
                if(s.get(0).equals(state) && Integer.parseInt(s.get(1)) == grid.getValueAt(x, y)) {
                    /// Beállítja az új irányt ///
                    if(s.get(2).equals("L")) {
                        if(--direction < 0) {
                            direction = 3;
                        }
                    }
                    else if(s.get(2).equals("R")) {
                        if(++direction > 3) {
                            direction = 0;
                        }
                    }
                    else if(s.get(2).equals("U")) {
                        direction+=2;
                        if(direction == 4) {
                            direction = 0;
                        }
                        else if(direction == 5) {
                            direction = 1;
                        }
                    }
                    else { /* Nem fordul */ }
                    /// Ír a cellába ///
                    grid.setValueAt(x, y, Integer.parseInt(s.get(3)));
                    /// Lép ///
                    if(direction == 0) {
                        x--;
                    }
                    else if(direction == 1) {
                        y++;
                    }
                    else if(direction == 2) {
                        x++;
                    }
                    else {
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
                    try {
                        if(speed == 4) {
                            sleep(0,1);
                        } else if(speed == 3) {
                            sleep(1);
                        } else if(speed == 2) {
                            sleep(10);
                        } else if(speed == 1) {
                            sleep(100);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
    private void read(String fileName){
        String filePath = fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            stateMachine = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] inputs = line.split("-");
                List<String> list = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    list.add(inputs[i]);
                }
                stateMachine.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setStartingPosition(Grid g) {
        x = (int) Math.floor(g.getWidth()/2);
        y = (int) Math.floor(g.getHeight()/2);
    }
    public void terminate() {
        terminated = true;
    }
    public boolean isTerminated() {
        return terminated;
    }
    public synchronized void sleepNWakeUp() {
        if(sleep) {
            sleep = false;
            notify();
        }
        else {
            sleep = true;
        }
    }
    public void setSpeed(int s) {
        speed = s;
    }
    public void setDirection(int d) {
        if(d >= 0 && d <= 3) {
            direction = d;
        }
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}