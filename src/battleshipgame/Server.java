//Alexander Urbanyak
//Battle ship game
//Server side

package battleshipgame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final char MISS;
    private final char HIT;
    private final char LOSE;
    private final char WINN;   // Internal protocol only  DO NOT SEND
    
    private final String config_file; //file with game settings
    private final File file;
    private final ReadConfigFile config;
    private final Coordinates c;
    private final Dimension dimension;
    private Board board;
    private final LogFile log;
    
    private final String ip; //ip adress
    private final int port; //port number
    private final int xCoord; //x coordinates for the game board
    private final int yCoord; //y coordinates for the game board
    private final int xCoord1; //x coordinates for result window
    private final int yCoord1; //y coordinates for result window
    private int x, y; //coordinates sent by the client
    private char client_shot; //result of the client shot
    private char server_shot; //result of the server shot
    private int shot; //clients x,y coordinates as a single number
    private String myX; //x to be sent
    private String myY; //y to be sent
    private String send; // string to be sent
    //ships
    private ArrayList<Integer> ship1;
    private ArrayList<Integer> ship2;
    private ArrayList<Integer> ship3;
    private ArrayList<Integer> ship4;
    //shots list
    private ArrayList<Integer> shots = new ArrayList<> ();  
    //all ships
    private final ArrayList<Integer> all_ships;
    
    Server (String s, int p) throws IOException {
        MISS = 'M';
        HIT  = 'H';
        LOSE = 'L';
        WINN = 'W';   // Internal protocol only  DO NOT SEND
        
        config_file = "ConfigFile.txt"; //text file with game settings
        file = new File(config_file);
        config = new ReadConfigFile(file); 
        c = new Coordinates();
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
        board = new Board();
        log = new LogFile();
        
        ip = s; //ip adress
        port = p; //port number
        //x,y coordinate where the board will be placed
        xCoord = (int)(dimension.getWidth()) / 25;
        yCoord = (int)(dimension.getHeight()) / 20;
        //x,y coordinate where the result window will be placed
        xCoord1 = (int)(dimension.getWidth()) / 11;
        yCoord1 = (int)(dimension.getHeight()) / 6;
        //shots list
        shots = c.shotList();
        //add all ships to a list
        all_ships = new ArrayList<> ();
    }
    
    public void setup() throws IOException {
        try {
        //create the game board
        board = new Board("Server", xCoord, yCoord);
        //read the game settings
        config.read();
        //ships
        ship1 = c.getShip(config.getShip1());
        ship2 = c.getShip(config.getShip2());
        ship3 = c.getShip(config.getShip3());
        ship4 = c.getShip(config.getShip4());
        all_ships.addAll(ship1);
        all_ships.addAll(ship2);
        all_ships.addAll(ship3);
        all_ships.addAll(ship4);
        //place ships on the board
        board.placeShip(ship1);
        board.placeShip(ship2);
        board.placeShip(ship3);
        board.placeShip(ship4);
        }
        catch (Exception e) {
            log.append(log.getTime() + " Server setup error: " + e.toString());
        }
    }
    public void run() throws IOException, ClassNotFoundException, InterruptedException {
        try {
            
        ServerSocket s = new ServerSocket(port);
        Socket ss = s.accept();
        ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
        ObjectOutputStream p = new ObjectOutputStream(ss.getOutputStream());
               
        while (all_ships.size() > 0) {
            
            String start = (String) ois.readObject();
            server_shot = start.charAt(0);         
            String[] r = start.split(",");
            x = Integer.parseInt(r[1]);
            y = Integer.parseInt(r[2]);
            //convert the coordinates
            shot = c.getShot(x, y);
            
            if (server_shot == LOSE) { 
                    log.append(log.getTime() + " Server won!");
                    Thread.sleep(1500);
                    board.resultWindow("Server won", xCoord1, yCoord1);
                    break;
                }

            if (server_shot == MISS || server_shot == HIT) {
                //if client hit
                if (all_ships.contains(shot)) {
                    Thread.sleep(100); //pause
                    client_shot = HIT;
                    all_ships.remove(all_ships.indexOf(shot)); //remove the hit ships coordinates
                    board.hit(shot);
                }
                //if client missed
                else {
                    client_shot = MISS;
                    board.miss(shot);
                }
            }
            
            myX = Integer.toString(c.getShotX(shots.get(0)));
            myY = Integer.toString(c.getShotY(shots.get(0)));
            
            //when all ships have sunk
            if (all_ships.isEmpty()) {
                log.append(log.getTime() + " Server lost!");
                client_shot = LOSE;
                send = client_shot + "," + myX + "," +  myY;
                p.writeObject(send);
                Thread.sleep(1500);
                board.resultWindow("Server lost", xCoord1, yCoord1);
                break;
            }

            send = client_shot + "," + myX + "," +  myY; 
            p.writeObject(send);
            shots.remove(0); //remove shot after its taken
        }
        s.close(); 
        }
        catch (IOException e) {
            log.append(log.getTime() + " Server error: " + e.toString());
            e.printStackTrace();
        }

    }
}
