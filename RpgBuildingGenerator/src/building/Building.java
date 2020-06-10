/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import building.Room.RoomType;
import java.util.ArrayList;
import org.joml.Intersectiond;
import org.joml.Rectangled;
import org.joml.Vector2d;
import org.joml.Vector3d;
import util.Edge;
import util.Edge.EdgeAlignment;
import util.Point;


public class Building extends BuildingItem {

    private ArrayList<Wall> externalWalls;
    private ArrayList<Room> rooms;
    private int wealthIndicator;
    private floorplanner.FloorPlanner.BuildingTheme buildingTheme;
    private Floor floor;
    
    //remove
    public Point midInternalDoorEdge;
    public Rectangled extRoom;
    public Edge externalDoorEdge;
    public Edge internalDoorEdge;
    public Edge externalDoorCalcEdge;
    
    public Edge frontDoorEdge;
    
    
    public Building() {
        super();
        externalWalls = new ArrayList<>();
        rooms = new ArrayList<>();
        floor = new Floor();
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
    
    public Floor getFloor() {
        return floor;
    }
    
    public Point getFrontDoorPosition() {
        Room livingRoom = null;
        Edge externalRoomEdge = null;
        Point mid;
        
        // Locate Living Room
        for (Room room: this.rooms) {
            if (room.getRoomType() == RoomType.LivingRoom || 
                room.getRoomType() == RoomType.TavernFloor) {
                livingRoom = room;
                break;
            }
        }
        
        // Living room should have one external wall
        for (Edge edge: livingRoom.edges()) {
            if (edge.isInternal() == false) {
                externalRoomEdge = edge;
                break;
            }
        }
        
        //Remove
        internalDoorEdge = externalRoomEdge;
        
        // Get mid point of external living room edge
        mid = externalRoomEdge.getMidPoint();
        midInternalDoorEdge = mid;
        
        // Find closest external wall
        Double closestEdge = Double.MAX_VALUE;
        Wall closestExternalWall = null;
        for (Wall wall: this.externalWalls) {
            Double distance;
            distance = Math.abs(Intersectiond.distancePointLine(mid.x, mid.y, 
                                            wall.getEdge().x1(), wall.getEdge().y1(), 
                                            wall.getEdge().x2(), wall.getEdge().y2()));
            if (distance < closestEdge) {
                closestEdge = distance;
                closestExternalWall = wall;
            }
        }
        
        this.externalDoorEdge = closestExternalWall.getEdge();
        
        // Find points of external wall that intersect with living room
        Point p1start, p1end, p2start, p2end;
        Rectangled roomExtendedRect;
        if (externalRoomEdge.getOriginalAlignment() == EdgeAlignment.HORIZONTAL) {
            p1start = new Point(externalRoomEdge.x1(), 0);
            p1end = new Point(externalRoomEdge.x1(), Integer.MAX_VALUE);
            p2start = new Point(externalRoomEdge.x2(), 0);
            p2end = new Point(externalRoomEdge.x2(), Integer.MAX_VALUE);
            
            roomExtendedRect = new Rectangled(Math.min(externalRoomEdge.x1(), externalRoomEdge.x2()),
                                              0,
                                              Math.max(externalRoomEdge.x1(), externalRoomEdge.x2()),
                                              Integer.MAX_VALUE);
            
        } else {
            p1start = new Point(0, externalRoomEdge.y1());
            p1end = new Point(Integer.MAX_VALUE, externalRoomEdge.y1());
            p2start = new Point(0, externalRoomEdge.y2());
            p2end = new Point(Integer.MAX_VALUE, externalRoomEdge.y2());  
            
            roomExtendedRect = new Rectangled(0,
                                              Math.min(externalRoomEdge.y1(), externalRoomEdge.y2()),
                                              Integer.MAX_VALUE,
                                              Math.max(externalRoomEdge.y1(), externalRoomEdge.y2()));
        }
        
        //Remove
        this.extRoom = roomExtendedRect;
        
        Vector2d p1intersect = new Vector2d();
        Vector2d p2intersect = new Vector2d();
        boolean p1intersects = false;
        boolean p2intersects = false;
        if (Intersectiond.intersectLineLine(p1start.x, p1start.y, p1end.x, p1end.y, 
                                        closestExternalWall.getEdge().x1(), closestExternalWall.getEdge().y1(), 
                                        closestExternalWall.getEdge().x2(), closestExternalWall.getEdge().y2(), p1intersect)) {
            p1intersects = true;
        }
        if (Intersectiond.intersectLineLine(p2start.x, p2start.y, p2end.x, p2end.y, 
                                        closestExternalWall.getEdge().x1(), closestExternalWall.getEdge().y1(), 
                                        closestExternalWall.getEdge().x2(), closestExternalWall.getEdge().y2(), p2intersect)) {
            p2intersects = true;
        }
        
        Vector3d p1 = new Vector3d();
        Vector3d p2 = new Vector3d();
        Intersectiond.findClosestPointOnLineSegment(
                            (double) closestExternalWall.getEdge().x1(), (double) closestExternalWall.getEdge().y1(), 0,
                            (double) closestExternalWall.getEdge().x2(), (double) closestExternalWall.getEdge().y2(), 0,
                            p1intersect.x, p1intersect.y, 0, p1);
        Intersectiond.findClosestPointOnLineSegment(
                            (double) closestExternalWall.getEdge().x1(), (double) closestExternalWall.getEdge().y1(), 0,
                            (double) closestExternalWall.getEdge().x2(), (double) closestExternalWall.getEdge().y2(), 0,
                            p2intersect.x, p2intersect.y, 0, p2);
          // Front door midpoint placement is middle of our calculated edge
        frontDoorEdge = new Edge(new Point(p1.x,p1.y), new Point(p2.x, p2.y));
        Point doorPosition = frontDoorEdge.getMidPoint();
        externalDoorCalcEdge = frontDoorEdge;
        return doorPosition;  
       
    } 
    
    public void Generate3DPositions() {
    
        //Floor
        this.floor.Generate3DPositionsInternal(this.getLocation(), this.buildingTheme, this.wealthIndicator);
        
        // External walls - One texture for all external walls
        String externalWallTexture = chooseExternalWallTexture();
        this.externalWalls.forEach((wall) -> {
            wall.Generate3DPositionsExternal(this.getLocation(), externalWallTexture);
        });
        
        // Rooms
        this.rooms.forEach((room) -> {
            room.Generate3DPositions(this.getLocation(), this.wealthIndicator, this.buildingTheme);
        });
    }
    
        
    private String chooseExternalWallTexture() {
                
        switch (this.buildingTheme) {
            case MEDIEVAL:
                System.out.println(this.buildingTheme + "  :  "+this.wealthIndicator);
                if (this.wealthIndicator <= 15) {                                   // STRAW
                    return "Straw_wall1.jpg"; 
                }
                else if (this.wealthIndicator > 15 && this.wealthIndicator <= 35) { // WOOD (POOR)
                    return "Wood_wall1.png"; 
                }
                else if (this.wealthIndicator > 35 && this.wealthIndicator <= 50) { // WOOD (RICH)
                    if (Math.random() < 0.5) {                                      // STONE (POOR)
                        return "Wood_wall2.png";
                    }
                    else {                                    
                        return "Stone_wall2.png";
                    }
                }
                else if (this.wealthIndicator > 50 && this.wealthIndicator <= 70) { // WOOD (RICH)
                    double n = Math.random();                                       // STONE (RICH)
                    if (n <= 0.4) {                                                 
                        return "Wood_wall2.png";
                    }
                    else {
                        return "Stone_wall.png";
                    }
                }
                else if (this.wealthIndicator > 70 && this.wealthIndicator <= 85) { // LIMESTONE
                    double n = Math.random();                                       // STONE (RICH)
                    if (n <= 0.4) {                                                 // MARBLE
                        return "limestone_wall.png";
                    }
                    else if (n > 0.4 && n <= 0.7){
                        return "Stone_wall.png";
                    }
                    else {
                        return "Marble_wall.png";
                    }
                }
                else {                                                              // MARBLE
                    if (Math.random() < 0.6) {                                      // LIMESTONE
                        return "limestone_wall.png";
                    }
                    else {                                    
                        return "Marble_wall.png";
                    }
                }   
            case MODERN:
                if (Math.random() < 0.5) {
                    return "brick_wall.png";                        
                } else {
                    return "brick_wall2.png";     
                }
            default: // FUTURISTIC
                return "Metal_wall.png"; 
        }
                
    }
}
