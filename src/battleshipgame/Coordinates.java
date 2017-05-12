//Alexander Urbanyak
//Battle ship game
//Create shots list
//Converts coordinates to ArrayLists
//Creates lists for ship locations 
//Converts ArrayList values to x and y coordinates

package battleshipgame;

import java.util.ArrayList;

public class Coordinates {
    
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int columns;    
    private int increment;
    private int row;
    private int row_increment;
    private int max_shots;
    
    public Coordinates() {
    }
    //array of coordinates from config file
    public int getX1(int[] array) {
        x1 = array[0];
        return x1;
    }
    public int getY1(int[] array) {
        y1 = array[1];
        return y1;
    }
    public int getX2(int[] array) {
        x2 = array[2];
        return x2;
    }
    public int getY2(int[] array) {
        y2 = array[3];
        return y2;
    }
    //converts head and tail of the ship coordinates to ArrayList (x,y to single number)
    //and fills in the points in between
    public ArrayList<Integer> getShip(int[] array) {
        ArrayList<Integer> ship = new ArrayList<>();
        columns = 100;
        increment = 100;
        row = 1;
        row_increment = 1;
        //convert x,y coordinates into an equivalent single number
        //if ship is vertical 
        if (array[0] == array[2]) {
            int vertical_length = Math.abs(array[3] - array[1]);
            ship.add((array[1] * columns) + array[0]);
                for (int j = 1; j <= vertical_length; j++) {
                ship.add(ship.get(0) + columns);
                columns += increment;
                }
    return ship;
        }
        //if ship is horizontal
        else {
            int horizontal_length = Math.abs(array[2] - array[0]);
            ship.add((array[1] * columns) + array[0]);
                for (int j = 1; j <= horizontal_length; j++) {
                ship.add(ship.get(0) + row);
                row += row_increment;
                }
    return ship;
        }
    }
    //shots for each square 0-9999 (10000 shots total) in random order
    public ArrayList<Integer> shotList() {
      max_shots = 10000;
      ArrayList<Integer> shots = new ArrayList<>();
      int[] array = new int [max_shots];
         for (int i = 0; i < array.length; i++) {
             array[i] = 0 + (int)(Math.random() * max_shots);  
            for (int j = 0; j < i; j++) {
                if (array[i] == array[j]){          
                 i--;    //if [i] is a duplicate run the outer loop again 
                 shots.remove(i);
                 break;
                }
            }
            shots.add(array[i]);
         }
         return shots;
   }
    //convert a x,y value into a single equivalent number for a shot
    public int getShot(int x, int y) {    
    columns = 100;
        int shot = (y * columns) + x;
    return shot;
    }
    //find x coordinate from a single number equivalent from shotsList
    public int getShotX(int shot) {   
        columns = 100;
        if (shot > 0 && shot % columns != 0) {
            int column_of_number = shot / columns;
            int x = shot - (column_of_number * columns);
    return x;
        }   
        else
    return 0;
    }
    //find y coordinate from a single number equivalent from shotsList
    public int getShotY(int shot) {
        columns = 100;
        int y = shot / columns;
    return y;
    }
}
