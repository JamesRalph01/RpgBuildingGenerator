/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import util.Rect;
import java.util.ArrayList;
import org.joml.Intersectiond;
import org.joml.Rectangled;
import shapes.Shape;
import shapes.BuildingOutline;
import util.CoordSystemHelper;
import org.joml.Vector2i;
import org.joml.Vector2d;
import org.joml.Vector3d;
import shapes.Room;
import util.Edge;
import util.PolygonHelper;

/**
 *
 * @author chrisralph
 */


public class FloorPlanner extends Shape{

    public enum BuildingType {
        TAVERN, CHURCH, HOUSE
    }
    
    private TreemapLayout algorithm;
    private MapModel mapModel;
    private BuildingType buildingType;
    private boolean activeFloorPlan = false;
    private ArrayList<Vector2i> points = new ArrayList<>();
    PolygonHelper polygonHelper;
    private ArrayList<Room> rooms;
    
    public FloorPlanner() {
        this.buildingType = BuildingType.TAVERN;
    } 
    
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
    
    public void generate(BuildingOutline buildingOutline, int w, int h) {
        
        //Step 1: Find largest rectangle inside user drawn building outline
        polygonHelper = new PolygonHelper(buildingOutline.points());
        Rect bounds = polygonHelper.findLargestRect();
                
        switch (buildingType) {
            case TAVERN:
                mapModel = new TavernMapModel(bounds.w, bounds.h);
                break;
            case CHURCH:
                mapModel = new ChurchMapModel(bounds.w, bounds.h);
                break;
            case HOUSE:
                mapModel = new HouseMapModel(bounds.w, bounds.h);
                break;       
        }
        
        algorithm = new TreemapLayout();
        algorithm.layout(mapModel, bounds);
        
        generateRooms(bounds);
        
        activeFloorPlan = true;
    }
    
    public void clear() {
        activeFloorPlan = false;
    }
    
    public boolean activeFloorPlan() {
        return activeFloorPlan;
    }
 
    /*
    @Override
    public float[] getPositionData() {
 
        Rect bounds;
        Mappable[] items = mapModel.getItems();
        
        points = new ArrayList<>();
        
        int p = 0;
        for (int i=0; i<items.length; i++) {
            bounds = items[i].getBounds();
            
            points.add(new Vector2i((int)bounds.x,(int)bounds.y));
            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y));

            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y));
            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));

            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));
            points.add(new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h));

            points.add(new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h));
            points.add(new Vector2i((int)bounds.x,(int)bounds.y));     
        }
       
        return CoordSystemHelper.deviceToOpenGLf(points);
    } */
    
    @Override
    public float[] getPositionData() {
        
        points = new ArrayList<>();
        
        for (Room room : rooms) {
            for (Edge edge : room.edges()) {
                points.add(edge.point1());
                points.add(edge.point2());           
            }
        
        }
        return CoordSystemHelper.deviceToOpenGLf(points);
    } 
    
    
    @Override
    public float[] getColourData() {

         float colourData[];
         int i = 0;
         
         colourData = new float[points.size() * 3];   
         for (Vector2i point : points) {
            colourData[i++] = 102f/255f;
            colourData[i++] = 224f/255f;
            colourData[i++] = 20f/255f;   
         }
        return colourData;
    }
    
    public int numbervertices() {
        return points.size();
    }
    
