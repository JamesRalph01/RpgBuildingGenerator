/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.ArrayList;

/**
 *
 * @author chrisralph
 */
public class Building {
    
    private ArrayList<Room> rooms;
    
    public Building() {
        rooms = new ArrayList<Room>();
    }
    
    public ArrayList<Room> rooms() {
        return rooms;
    }
   
}
