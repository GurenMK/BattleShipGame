//Alexander Urbanyak
//Battle ship game
//Creates .txt log file and appends it

package battleshipgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;

public class LogFile {
    
    private File log;
    private Date date;
    private long time;
    private FileWriter fw;
    private PrintWriter output;
    
   LogFile () {}
    
   LogFile (String s) throws FileNotFoundException, FileAlreadyExistsException {
    
    log = new File("LogFile.txt");
    date = new Date();
    time = date.getTime();
    date.setTime(time);
   
        try {
            fw = new FileWriter("LogFile.txt", true);
            output = new PrintWriter(fw);
            output.println("\n" + date + " " + s + " started");
            output.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
   }
   public void append(String s) throws IOException {
        try {
            fw = new FileWriter("LogFile.txt", true);
            output = new PrintWriter(fw);
            output.println(s);
            output.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
   }
   public Date getTime() {
        date = new Date();
        time = date.getTime();
        date.setTime(time);
       return date;
   }
}
