/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import util.Point;


public class Grid implements IDesignerComponent {
    
    static int GRID_SPACING = 20;  
    ArrayList<Point> points = new ArrayList<>();
    private boolean enabled = true;
    
    public Grid()
    {
        initPositionData();
    }
    
    public Point getNearestGridPoint(Point pointToTest) {
        Point nearestGridPoint = new Point(32000,32000);
        for (Point point : points) {
            if (pointToTest.distance(point) < pointToTest.distance(nearestGridPoint)) {
                nearestGridPoint = point;

            }            
        };
        /*System.out.printf("Point to check: %d, %d \n", pointToTest.x, pointToTest.y);
        System.out.printf("Nearest point: %d, %d \n", nearestGridPoint.x, nearestGridPoint.y);
        System.out.printf("distance: %f \n", pointToTest.distance(nearestGridPoint)); */
        return nearestGridPoint;
    }
    
    private void initPositionData() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        for (int x=0; x < screenSize.width / GRID_SPACING; x++) {
            for (int y=0; y< screenSize.height / GRID_SPACING; y++){
                points.add(new Point(GRID_SPACING+x*GRID_SPACING,GRID_SPACING+y*GRID_SPACING));
            }
        }
    }


    @Override
    public void paint(Graphics g) {
        
        if (!enabled) return;
        
        g.setColor(Color.WHITE);    // set the drawing color

        for (Point point : this.points) {
            g.drawLine(point.x, point.y, point.x, point.y);
        }  
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
