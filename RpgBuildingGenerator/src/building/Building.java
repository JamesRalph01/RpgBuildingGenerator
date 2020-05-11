/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;

/**
 *
 * @author chrisralph
 */
public class Building {

    private ArrayList<Wall> externalWalls;
    private ArrayList<Room> rooms;
    
    public Building() {
        externalWalls = new ArrayList<>();
        rooms = new ArrayList<>();
    }
    
    public ArrayList getExternalWalls() {
        return externalWalls;
    }
    
    public ArrayList getRooms() {
        return rooms;
    }
}
