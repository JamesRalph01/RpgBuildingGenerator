/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import util.Point;


public class EditCursorLine implements IDesignerComponent {

    private Point fromPoint, toPoint;
    private boolean enabled = false;
    
    
    public EditCursorLine()
    {
        fromPoint = new Point(0,0);
        toPoint = new Point(0,0);
    }
    
    
    public void fromPoint(Point point) {
        fromPoint.set(point);
    }
    
    public void ToPoint(Point point) {
        toPoint.set(point);
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
        
        if (!enabled) return;
        
        g.setColor(new Color(252, 186, 3));
        g.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);      
    }
}
