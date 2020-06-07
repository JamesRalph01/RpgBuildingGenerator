/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import building.Building;
import building.BuildingItem;
import building.Room;
import building.Wall;
import building.furniture.Door;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import util.Edge;
import util.Point;

public class FloorPlan implements IDesignerComponent {

    private boolean enabled = true;
    Building building;
    
    public void setBuilding(Building building) {
        this.building = building;
    }
    
    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void paint(Graphics g) {
        
        if (!enabled || this.building == null) return;
        
        // Draw all external walls
        for (Wall wall : this.building.getExternalWalls()) {
            Edge edge = wall.getEdge();
            g.setColor(Color.GREEN);
            g.drawLine(edge.point1().x, edge.point1().y, edge.point2().x, edge.point2().y);         
        }
        
        // Room
        g.setColor(Color.lightGray);
        for (Room room: this.building.getRooms())
        {
            for (Wall wall: room.getInternalWalls()) {
                Edge edge = wall.getEdge();
                g.setColor(edge.point1().getColour());
                g.drawLine(edge.point1().x, edge.point1().y, edge.point2().x, edge.point2().y);   
            }
            Point point = room.centreOf();
            String label = room.RoomTypeText[room.getRoomType().ordinal()];
            g.drawString(label, point.x, point.y);
        } 
        
        //Front door
        g.setColor(Color.ORANGE);
        Point frontDoor = this.building.getFrontDoorPosition();
        g.drawOval(frontDoor.x, frontDoor.y, 15, 15);
        
        for (Room room: this.building.getRooms()) {
            //external room walls only if debugging
//            for (Edge edge: room.edges()) {
//                if (edge.isInternal() == false) {
//                    g.setColor(Color.WHITE);
//                    g.drawLine(edge.x1(),edge.y1(), edge.x2(), edge.y2());                       
//                }          
//            }
//            g.setColor(Color.WHITE);
//            g.drawLine(this.building.internalDoorEdge.x1(),this.building.internalDoorEdge.y1(), 
//                       this.building.internalDoorEdge.x2(), this.building.internalDoorEdge.y2());                       
//            
//            g.setColor(Color.RED);
//            g.drawOval(this.building.midInternalDoorEdge.x, this.building.midInternalDoorEdge.y, 10, 10);   
            
            //Internal doors    
            g.setColor(Color.GREEN);
            for (BuildingItem item: room.getFurniture()) {
                if (item instanceof Door ) {
                    g.drawOval((int)item.getLocation().x, (int)item.getLocation().z, 15, 15);                  
                }
            }
            
            //Remove! Bounding box for checking wall intersections
//            g.setColor(Color.PINK);
//            int w = (int) this.building.extRoom.maxX - (int) this.building.extRoom.minX;
//            int h = (int) this.building.extRoom.maxY - (int) this.building.extRoom.minY;
//            g.drawRect((int)this.building.extRoom.minX, (int)this.building.extRoom.minY, w,h);
        }
        
        // Chosen external wall for front door
//        g.setColor(Color.YELLOW);
//        g.drawLine(building.externalDoorEdge.x1() ,building.externalDoorEdge.y1(), 
//                   building.externalDoorEdge.x2(), building.externalDoorEdge.y2());  
//
//        g.setColor(Color.LIGHT_GRAY);
//        g.drawLine(building.externalDoorCalcEdge.x1() ,building.externalDoorCalcEdge.y1(), 
//                   building.externalDoorCalcEdge.x2(), building.externalDoorCalcEdge.y2());  

    }    
}
