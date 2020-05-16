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
public class Building extends BuildingItem {

    
    private ArrayList<Wall> externalWalls;
    private ArrayList<Room> rooms;
    private int wealthIndicator;
    
    public Building() {
        super();
        externalWalls = new ArrayList<>();
        rooms = new ArrayList<>();
        wealthIndicator = 50;
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
    
    public void setWealthIndicator(int wealthIndicator) {
        this.wealthIndicator = wealthIndicator;  
    }
    
    public int getWealthIndicator() {
        return wealthIndicator;  
    }
    
    public void Generate3DPositions() {
    
        // External walls - One texture for all external walls
        String externalWallTexture = chooseExternalWallTexture();
        this.externalWalls.forEach((wall) -> {
            wall.Generate3DPositionsExternal(this.getLocation(), externalWallTexture);
        });
        
        // Rooms
        this.rooms.forEach((room) -> {
            room.Generate3DPositions(this.getLocation(), this.wealthIndicator);
        });
    }
    
        
    private String chooseExternalWallTexture() {
        if (this.wealthIndicator <= 50) {
            if (Math.random() < 0.5) {
                return "stone_wall2.png";                        
            } else {
                return "stone_wall.png";     
            }
        } else {
            if (Math.random() < 0.5) {
                return "brick_wall.png";                        
            } else {
                return "brick_wall2.png";     
            }
        }
    }
    
}
