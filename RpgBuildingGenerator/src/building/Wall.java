/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import org.joml.Vector3f;
import util.Edge;

/**
 *
 * @author chrisralph
 */
public class Wall extends BuildingItem {

    private Edge edge;
    
   public Wall(Edge edge, float width, float height) {
       super();
       this.edge = edge;
       generateWall(width, height);
   }
   
   private void generateWall(float width, float height) {
        float Dx, Dy, D;
        float xMin, xMax, yMin, yMax;
                
        Dx = edge.x2() - edge.x1();
        Dy = edge.y2() - edge.y1();
        
        D = (float) Math.sqrt(Dx * Dx + Dy * Dy);
                
        Dx = (float) 0.5 * width * Dx / D;
        Dy = (float) 0.5 * width * Dy / D;

        xMin = Math.min(edge.x1(), edge.x2()) - Math.abs(Dy);
        xMax = Math.max(edge.x1(), edge.x2()) + Math.abs(Dy);
        yMin = Math.min(edge.y1(), edge.y2()) - Math.abs(Dx);
        yMax = Math.max(edge.y1(), edge.y2()) + Math.abs(Dx);
        
   }
   
   
}
