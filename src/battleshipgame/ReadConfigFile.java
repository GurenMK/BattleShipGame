//Alexander Urbanyak
//Battle ship game
//Reads .txt config file using properties

package battleshipgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFile {
    
    private final File config;
  //Ships coordinates
    private String[] ship1;
    private final int[] location1;
    private String[] ship2;
    private final int[] location2;
    private String[] ship3;
    private final int[] location3;
    private String[] ship4;
    private final int[] location4;
    private final LogFile log;
    
    ReadConfigFile(File c) {
       config = c;
       location1 = new int[4]; 
       location2 = new int[4];
       location3 = new int[4];
       location4 = new int[4];
       log = new LogFile();
    }

    public void read() throws FileNotFoundException, IOException {
    Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream(config);
            prop.load(input);
        //ship1 coordinates
            String s1 = prop.getProperty("Ship1");
            ship1 = s1.split("[\\,\\-]");
        //convert String[] to int[]
            for (int i = 0; i < 4; i++) {
            location1[i] = Integer.parseInt(ship1[i]);
            }
        //ship2 coordinates
            String s2 = prop.getProperty("Ship2");
            ship2 = s2.split("[\\,\\-]");
        //convert String[] to int[]
            for (int i = 0; i < 4; i++) {
            location2[i] = Integer.parseInt(ship2[i]);
            }
        //ship2 coordinates
            String s3 = prop.getProperty("Ship3");
            ship3 = s3.split("[\\,\\-]");
        //convert String[] to int[]
            for (int i = 0; i < 4; i++) {
            location3[i] = Integer.parseInt(ship3[i]);
            }
        //ship2 coordinates
            String s4 = prop.getProperty("Ship4");
            ship4 = s4.split("[\\,\\-]");
        //convert String[] to int[]
            for (int i = 0; i < 4; i++) {
            location4[i] = Integer.parseInt(ship4[i]);
            }
        }
        
        catch(FileNotFoundException e) {
            log.append(log.getTime() + " Read ConfigFile error: " + e.toString());
            e.printStackTrace();
        }
    }
    public int[] getShip1() {
        return location1;
    }
    public int[] getShip2() {
        return location2;
    }
    public int[] getShip3() {
        return location3;
    }
    public int[] getShip4() {
        return location4;
    }
}
