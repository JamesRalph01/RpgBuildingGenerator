/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import java.awt.Color;
import util.Rect;
import java.util.ArrayList;
import java.util.Comparator;
import org.joml.Intersectiond;
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

    private boolean DEBUG = true;
    
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
    private ArrayList<EdgeAdjustment> listEdgeAdjustments;
    private int indexColourStart = 0;
    

    
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
         
         colourData = new float[points.size() * 3];   
         
         int i = 0;
         for (Point point : points) {
            colourData[i++] = point.getRedf();
            colourData[i++] = point.getGreenf();
            colourData[i++] = point.getBluef();
         };

        return colourData;
    }
    
    public int numbervertices() {
        return points.size();
    }
    
    private Color getRoomColour(String roomType) {
        int R,G,B;
        
        if (!roomType.equals("NA")) {
           // Social
           if (roomType == "Li" || roomType == "Dr") {
              R = 255;
              G = 20;
              B = 20;  
           }
           // Service
           else if (roomType == "Ki" || roomType == "Ut") {
               R = 255;
               G = 255;
               B = 20;     
           }
           // Private
           else if (roomType == "Mb" || roomType == "Sr" || roomType == "To" || roomType == "Br") {
               R = 20;
               G = 20;
               B = 255; 
           }
           else {
               R = 255;
               G = 255;
               B = 255; 
           } 
        }
        else {
            R = 102;
            G = 224;
            B = 20; 
        }
        return new Color(R,G,B);
    }
    
    private void generateRooms(Rect overallBounds)
    {
        Rect bounds;
        
        listEdgeAdjustments= new ArrayList<>();
        Mappable[] items = mapModel.getItems();
        
        System.out.printf("Treemap bounds x1: %f, y1: %f, x2: %f, y2: %f\n", 
                overallBounds.x, overallBounds.y, 
                overallBounds.x+overallBounds.w, overallBounds.y+overallBounds.h);
        
        rooms = new ArrayList<>();
        
        // Convert treemap output into a Rooms collection with edges
        System.out.println("Step 1: Load Treemap");
        for (int i=0; i<items.length; i++) {
            Color c = getRoomColour(items[i].getRoomType());
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
            // set room type
            Room room = new Room(edges);
            for (Edge edge : edges) {
                edge.point1().setColour(c);
                edge.point2().setColour(c);
            }
            rooms.add(new Room(edges));
        }
        printAllPoints();

        if (DEBUG == false) {


            // Detect whether edges are internal or external so that we can expand external edges to fit 
            // the building outline polygon  
            System.out.println("Step 2: internal/external edge detection");
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
            printAllPoints();

            // Split existing treemap edges where a point on the building outline would intersect it 
            System.out.println("Step 3: split external edges");
            for (Point externalPoint: polygonHelper.points()) {

                Edge nearestExternalEdge = null;
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
                                nearestExternalEdge = roomEdge;
                                nearestRoom = room;
                                nearestPoint = new Point(result.x, result.y);
                                first = false;
                            } else {
                                // found closer edge?
                                if (externalPoint.distance(new Point(result.x, result.y)) < 
                                    externalPoint.distance(nearestPoint)) {
                                    nearestExternalEdge = roomEdge;
                                    nearestRoom = room;            
                                    nearestPoint = new Point(result.x, result.y);

                                }
                            }
                        }  
                    }

                }
                // Adjust room and edge to fit building outline point
                if (nearestRoom != null) {
                    listEdgeAdjustments.add(new EdgeAdjustment(nearestRoom, nearestExternalEdge, nearestPoint, externalPoint));
                }

            }
            printAllPoints();    

            // Now extend internal edges that have externally facing end points to a point on the building outline
            System.out.println("Step 4: expand internal edges with one external point");
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
            printAllPoints();

            //Split external facing edges
            System.out.println("Step 5: split external edges");
            for (EdgeAdjustment adj : listEdgeAdjustments) {
                adj.room.split(adj.splitPoint, adj.edge);
            }
            printAllPoints();

            //Finally move split points to building outline
            System.out.println("Step 6: move split point");
            for (EdgeAdjustment adj : listEdgeAdjustments) {
                adj.room.adjust(adj.splitPoint, adj.movePoint);
            }
            printAllPoints();
        }
    }

    
    private void printAllPoints() {
        for (Room room: rooms) {
            System.out.println("Room");
            for (Edge edge: room.edges()) {
                System.out.printf("Edge isInternal: %s\n", edge.isInternal());
                System.out.printf("x1: %d, y1: %d, scope1: %s -> x2: %d, y2: %d, scope2: %s \n", 
                        edge.point1().x, edge.point1().y, edge.point1().scope.toString(),
                        edge.point2().x, edge.point2().y, edge.point2().scope.toString());
//                for (EdgeAdjustment adj : listEdgeAdjustments) {
//                    System.out.printf("... adjustment point x1: %d, y1: %d, index %d \n", point.x, point.y, point.orginalIndex);
//                }
            }
            System.out.println("===========================");
        }
    }
    
    class adjustmentPointsComp implements Comparator<Point>{
 
    @Override
    public int compare(Point p1, Point p2) {
        if(p1.orginalIndex > p2.orginalIndex){
            return 1;
        } else {
            return -1;
        }
    }
    }       
    
}

