/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import shapes.Room;
import util.Edge;
import util.Point;

/**
 *
 * @author chrisralph
 */
public class EdgeAdjustment {
    
    public Room room;
    public Edge edge;
    public Point splitPoint;
    public Point movePoint;
    
    public EdgeAdjustment(Room room, Edge edge, Point splitPoint, Point movePoint) {
        this.room = room;
        this.edge = edge;
        this.splitPoint = splitPoint;
        this.movePoint = movePoint;
    }
    
}
