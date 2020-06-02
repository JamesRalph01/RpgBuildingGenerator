/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import util.Edge;
import util.Point;

/**
 *
 * @author chrisralph
 */
public class FloorPlan implements IDesignerComponent {

    private boolean enabled = true;
    ArrayList<Edge> walls = new ArrayList<>();
    HashMap<String, Point> labels = new HashMap<>();
    
    
    public void addEdge(Edge edge) {
        walls.add(edge);
    }
    
    public void setLabels(HashMap<String, Point> labels) {
        this.labels = labels;
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
    }    
}
