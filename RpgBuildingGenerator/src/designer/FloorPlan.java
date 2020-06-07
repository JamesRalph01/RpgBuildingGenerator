/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import building.Building;
import building.BuildingItem;
import building.Room;
import building.furniture.Door;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import util.Edge;
import util.Point;

public class FloorPlan implements IDesignerComponent {

    private boolean enabled = true;
    ArrayList<Edge> walls = new ArrayList<>();
    HashMap<String, Point> labels = new HashMap<>();
    Building building;
    
    
    public void addEdge(Edge edge) {
        walls.add(edge);
    }
    
    public void setLabels(HashMap<String, Point> labels) {
        this.labels = labels;
    }
    
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
        
        if (!enabled || walls.isEmpty()) return;
        
        // Draw all walls
        for (Edge wall : walls) {
            g.setColor(wall.point1().getColour());
            g.drawLine(wall.point1().x, wall.point1().y, wall.point2().x, wall.point2().y);          
        }
        
        // Room labels
        g.setColor(Color.lightGray);
        Set set = this.labels.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            
            Point point = (Point) mentry.getValue();
            String label = (String) mentry.getKey();
            g.drawString(label, point.x, point.y);
        } 
        
        //Front door
        g.setColor(Color.ORANGE);
        Point frontDoor = this.building.getFrontDoorPosition();
        g.drawOval(frontDoor.x, frontDoor.y, 15, 15);
        
        for (Room room: this.building.getRooms()) {
            //external room walls only if debugging
            for (Edge edge: room.edges()) {
                if (edge.isInternal() == false) {
                    g.setColor(Color.WHITE);
                    //g.drawLine(edge.x1(),edge.y1(), edge.x2(), edge.y2());                       
                }          
            }
            g.setColor(Color.WHITE);
            g.drawLine(this.building.internalDoorEdge.x1(),this.building.internalDoorEdge.y1(), 
                       this.building.internalDoorEdge.x2(), this.building.internalDoorEdge.y2());                       
            
            g.setColor(Color.RED);
            g.drawOval(this.building.midInternalDoorEdge.x, this.building.midInternalDoorEdge.y, 10, 10);   
            
            //Internal doors    
            g.setColor(Color.GREEN);
            for (BuildingItem item: room.getFurniture()) {
                if (item instanceof Door ) {
                    g.drawOval((int)item.getLocation().x, (int)item.getLocation().z, 15, 15);                  
                }
            }
            
            //Remove! Bounding box for checking wall intersections
            g.setColor(Color.PINK);
            int w = (int) this.building.extRoom.maxX - (int) this.building.extRoom.minX;
            int h = (int) this.building.extRoom.maxY - (int) this.building.extRoom.minY;
            g.drawRect((int)this.building.extRoom.minX, (int)this.building.extRoom.minY, w,h);
        }
        
        // Chosen external wall for front door
        g.setColor(Color.YELLOW);
        g.drawLine(building.externalDoorEdge.x1() ,building.externalDoorEdge.y1(), 
                   building.externalDoorEdge.x2(), building.externalDoorEdge.y2());  

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(building.externalDoorCalcEdge.x1() ,building.externalDoorCalcEdge.y1(), 
                   building.externalDoorCalcEdge.x2(), building.externalDoorCalcEdge.y2());  

    }    
}
