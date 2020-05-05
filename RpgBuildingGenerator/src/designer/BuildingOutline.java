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

public class BuildingOutline implements IDesignerComponent {
    
    ArrayList<Point> points = new ArrayList<>();
    boolean isComplete = false;
    boolean enabled = true;
    
    public void addPoint(Point point) {
         // Don't allow further points to be added if outline is a closed polygon
         checkIsComplete(point);
         
         if (points.isEmpty() || this.isComplete == false) {
             points.add(point);
             System.out.printf("Building outline added: %d, %d \n", point.x, point.y);
         }
     }

     public void clear() {
         points.clear();
         this.isComplete = false;
     }

     public int size() {
         return points.size();
     }
     
     public ArrayList<Point> points() {
         return this.points;
     }
     
     public boolean isComplete() {
         return this.isComplete;
     }

     private void checkIsComplete(Point point) {
         
         // Can consider if complete if user has defined more than 3 points 
         // i.e. a triangle with last point equal to first
         if (this.isComplete == false && points.size() >= 3) {
         // Check if last line segment ends at the start point   
            if (points.get(0).equals(point)) {
                this.isComplete = true;
            }             
         }         
         System.out.println("Building outline complete=" + this.isComplete);      
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
        int xPoints[], yPoints[];
        
        if (!enabled || points.isEmpty()) return;
        
        xPoints = new int[points.size()];
        yPoints = new int[points.size()];
     
        int i=0;
        for (Point point : points) {
            xPoints[i] = point.x;
            yPoints[i] = point.y;
            i++;
        }
        if (this.isComplete) {
            g.setColor(new Color(140,140,140));
            g.drawPolygon(xPoints, yPoints, points.size());
        } else {
            g.setColor(Color.WHITE);    
            g.drawPolyline(xPoints, yPoints, points.size());
        }
         
    }
}
