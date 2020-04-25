/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;
import java.util.ArrayList;
import org.joml.Rectangled;
import org.joml.Vector2i;
import util.CoordSystemHelper;

public class BuildingOutline extends Shape {
    
    ArrayList<Vector2i> points = new ArrayList<>();
    boolean isComplete = false;
    
    public void addPoint(Vector2i point) {
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
     
     public ArrayList<Vector2i> points() {
         return this.points;
     }
     
     public boolean isComplete() {
         return this.isComplete;
     }
     
     public Rectangled boundingRect() {
        Rectangled bounds = new Rectangled();
        boolean firstPoint = true;
        
        if (this.isComplete) {
            
            for (Vector2i point : points) {
                if (firstPoint) {
                    bounds.minX = point.x;
                    bounds.maxX = point.x;
                    bounds.minY = point.y;
                    bounds.maxY = point.x;
                    firstPoint = false;
                } else {
                    bounds.minX = Math.min(bounds.minX, point.x);
                    bounds.maxX = Math.max(bounds.maxX, point.x);
                    bounds.minY = Math.min(bounds.minY, point.y);
                    bounds.maxY = Math.max(bounds.maxY, point.y);
                }
            }
        }
        return bounds;
     }
     
     @Override
     public float[] getPositionData() {
         return CoordSystemHelper.deviceToOpenGLf(points);
     }

     @Override
     public float[] getColourData() {

         float colourData[];
         int i = 0;
         
         colourData = new float[points.size() * 3];   
         //White 255,255,255
         for (Vector2i point : points) {
             colourData[i++] = 1.0f; //R
             colourData[i++] = 1.0f; //G
             colourData[i++] = 1.0f; //B
         }
         return colourData;
     }

     public int numbervertices() {
        return points.size();
     }

     private void checkIsComplete(Vector2i point) {
         
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
}
