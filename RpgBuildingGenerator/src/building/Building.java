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

    public enum WealthIndicatorType {
        POOR,
        WEATHLY
    }
    
    private ArrayList<Wall> externalWalls;
    private ArrayList<Room> rooms;
    private WealthIndicatorType wealthIndicator;
    
    public Building() {
        externalWalls = new ArrayList<>();
        rooms = new ArrayList<>();
        wealthIndicator = WealthIndicatorType.WEATHLY;
    }
    
    public ArrayList<Wall> getExternalWalls() {
        return externalWalls;
    }
    
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public void addExternalWall(Wall wall) {
        externalWalls.add(wall);
    }
    
    public void setWealthIndicator(WealthIndicatorType value) {
        wealthIndicator = value;  
    }
    
    public WealthIndicatorType getWealthIndicator() {
        return wealthIndicator;  
    }
    
}
