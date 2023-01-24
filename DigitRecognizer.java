// Main
import java.io.*;
import java.util.*;
// Art
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;
// Sound
import javax.sound.sampled.*;

class Grid {
    int x, y;
    double color = 0;
    static int size;
    static int offset = -1;

    public Grid(int x, int y, int s) {
        this.x = x;
        this.y = y;
        size = s;
    }

    public int toScreenX() {
        return this.x * size + 200;
    }

    public int toScreenY(int height, int YGRIDS) {
        if (offset == -1) {
            offset = height/2 - (YGRIDS/2 * size);
        } 
        return this.y * size + offset;
    }

    static void paintGridFromMouse(Grid[][] grid, int mouseX, int mouseY, int XGRIDS, int YGRIDS) {
        int xpos = (int) ((mouseX - 200)/size);
        int ypos = (int) ((mouseY - offset)/size);

        if (xpos >= 0 && xpos < XGRIDS && ypos >= 0 && ypos < YGRIDS) {
            if (grid[xpos][ypos].color == 1) {
                return;
            }
        }
        

        for (int nx = -1; nx <= 1; nx++) {
            for (int ny = -1; ny <= 1; ny++) {
                if (xpos + nx >= 0 && xpos + nx < XGRIDS && ypos + ny >= 0 && ypos + ny < YGRIDS) {
                    if (grid[xpos + nx][ypos + ny].color != 0) {
                        grid[xpos + nx][ypos + ny].color = 1;
                    } else {
                        grid[xpos + nx][ypos + ny].color = (Math.abs(nx) == 1 ? .75 : 1) - (Math.abs(ny) == 1 ? .25 : 0);
                    }
                }
            }
        }
    }
}

class NeuralNetwork {
    int STARTING_NEURONS;
    int HIDDEN_LAYERS;
    int HIDDEN_LAYER_NEURONS;

}

public class DigitRecognizer extends JPanel {
    final int XGRIDS = 28;
    final int YGRIDS = 28;
    final int DRAWING_AREA_SIZE = 25;

    boolean mouseDown = false;
    Grid[][] grid = new Grid[XGRIDS][YGRIDS];

    void setColor(Graphics graphics, int r, int g, int b) { Color customColor = new Color(r, g, b); graphics.setColor(customColor); }

    public void paintComponent(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics.create();
        final int width = getSize().width;
        final int height = getSize().height;

        // Generate Grid
        for (int y = 0; y < XGRIDS; y++) {
            for (int x = 0; x < XGRIDS; x++) {
                Grid localGrid = grid[x][y];
                setColor(g, (int) (localGrid.color * 255), (int) (localGrid.color * 255), (int) (localGrid.color * 255));
                g.fillRect(localGrid.toScreenX(), localGrid.toScreenY(height, YGRIDS), DRAWING_AREA_SIZE, DRAWING_AREA_SIZE);
            }
        }

        if (mouseDown) {
            Grid.paintGridFromMouse(grid, MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y - 20, XGRIDS, YGRIDS);
        }
    }

    public DigitRecognizer() {
        MouseListener mouselistener = new MouseListener() {
            public void mouseClicked(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { }

            public void mouseExited(MouseEvent e) { }

            public void mousePressed(MouseEvent e) { mouseDown = true; }

            public void mouseReleased(MouseEvent e) { mouseDown = false; }
        };
        addMouseListener(mouselistener);
        setFocusable(true);
    }

    public static void main(String[] args) {
        final DigitRecognizer panel = new DigitRecognizer();
        final JFrame frame = new JFrame("Digit Recognizer");

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().add(panel, BorderLayout.CENTER); 
        frame.setVisible(true);

        for (int x = 0; x < panel.XGRIDS; x++) {
            for (int y = 0; y < panel.XGRIDS; y++) {
                panel.grid[x][y] = new Grid(x, y, panel.DRAWING_AREA_SIZE);
            }
        }

        while (true) { frame.repaint(); }
    }
}
