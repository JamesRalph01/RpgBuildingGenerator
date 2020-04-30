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
import org.joml.Vector2d;
import org.joml.Vector3d;
import shapes.Room;
import util.Edge;
import util.PolygonHelper;
import util.CoordSystemHelper;
import util.Point;
/**
 *
 * @author chrisralph
 */


public class FloorPlanner extends Shape{

    public enum BuildingType {
        TAVERN, CHURCH, HOUSE, 
        TEST // internal use only for repeatable results
    }
    
    private TreemapLayout algorithm;
    private MapModel mapModel;
    private BuildingType buildingType;
    private boolean activeFloorPlan = false;
    private ArrayList<Point> points = new ArrayList<>();
    PolygonHelper polygonHelper;
    private ArrayList<Room> rooms;
    private int indexColourStart = 0;
    
    private boolean DEBUG = false;
    
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
            case TEST:
                mapModel = new TestMapModel(bounds.w, bounds.h);
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
 
   
    @Override
    public float[] getPositionData() {
        
        points = new ArrayList<>();
        
        // Final 'fitted' layout
        if (DEBUG == false) {
            //Room room = rooms.get(1);
            for (Room room : rooms) {
                for (Edge edge : room.edges()) {
                    points.add(edge.point1());
                    points.add(edge.point2());           
                }

            }
        }
        indexColourStart = points.size();
        
        if (DEBUG) {
            // Treemap output
            Rect bounds;
            Mappable[] items = mapModel.getItems();
            for (int i=0; i<items.length; i++) {
                bounds = items[i].getBounds();

                points.add(new Point((int)bounds.x,(int)bounds.y));
                points.add(new Point((int)bounds.x+(int)bounds.w,(int)bounds.y));

                points.add(new Point((int)bounds.x+(int)bounds.w,(int)bounds.y));
                points.add(new Point((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));

                points.add(new Point((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));
                points.add(new Point((int)bounds.x,(int)bounds.y+(int)bounds.h));

                points.add(new Point((int)bounds.x,(int)bounds.y+(int)bounds.h));
                points.add(new Point((int)bounds.x,(int)bounds.y));     
            }           
        }
 
        
        return CoordSystemHelper.deviceToOpenGLf(points);
    } 
    
    
    @Override
    public float[] getColourData() {

        float colourData[];
        int i = 0;
         
        colourData = new float[points.size() * 3];
         
        Mappable[] items = mapModel.getItems(); 

        // Final 'fitted' layout Pink! 255, 0, 255
        for (Point point : points) {
            // Treemap output
            if (DEBUG && i > (indexColourStart * 3)) {
                colourData[i++] = 0f/255f;
                colourData[i++] = 255f/255f;
                colourData[i++] = 0f/255f;                
            } else {
                colourData[i++] = 255f/255f;
                colourData[i++] = 0f/255f;
                colourData[i++] = 255f/255f;                  
            }
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
        
        System.out.printf("Treemap bounds x1: %f, y1: %f, x2: %f, y2: %f\n", 
                overallBounds.x, overallBounds.y, 
                overallBounds.x+overallBounds.w, overallBounds.y+overallBounds.h);
        
        rooms = new ArrayList<>();
        
        // Convert treemap output into a Rooms collection with edges
        for (int i=0; i<items.length; i++) {
            bounds = items[i].getBounds();
            
            ArrayList<Edge> edges = new ArrayList<>();
           
            edges.add(new Edge(new Point(bounds.x,bounds.y),
                               new Point(bounds.x+bounds.w,bounds.y), overallBounds));
            
            
            edges.add(new Edge(new Point(bounds.x+bounds.w,bounds.y),
                               new Point(bounds.x+bounds.w,bounds.y+bounds.h), overallBounds));

            edges.add(new Edge(new Point(bounds.x+bounds.w,bounds.y+bounds.h),
                               new Point(bounds.x,bounds.y+bounds.h), overallBounds));

            edges.add(new Edge(new Point(bounds.x,bounds.y+bounds.h),
                               new Point(bounds.x,bounds.y), overallBounds));
            
            rooms.add(new Room(edges));
        }
        System.out.println("After Step 1: Treemap load");
        printAllPoints();
        
        
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
        System.out.println("After Step 2: internal/external edge detection");
        printAllPoints();

                
        // Now extend internal edges that have externally facing end points to a points on the building outline
        for (Room room: rooms) {
            
            for (Edge edge: room.edges()) {

                for (int i = 0; i < 2; i++) {
                    
                    Point p;
                    if (i==0) {
                        p = new Point(edge.point1()); // take a copy to avoid changing original
                    }
                    else {
                        p = new Point(edge.point2());
                    }

                    if (p.scope == Point.Scope.EXTERNAL) {
                        Edge closestOutsideEdge;
                        Vector2d intersection = new Vector2d();
                        closestOutsideEdge = polygonHelper.closestEdge(p);
                        if (Intersectiond.intersectLineLine(edge.point1().x, edge.point1().y, 
                                                            edge.point2().x, edge.point2().y,
                                                            closestOutsideEdge.point1().x, closestOutsideEdge.point1().y,
                                                            closestOutsideEdge.point2().x, closestOutsideEdge.point2().y,
                                                            intersection)) {
                            // set all matching points to new position 
                            for (Room bRoom: rooms) {
                                bRoom.adjust(p, new Point(intersection.x, intersection.y));    
                            }
 
                        }

                    }   
                }
                
            }
        }
        System.out.println("After Step 4: expand internal edges with one external point");
        printAllPoints();
        
        
        // Extend external room edges generated by treemap to match building outline   
        for (Point externalPoint: polygonHelper.points()) {
            
            Edge nearestInternalEdge = null;
            Point nearestPoint = new Point();
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
                            nearestPoint = new Point((int)result.x, (int)result.y);
                            first = false;
                        } else {
                            // found closer edge?
                            if (externalPoint.distance(new Point((int)result.x, (int)result.y)) < 
                                externalPoint.distance(nearestPoint)) {
                                nearestInternalEdge = roomEdge;
                                nearestRoom = room;            
                                nearestPoint = new Point((int)result.x, (int)result.y);
                                
                            }
                        }
                    }
                }
            }    
            // Adjust room and edge to fit building outline point
            if (nearestRoom != null) nearestRoom.adjust(externalPoint, nearestPoint, nearestInternalEdge);    
        }
        System.out.println("After Step 3: expand external edges");
        printAllPoints();

        
    }
    
    private void printAllPoints() {
        for (Room room: rooms) {
            System.out.println("Room");
            for (Edge edge: room.edges()) {
                System.out.printf("Edge isInternal: %s\n", edge.isInternal());
                System.out.printf("x1: %d, y1: %d, scope1: %s -> x2: %d, y2: %d, scope2: %s \n", 
                        edge.point1().x, edge.point1().y, edge.point1().scope.toString(),
                        edge.point2().x, edge.point2().y, edge.point2().scope.toString());
         
            }
            System.out.println("===========================");
        }
    }
}

