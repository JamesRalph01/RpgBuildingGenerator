/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import java.util.ArrayList;
import building.Room;
import util.Edge;
import util.Point;


public class EdgeAdjustment {
    
    public Room room;
    public Edge edge;
    public ArrayList<Point> movePoints;
    
    public EdgeAdjustment(Room room, Edge edge) {
        this.room = room;
        this.edge = edge;
        this.movePoints = new ArrayList<>();  
    }
    
}
