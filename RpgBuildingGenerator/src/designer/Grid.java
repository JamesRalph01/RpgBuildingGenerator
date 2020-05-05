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

/**
 *
 * @author chrisralph
 */
public class Grid implements IDesignerComponent {
    
    static int MAX_GRID_ELEMENTS = 40;
    static int GRID_SPACING = 20;  
    ArrayList<Point> points = new ArrayList<>();
    boolean enabled = true;
    
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
        for (int x=0; x<MAX_GRID_ELEMENTS; x++) {
            for (int y=0; y<MAX_GRID_ELEMENTS; y++){
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
