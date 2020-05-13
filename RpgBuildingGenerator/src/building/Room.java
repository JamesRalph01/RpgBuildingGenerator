/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;
import org.joml.Rectangled;
import org.joml.Vector3f;
import util.Point;
import util.Edge;
import util.PolygonHelper;
/**
 *
 * @author chrisralph
 */
public class Room extends BuildingItem {

    private ArrayList<Edge> edges;
    private float[] colourData;
    private ArrayList<Wall> internalWalls = new ArrayList<>();
    
    public Room(ArrayList<Edge> edges) {
        super();
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
    
    public Edge split(Point pointOnEdge, Edge edgeToAdjust) {
    
        // if the point to adjust is at either end of the edge, leave as is
        // otherwise split the edge in two, but keep aligned aith the other two points
        
        Edge newEdge = null;
        
        if (pointOnEdge.equals(edgeToAdjust.point1()) == false &&
            pointOnEdge.equals(edgeToAdjust.point2()) == false) {
            // 
            int index = this.edges.indexOf(edgeToAdjust);
            
            //Create new edge and keep existing one
            Edge edge1 = new Edge(edgeToAdjust.point1(), pointOnEdge);
            this.edges.add(index, edge1);  
            edgeToAdjust.point1().set(pointOnEdge);
            
            newEdge = edge1;
        }
        return newEdge;
    }
    
    public void adjust(Point currentPoint, Point adjustedPoint) {
        // adjust any matching point to new point across all edges
        for (Edge edge : edges) {
            if (currentPoint.x == edge.point1().x &&
                currentPoint.y == edge.point1().y) {
                edge.point1().set(adjustedPoint.x, adjustedPoint.y);
            } else if (currentPoint.x == edge.point2().x &&
                       currentPoint.y == edge.point2().y) {
                edge.point2().set(adjustedPoint.x, adjustedPoint.y);
            }
        }          
    }

    public ArrayList<Wall> getInternalWalls() {
        return this.internalWalls;
    } 
    
    public void Generate3DPositions(Vector3f screenOrigin) {
        calcLocation(screenOrigin);
        calcInternalWalls(screenOrigin);
    }
   
    private void calcLocation(Vector3f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    
    private void calcInternalWalls(Vector3f screenOrigin) {
        internalWalls.clear();
        for (Edge edge : this.edges) {
            if (edge.isInternal()) {
                Wall wall = new Wall(edge);
                wall.isInternal = true;
                wall.Generate3DPositions(screenOrigin);
                this.internalWalls.add(wall);
            }
        }
    }
    
}