/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import building.Building;
import java.awt.Color;
import util.Rect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.joml.Intersectiond;
import designer.BuildingOutline;
import org.joml.Vector2d;
import building.Room;
import designer.FloorPlan;
import floorplanner.ChurchMapModel;
import floorplanner.HouseMapModel;
import floorplanner.MapModel;
import floorplanner.Mappable;
import floorplanner.TavernMapModel;
import floorplanner.TestMapModel;
import floorplanner.TreemapLayout;
import util.Edge;
import util.PolygonHelper;
import util.Point;
/**
 *
 * @author chrisralph
 */


public class FloorPlanner {

    private boolean DEBUG = false;
    
    public enum BuildingType {
        TAVERN, CHURCH, HOUSE, 
        TEST // internal use only for repeatable results
    }
    
    private TreemapLayout algorithm;
    private MapModel mapModel;
    private BuildingType buildingType;
    private boolean activeFloorPlan = false;
    
    private Building building;
    private FloorPlan floorplan;
    
    
    private ArrayList<Point> points = new ArrayList<>();
    PolygonHelper polygonHelper;
    private ArrayList<Room> rooms;
    private HashMap<Point, Point> listPointAdjustments;
   
    
    public FloorPlanner() {
        this.buildingType = BuildingType.TAVERN;
    } 
    
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
   
    public void generate(BuildingOutline buildingOutline) {
        
        building = new Building();
        floorplan = new FloorPlan();

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
        generate2DFloorplan(); // for display in designer view
        //generate3DBuilding(); // for 3D rendering view
        
        activeFloorPlan = true;
    }
    
    public boolean getActiveFloorplan() {
        return activeFloorPlan;
    }
    
    public void clear() {
        activeFloorPlan = false;
    }
    
    public FloorPlan get2DFloorplan()
    {
        return this.floorplan;
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
        
        listPointAdjustments = new HashMap<>();
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
        printAllPoints(false);

        // Detect whether edges are internal or external so that we can expand external edges to fit 
        // the building outline polygon  
        System.out.println("Step 2: internal/external edge detection");
        for (Room room: rooms) {
            for (Edge edge: room.edges()) {
                // check if this edge shares any other room edge
                // take a partial piece of the edge and check if intersects with another room
                // partial to prevent overlaps where rooms touch
                Edge partial = new Edge(edge);
                partial.shrink(5);

                for (Room roomToCheck: rooms) {    
                    if (roomToCheck != room) {
                        for (Edge edgeToCheck : roomToCheck.edges()) {
                            if (partial.intersets(edgeToCheck,2)) {
                                edge.connectedEdges().add(edgeToCheck);
                                edge.isInternal(true);
                            }                          
                        }
                    }
                }
            }
        }
        printAllPoints(false); 

        // Now extend internal edges that have externally facing end points to a point on the building outline
        System.out.println("Step 3: expand internal edges");
        for (Room room: rooms) {  
            for (Edge edge: room.edges()) {

                if (edge.isInternal())
                {
                    for (int i = 0; i < 2; i++) {

                        Point p = (i == 0 ? edge.point1() : edge.point2());

                        if (p.scope == Point.Scope.EXTERNAL) {
                            Edge closestOutsideEdge;
                            Vector2d intersection = new Vector2d();
                            closestOutsideEdge = polygonHelper.closestEdge(edge, p);
                            if (closestOutsideEdge != null) {
                                if (Intersectiond.intersectLineLine(edge.point1().x, edge.point1().y, 
                                                                    edge.point2().x, edge.point2().y,
                                                                    closestOutsideEdge.point1().x, closestOutsideEdge.point1().y,
                                                                    closestOutsideEdge.point2().x, closestOutsideEdge.point2().y,
                                                                    intersection)) {

                                    if (listPointAdjustments.containsKey(p) == false) {
                                        listPointAdjustments.put(new Point(p), new Point(intersection.x, intersection.y));
                                    }

                                }
                            }

                        }   
                    }
                }
            }
        }
        // apply changes
        Set set = listPointAdjustments.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            Point pOld = (Point) mentry.getKey();
            Point pNew = (Point) mentry.getValue();
            for (Room bRoom: rooms) {
                bRoom.adjust(pOld, pNew);    
            }
        }
        printAllPoints(false);

    }
        
    private void printAllPoints(boolean includeadj) {
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
    
    private void generate2DFloorplan() {

        // Add external walls
        for (Edge edge : polygonHelper.edges()) {
            floorplan.addEdge(edge);
        }
        
        // Add internal walls
        for (Room room : rooms) {
            for (Edge edge : room.edges()) {
                if (edge.isInternal()) {
                    floorplan.addEdge(edge);
                }        
            }
        }
    } 
    
    private void generate3DBuilding() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        
}

