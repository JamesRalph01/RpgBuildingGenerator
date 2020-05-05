/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Color;
import java.awt.Graphics;
import util.Point;

public class GridCursor implements IDesignerComponent {
    
    private Point position = new Point(0,0);
    boolean enabled = true;
   
    public void cursorPosition(Point point) {
        position = point;
    }
    
    @Override
    public void paint(Graphics g) {
        
        if (!enabled) return;
        
        g.setColor(Color.YELLOW);    // set the drawing color
        g.drawLine(position.x-5, position.y, position.x+5, position.y);
        g.drawLine(position.x, position.y-5, position.x, position.y+5);            
        
    }
    
    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

