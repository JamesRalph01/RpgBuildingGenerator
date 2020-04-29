/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.ArrayList;
import org.joml.Rectangled;
import util.Point;
import util.CoordSystemHelper;
import util.Edge;
import util.PolygonHelper;
/**
 *
 * @author chrisralph
 */
public class Room extends Shape {

    private ArrayList<Edge> edges;
    private float[] colourData;
    
    public Room(ArrayList<Edge> edges) {
        this.edges = edges;
    }
    
    public ArrayList<Edge> edges() {
        return this.edges;
    }
    
    public ArrayList<Point> points() {
        ArrayList<Point> points = new ArrayList<>();
        for (Edge edge : edges) {
            if (points.contains(edge.point1()) == false) points.add(edge.point1());
            if (points.contains(edge.point2()) == false) points.add(edge.point2());
        }
        return points;
    }
    
    public Rectangled bounds() {
        PolygonHelper polygon;
        polygon = new PolygonHelper(this.points());
        return polygon.boundingRect();
    }
    
    @Override
    public float[] getPositionData() {
        return CoordSystemHelper.deviceToOpenGLf(this.points());
    }

    @Override
    public float[] getColourData() {
        return this.colourData;   
    }
    
    public int numbervertices() {
        return this.points().size();
    }

    private void initColourData() {
        int i = 0;

        // Green 50, 168, 82
        colourData = new float[this.points().size() * 3]; 
        // pink 245, 66, 230
        for (Point point : this.points()) {
            colourData[i++] = 245f/255f;
            colourData[i++] = 66f/255f;
            colourData[i++] = 230f/255f;   
        }
    }
    
    public void adjust(Point adjustmentPoint, Point pointOnEdge, Edge edgeToAdjust) {
    
        // if the point to adjust is at either end of the edge, leave as is
        // otherwise split the edge in two
        if (pointOnEdge.equals(edgeToAdjust.point1()) == false &&
            pointOnEdge.equals(edgeToAdjust.point2()) == false) {
            // 
            int index = this.edges.indexOf(edgeToAdjust);
            this.edges.remove(edgeToAdjust);
            
            //Create new edges
            Edge edge1 = new Edge(edgeToAdjust.point1(), adjustmentPoint);
            Edge edge2 = new Edge(adjustmentPoint, edgeToAdjust.point2());
            
            this.edges.add(index, edge2);
            this.edges.add(index, edge1);  
        }
    }
    
    public void adjust(Point currentPoint, Point adjustedPoint) {
        // adjust any matching point to new point across all edges
        for (Edge edge : edges) {
            if (currentPoint.equals(edge.point1())) {
                edge.point1().set(adjustedPoint.x, adjustedPoint.y);
            } else if (currentPoint.equals(edge.point2())) {
                edge.point2().set(adjustedPoint.x, adjustedPoint.y);
            }
        }
                
    }
    
}
