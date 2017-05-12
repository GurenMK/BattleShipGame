//Alexander Urbanyak
//Battle ship game
//Client side

package battleshipgame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private final char MISS;
    private final char HIT;
    private final char LOSE;
    private final char WINN;   // Internal protocol only  DO NOT SEND
    
    private final String config_file;
    private final File file;
    private final ReadConfigFile config;
    private final Coordinates c;
    private final Dimension dimension;
    private Board board;
    private final LogFile log;
    
    private final String ip; //ip address
    private final int port; //port number
    private final int xCoord; //x coordinates for the game board
    private final int yCoord; //y coordinates for the game board
    private final int xCoord1; //x coordinates for result window
    private final int yCoord1; //y coordinates for result window
    private int x, y; //coordinates sent by the server
    private int shot; //clients x,y coordinates as a single number
    private String myX; //x to be sent 
    private String myY; //y to be sent
    private String send; //string to be sent 
    private char server_shot; //result of the server shot
    private char client_shot; //result of the client shot
    //ships
    private ArrayList<Integer> ship1;
    private ArrayList<Integer> ship2;
    private ArrayList<Integer> ship3;
    private ArrayList<Integer> ship4;
    //shots
    private final ArrayList<Integer> shots; 
    //all ships
    private final ArrayList<Integer> all_ships;
    
    Client (String s, int p) throws IOException {
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
        
        ip = s;
        port = p;
        //x,y coordinates for the game board
        xCoord = (int)(dimension.getWidth()) / 2;
        yCoord = (int)(dimension.getHeight()) / 20;
        //x,y coordinates for result window
        xCoord1 = (int)(dimension.getWidth()) / 2 + ((int)(dimension.getWidth()) / 16);
        yCoord1 = (int)(dimension.getHeight()) / 6;
        //ships
        ship1 = c.getShip(config.getShip1());
        ship2 = c.getShip(config.getShip2());
        ship3 = c.getShip(config.getShip3());
        ship4 = c.getShip(config.getShip4());
        //shots to be fired
        shots = c.shotList();
        //add all ships to one list
        all_ships = new ArrayList<> ();
    }
    
    public void setup() throws IOException {
        try {
        //create the game board
        board = new Board("Client", xCoord, yCoord);
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
            log.append(log.getTime() + " Client setup error: " + e.toString());
        }
    }   
    public void run() throws IOException, ClassNotFoundException, InterruptedException  {
            
        try {
            
        Socket echo = new Socket(ip, port);
        ObjectOutputStream p = new ObjectOutputStream(echo.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(echo.getInputStream());
        
        while (all_ships.size() > 0) {
            
            myX = Integer.toString(c.getShotX(shots.get(0)));
            myY = Integer.toString(c.getShotY(shots.get(0))); 
            server_shot = MISS;
 
            send = server_shot + "," + myX + "," +  myY;
            
            //send
            p.writeObject(send);
            //System.out.println("Client sent " + server_shot + "," + myX + "," +  myY);
            shots.remove(0);    //remove the sent shot
             
            //recieve
            String read = (String) ois.readObject();
            client_shot = read.charAt(0);    
            String[] r = read.split(",");
            x = Integer.parseInt(r[1]);
            y = Integer.parseInt(r[2]);
            //convert x,y into equivalent array list value
            shot = c.getShot(x, y);
            
            //client lost the game
                if (client_shot == LOSE) {
                    log.append(log.getTime() + " Client won!");
                    Thread.sleep(1500); //pause
                    board.resultWindow("Client won", xCoord1, yCoord1);
                    break;
                }
            //System.out.println("Server sent " + client_shot + " with " + x + " and " + y);
            
            //while shots are valid
            if (client_shot == MISS || client_shot == HIT) {
                //if server hit
                if (all_ships.contains(shot)) {
                    Thread.sleep(100); //pause
                    server_shot = HIT;
                    all_ships.remove(all_ships.indexOf(shot)); //remove the hit ships coordinates
                    board.hit(shot); 
                }
                //if server missed
                else {
                    server_shot = MISS;
                    board.miss(shot);
                }
                    //when all ships are sunk
                    if (all_ships.isEmpty()) {
                    log.append(log.getTime() + " Client lost!");
                    server_shot = LOSE;
                    send = server_shot + "," + myX + "," +  myY;
                    p.writeObject(send);
                    Thread.sleep(1500); //pause
                    board.resultWindow("Client lost", xCoord1, yCoord1);
                    break;
                    }
            }
        }
        echo.close();
        }
        catch (IOException e) {
            log.append(log.getTime() + " Client error: " + e.toString());
            e.printStackTrace();
        }
    }

}
