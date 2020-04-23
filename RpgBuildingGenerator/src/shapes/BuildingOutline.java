/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;
import java.util.ArrayList;
import org.joml.Vector2f;

public class BuildingOutline extends Shape {
    
    ArrayList<Vector2f> points = new ArrayList<>();
    Vector2f cursorLocation;
    boolean isComplete = false;
    
    public void addPoint(Vector2f point) {
         // Don't allow further points to be added if outline is a closed polygon
         checkIsComplete(point);
         
         if (points.isEmpty() || this.isComplete == false) {
             points.add(point);
             System.out.printf("Building outline added: %.2f, %.2f \n", point.x, point.y);
         }
     }

     public void clear() {
         points.clear();
         this.isComplete = false;
     }

     public int size() {
         return points.size();
     }

     public void setCursorLocation(Vector2f point) {
         cursorLocation = point;
     }
     
     public ArrayList<Vector2f> points() {
         return this.points;
     }
     
     public boolean isComplete() {
         return this.isComplete;
     }
     
     @Override
     public float[] getPositionData() {

         float positionData[];

         // If we're actively defining, then add last 'indicator' line
         if (this.isComplete) {
             positionData = new float[points.size() * 3];    
         } else {
             positionData = new float[(points.size()+ 1) * 3];       
         }

         int i=0;
         for (Vector2f point : points) {
             positionData[i++] = point.x;
             positionData[i++] = point.y;
             positionData[i++] = 1.0f;  
         }
           // If we're actively defining, then add last 'indicator' line
         if (this.isComplete == false) {
             System.out.println("Adding selecton line");  
             positionData[i++] = cursorLocation.x;
             positionData[i++] = cursorLocation.y;
             positionData[i++] = 1.0f;            
         }      
         return positionData;
     }

     @Override
     public float[] getColourData() {

         float colourData[];

         // If we're actively defining, then add last 'indicator' line
         if (this.isComplete) {
             colourData = new float[points.size() * 3];   
         } else {
             colourData = new float[(points.size() + 1) * 3];
         }
         int i=0;
         for (Vector2f point : points) {
             colourData[i++] = 1.0f; //R
             colourData[i++] = 1.0f; //G
             colourData[i++] = 1.0f; //B
         }
         if (this.isComplete == false) {
             colourData[i++] = 1.0f/50.0f; //R
             colourData[i++] = 1.0f/168.0f; //G
             colourData[i++] = 1.0f/82.0f; //B           
         } 
         return colourData;
     }

     public int numbervertices() {
         if (this.isComplete) {
             return points.size();
         } else
         {
             return points.size() + 1; // additional point for moving cursor
         }
     }

     private void checkIsComplete(Vector2f point) {
         
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

     private boolean isPolygon() {
         return false;
     }
     
     
}