    private void generateRooms(Rect overallBounds)
    {
        Rect bounds;
        Mappable[] items = mapModel.getItems();
        
        rooms = new ArrayList<>();
        
        // Convert treemap output into a Rooms collection with edges
        for (int i=0; i<items.length; i++) {
            bounds = items[i].getBounds();
            
            ArrayList<Edge> edges = new ArrayList<>();
           
            edges.add(new Edge(new Vector2i((int)bounds.x,(int)bounds.y),
                               new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y)));
            
            edges.add(new Edge(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y),
                               new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h)));

            edges.add(new Edge(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h),
                               new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h)));

            edges.add(new Edge(new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h),
                               new Vector2i((int)bounds.x,(int)bounds.y)));
            

            
            rooms.add(new Room(edges));
        }
        
        
        // Detect whether edges are internal or external so that we can expand external edges to fit 
        // the building outline polygon      
        for (Room room: rooms) {
            for (Edge edge: room.edges()) {
                // check if this edge shares any other room edge
                // take a partial piece of the edge and check if intersects with another room
                // partial to prevent overlaps where rooms touch
                Edge partial = new Edge(edge);
                partial.shrink(1);
                    
                for (Room roomToCheck: rooms) {    
                    if (roomToCheck != room) {
                        for (Edge edgeToCheck : roomToCheck.edges()) {
                            if (partial.intersets(edgeToCheck)) {
                                edge.connectedEdges().add(edgeToCheck);
                                edge.isInternal(true);
                            }                          
                        }
                    }
                }
            }
        }
        
        // Extend external room edges generated by treemap to match building outline   
        for (Vector2i externalPoint: polygonHelper.points()) {
            
            Edge nearestInternalEdge = null;
            Vector2i nearestPoint = new Vector2i();
            Room nearestRoom = null;
            boolean first = true;
            
            // Check if building outline edge hits an external treemap edge
            // and if more than one, amend the nearest        
            
            for (Room room: rooms) {
                for (Edge roomEdge: room.edges()) {
                    if (roomEdge.isInternal() == false) {
                        
                        Vector3d result = new Vector3d();
                        Intersectiond.findClosestPointOnLineSegment((double)roomEdge.x1(), (double)roomEdge.y1(), 0, 
                                                                    (double)roomEdge.x2(), (double)roomEdge.y2(), 0, 
                                                                    (double)externalPoint.x, (double)externalPoint.y, 0, 
                                                                    result);
                        if (first) {
                            nearestInternalEdge = roomEdge;
                            nearestRoom = room;
                            nearestPoint = new Vector2i((int)result.x, (int)result.y);
                            first = false;
                        } else {
                            // found closer edge?
                            if (externalPoint.distance(new Vector2i((int)result.x, (int)result.y)) < 
                                externalPoint.distance(nearestPoint)) {
                                nearestInternalEdge = roomEdge;
                                nearestRoom = room;            
                                nearestPoint = new Vector2i((int)result.x, (int)result.y);
                            }
                        }
                    }
                }
            }    
            // Adjust room and edge to fit building outline point
            if (nearestRoom != null) nearestRoom.adjust(externalPoint, nearestPoint, nearestInternalEdge);    
        }
        
        // Now extend internal edges that have externally facing end points to a points on the building outline
        for (Room room: rooms) {
            for (Edge edge: room.edges()) {
                if (edge.isInternal()) {
                    for (int i = 0; i < 2; i++) {
                        Vector2i p;
                        if (i==0) {
                            p = edge.point1();
                        }
                        else {
                            p = edge.point2();
                        }
                        // does the edge point lie on the external rect of treemap ?
                        boolean isExternalPoint = false;
                        if (edge.alignment() == Edge.EdgeAlignment.HORIZONTAL) {
                            if (p.x == (int)overallBounds.x || p.x == (int)overallBounds.x+overallBounds.w) {
                                isExternalPoint = true;
                            }
                        } else {
                            if (p.y == (int)overallBounds.y || p.y == (int)overallBounds.y+overallBounds.h) {
                                isExternalPoint = true;
                            }    
                        }
                        if (isExternalPoint) {
                            Edge closestOutsideEdge;
                            Vector2d intersection = new Vector2d();
                            closestOutsideEdge = polygonHelper.closestEdge(p);
                            if (Intersectiond.intersectLineLine(edge.point1().x, edge.point1().y, 
                                                            edge.point2().x, edge.point2().y,
                                                            closestOutsideEdge.point1().x, closestOutsideEdge.point1().y,
                                                            closestOutsideEdge.point2().x, closestOutsideEdge.point2().y,
                                                            intersection)) {
                                // set all points to new position 
                                
                                room.adjust(p, new Vector2i((int)intersection.x, (int)intersection.y));
                                
                            }
                          
                        }   
                    }
                }
            }
        }
        
    }
}

