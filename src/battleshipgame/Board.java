//Alexander Urbanyak
//Battle ship game
//Creates the game boards, ships, changes colors for hit and missed shots
//Result pop up window

package battleshipgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board {
    
    private JFrame frame;
    private LogFile log;
    //number of coordinates
    private int squares;
    private JButton button_array [];
    //window layout
    private int rows;
    private int columns;
    //board dimentions
    private int x_board;
    private int y_board;
    
    private JFrame window;
    //result window
    private int x_window;
    private int y_window;
    private JButton button;
    private JLabel label;
    private JPanel top;
    private JPanel center;
    private JPanel main;
    private int padding;
    
    Board () {}
    
    Board (String s, int x, int y) throws IOException {
        frame = new JFrame();
        log = new LogFile();
        squares = 10000;
        button_array = new JButton[squares];
        rows = 100;
        columns = 100;
        x_board = 720;
        y_board = 740;
        window = new JFrame();
        x_window = 300;
        y_window = 140;
        button = new JButton();
        label = new JLabel();
        top = new JPanel();
        center = new JPanel();
        main = new JPanel();
        padding = 30;
        
        try {
          frame.setTitle(s);
              for (int i = 0; i < squares; i++) {
                button_array[i] = new JButton();
                button_array[i].setBackground(new Color(1, 0, 102));
                button_array[i].setBorder(BorderFactory.createLineBorder(new Color(1, 0, 102)));
                frame.add(button_array[i]);
              }
           frame.setLayout(new GridLayout(rows, columns));   
           frame.setPreferredSize(new Dimension(x_board, y_board)); 
           frame.pack();
           frame.setLocation(x, y);
           frame.getContentPane().setBackground(Color.blue);
           frame.setVisible(true);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } 
        catch (Exception e) {
           log.append(log.getTime() + " Board class error: " + e.toString());
           e.printStackTrace();
        }
    }
    public void hit(int c) {
          button_array[c].setBackground(Color.yellow);
          button_array[c].setBorder(BorderFactory.createLineBorder(Color.yellow));
    }
    public void miss(int c) {
          button_array[c].setBackground(new Color(40, 124, 204));
          button_array[c].setBorder(BorderFactory.createLineBorder(new Color(40, 124, 204)));
    }
    public void placeShip(ArrayList<Integer> list) {
        for (int i = 0;  i < list.size(); i++) {
           button_array[list.get(i)].setBackground(Color.red);
        }
    }
    public void resultWindow(String s, int x, int y) {
        
        window.setTitle(s);
        window.setSize(x_window, y_window);
        window.setLocation(x, y);
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      if (s.equals("Client lost") || s.equals("Server lost"))
        label.setText("You Lose!");
      else {
        label = new JLabel("You Win!"); 
      } 
        button.setText("OK");

        top.add(label);
        center.add(button);

        main.setLayout(new BorderLayout(0, padding)); 
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        window.add(main);

        button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        window.dispose();
        frame.dispose();
        }
        });
    }
}
