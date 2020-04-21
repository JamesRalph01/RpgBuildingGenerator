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

         if (points.isEmpty() || this.isActive()) {
             points.add(point);
             System.out.printf("Building outline added: %.2f, %.2f \n", point.x, point.y);        
         }
     }

     public void clear() {
         points.clear();
         isComplete = false;
     }

     public int size() {
         return points.size();
     }

     public void setCursorLocation(Vector2f point) {
         cursorLocation = point;
     }

     @Override
     public float[] getPositionData() {

         float positionData[];

         // If we're actively defining, then add last 'indicator' line
         if (this.isActive()) {
             positionData = new float[(points.size()+ 1) * 3];
         } else {
             positionData = new float[points.size() * 3];           
         }

         int i=0;
         for (Vector2f point : points) {
             positionData[i++] = point.x;
             positionData[i++] = point.y;
             positionData[i++] = 1.0f;  
         }
           // If we're actively defining, then add last 'indicator' line
         if (this.isActive()) {
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
         if (this.isActive()) {
             colourData = new float[(points.size() + 1) * 3];
         } else {
             colourData = new float[points.size() * 3];           
         }
         int i=0;
         for (Vector2f point : points) {
             colourData[i++] = 1.0f; //R
             colourData[i++] = 1.0f; //G
             colourData[i++] = 1.0f; //B
         }
         if (this.isActive()) {
             colourData[i++] = 1.0f/50.0f; //R
             colourData[i++] = 1.0f/168.0f; //G
             colourData[i++] = 1.0f/82.0f; //B           
         } 
         return colourData;
     }

     public int numbervertices() {
         if (this.isActive()) {
             return points.size() + 1;
         } else
         {
             return points.size();
         }
     }

     private boolean isActive() {
         boolean active = true;
         // return True if user is actively defining building outline
         if (points.isEmpty()) {
             active = false;
         } else if (points.size() > 2 && this.isComplete()) {
             active = false;
         }
         System.out.println("Building outline active=" + active);      
         return active;
     }

     private boolean isComplete() {
         
         if (this.isComplete == false) {
         // Check if last line segment ends at the start point   
            if (points.get(0).equals(points.get(points.size() -1))) {
                this.isComplete = true;
            }             
         }
         
         System.out.println("Building outline complete=" + this.isComplete);      
         return this.isComplete;       
     }

     private boolean isPolygon() {
         return false;
     }
}

