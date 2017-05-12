//Alexander Urbanyak
//Battle ship game
//Main class, runs as a server or a client

package battleshipgame;

import java.io.IOException;

public class BattleShipGame {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        
        LogFile log = new LogFile();
        
        try {
            if (args[0].equals("Server")){
                int port = Integer.parseInt(args[2]);
                Server s = new Server(args[1],port);
                log = new LogFile(args[0]);
                s.setup();
                s.run();
            }
            if (args[0].equals("Client")){
                int port = Integer.parseInt(args[2]);
                Client c = new Client(args[1],port);              
                log = new LogFile(args[0]);
                c.setup();
                Thread.sleep(2000);
                c.run();
            }
        }
        catch (Exception e) {
            System.out.println("You must include 3 arguments");
            System.out.println("[Server/Client] [IP adress] [Port number]\n");
            log.append(log.getTime() + " Main class error " + "(" + args[0] + "): " + e.toString());
            e.printStackTrace();
        }
    }
}

