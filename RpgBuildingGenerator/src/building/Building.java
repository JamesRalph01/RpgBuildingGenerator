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
    private floorplanner.FloorPlanner.BuildingTheme buildingTheme;
    
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
    
    public void setBuildingTheme(floorplanner.FloorPlanner.BuildingTheme theme) {
        this.buildingTheme = theme;
    }
    
    public floorplanner.FloorPlanner.BuildingTheme getBuildingTheme() {
        return this.buildingTheme;
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
        
        String folder = "external_walls/";
        
        switch (this.buildingTheme) {
            case MEDIEVAL:
                System.out.println(this.buildingTheme + "  :  "+this.wealthIndicator);
                if (this.wealthIndicator <= 15) {                                   // STRAW
                    return folder + "Straw_wall1.jpg"; 
                }
                else if (this.wealthIndicator > 15 && this.wealthIndicator <= 35) { // WOOD (POOR)
                    return folder + "Wood_wall1.png"; 
                }
                else if (this.wealthIndicator > 35 && this.wealthIndicator <= 50) { // WOOD (RICH)
                    if (Math.random() < 0.5) {                                      // STONE (POOR)
                        return folder + "Wood_wall2.png";
                    }
                    else {                                    
                        return folder + "Stone_wall2.png";
                    }
                }
                else if (this.wealthIndicator > 50 && this.wealthIndicator <= 70) { // WOOD (RICH)
                    double n = Math.random();                                       // STONE (RICH)
                    if (n <= 0.4) {                                                 
                        return folder + "Wood_wall2.png";
                    }
                    else {
                        return folder + "Stone_wall.png";
                    }
                }
                else if (this.wealthIndicator > 70 && this.wealthIndicator <= 85) { // LIMESTONE
                    double n = Math.random();                                       // STONE (RICH)
                    if (n <= 0.4) {                                                 // MARBLE
                        return folder + "limestone_wall.png";
                    }
                    else if (n > 0.4 && n <= 0.7){
                        return folder + "Stone_wall.png";
                    }
                    else {
                        return folder + "Marble_wall.png";
                    }
                }
                else {                                                              // MARBLE
                    if (Math.random() < 0.6) {                                      // LIMESTONE
                        return folder + "limestone_wall.png";
                    }
                    else {                                    
                        return folder + "Marble_wall.png";
                    }
                }   
            case MODERN:
                if (Math.random() < 0.5) {
                    return folder + "brick_wall.png";                        
                } else {
                    return folder + "brick_wall2.png";     
                }
            default: // FUTURISTIC
                return folder + "Metal_wall.png"; 
        }
                
    }
}
