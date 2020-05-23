/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Graphics;
import java.util.ArrayList;
import util.Edge;

/**
 *
 * @author chrisralph
 */
public class FloorPlan implements IDesignerComponent {

    private boolean enabled = true;
    ArrayList<Edge> walls = new ArrayList<>();
    
    public void addEdge(Edge edge) {
        walls.add(edge);
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
    }    
}
