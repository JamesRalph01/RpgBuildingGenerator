/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import building.Building;
import building.BuildingItem;
import building.Door;
import java.awt.Color;
import util.Rect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.sql.Timestamp;
import org.joml.Intersectiond;
import designer.BuildingOutline;
import org.joml.Vector2d;
import building.Room;
import building.Room.RoomType;
import building.Wall;
import building.furniture.Barrel;
import building.furniture.Bed;
import building.furniture.Table;
import designer.FloorPlan;
import java.util.Arrays;
import java.util.Date;
import org.joml.Vector2f;
import util.Edge;
import util.Edge.EdgeAlignment;
import util.PolygonHelper;
import util.Point;
/**
 *
 * @author chrisralph
 */


public class FloorPlanner {

    private boolean DEBUG = false;
    public Timestamp timestamp; // timestamp of last floorplan created
    
    public enum BuildingType {
        TAVERN, CHURCH, HOUSE, 
        TEST // internal use only for repeatable results
    }
    
    public enum BuildingTheme {
        MEDIEVAL, MODERN, FUTURISTIC
    }
    
    //Building Criteria (Inputs)
    private BuildingType buildingType;
    private BuildingTheme buildingTheme;
    private int wealthIndicator; // In range 0 to 100
    
    //Used for floor plan generation
    private TreemapLayout algorithm;
    private MapModel mapModel;
    private ArrayList<Point> points = new ArrayList<>();
    PolygonHelper polygonHelper;
    private ArrayList<Room> rooms;
    private HashMap<Point, Point> listPointAdjustments;
    private ArrayList<Mappable> roomList;
    
    //Outputs
    private Building building;
    private FloorPlan floorplan;
    private boolean floorPlanAvailable = false;
    
    public FloorPlanner() {
        this.buildingType = BuildingType.TAVERN;
        this.buildingTheme = BuildingTheme.MEDIEVAL;
        this.wealthIndicator = 50;
        timestamp = new Timestamp(new Date().getTime());
    } 
   
     public void setBuildingTheme(BuildingTheme buildingTheme) {
        this.buildingTheme = buildingTheme;
    }
    
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
    
    public void setWealthIndicator(int wealthIndicator) {
        this.wealthIndicator = wealthIndicator;
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

        roomList = new ArrayList<>();
        
        Mappable[] items = mapModel.getAreaRatios();
        for (Mappable item : items) {
            
            System.out.println(item.getAreaType() + " ::: " + item.getBounds());

            switch (item.getAreaType()) {
                case SOCIAL:
                    algorithm.layout(mapModel.getSocialRatios(), item.getBounds());
                    roomList.addAll(Arrays.asList(mapModel.getSocialRatios()));
                    break;
                case SERVICE:
                    algorithm.layout(mapModel.getServiceRatios(), item.getBounds());
                    roomList.addAll(Arrays.asList(mapModel.getServiceRatios()));
                    break;
                case PRIVATE:
                    algorithm.layout(mapModel.getPrivateRatios(), item.getBounds());
                    roomList.addAll(Arrays.asList(mapModel.getPrivateRatios()));
                    break;
                default:
                    //algorithm.layout(mapModel.getSocialRatios(), item.getBounds());
                    //roomList.addAll(Arrays.asList(mapModel.getSocialRatios()));
                    break;
            }
        }
        
        System.out.println("Hi");
        for (Mappable room: roomList) {
            System.out.println(room.getAreaType() + " : " + room.getRoomType() + " : " 
                    + room.getBounds());
        }
                
        generateRooms(bounds);
        generate2DFloorplan(); // for display in designer view
        generate3DBuilding(); // for 3D rendering view
        
        floorPlanAvailable = true;
        timestamp = new Timestamp(new Date().getTime());
    }
    
    public boolean hasfloorPlanAvailable() {
        return floorPlanAvailable;
    }
    
    public void clear() {
        floorPlanAvailable = false;
        timestamp = new Timestamp(new Date().getTime());
    }
    
    public FloorPlan get2DFloorplan()
    {
        return this.floorplan;
    }
    
    public Building get3DBuilding()
    {
        return this.building;
    }
        
    private Color getRoomColour(Room.RoomType roomType) {
        int R,G,B;
        
        if (!roomType.equals(Room.RoomType.Empty)) {
            // Social
            switch (roomType) {
                case LivingRoom:
                case DiningRoom:
                    R = 255;
                    G = 20;
                    B = 20;
                    break;
                case Kitchen:
                case Utility:
                    R = 255;
                    G = 255;
                    B = 20;
                    break;
                case MasterBedroom:
                case SpareRoom:
                case Toilet:
                case Bathroom:
                    R = 20;
                    G = 20;
                    B = 255;
                    break;
                default: 
                    R = 255;
                    G = 255;
                    B = 255;
                    break;
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

        
        overallBounds.x = Math.round(overallBounds.x + 2);
        overallBounds.y = Math.round(overallBounds.y + 2);
        overallBounds.w = Math.round(overallBounds.w - 4);
        overallBounds.h = Math.round(overallBounds.h - 4);
        
        System.out.printf("Treemap bounds x1: %f, y1: %f, x2: %f, y2: %f\n", 
                overallBounds.x, overallBounds.y, 
                overallBounds.x+overallBounds.w, overallBounds.y+overallBounds.h);
        
        rooms = new ArrayList<>();
        
        // Convert treemap output into a Rooms collection with edges
        System.out.println("Step 1: Load Treemap");
        for (Mappable item : this.roomList) {
            Color c = getRoomColour(item.getRoomType());
            bounds = item.getBounds();
            
            ArrayList<Edge> edges = new ArrayList<>();
            
            bounds.x = Math.round(bounds.x + 2);
            bounds.y = Math.round(bounds.y + 2);
            bounds.w = Math.round(bounds.w - 4);
            bounds.h = Math.round(bounds.h - 4);
            
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
            room.setRoomType(item.getRoomType());
            for (Edge edge : edges) {
                edge.point1().setColour(c);
                edge.point2().setColour(c);
            }
            rooms.add(room);
            
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
                partial.shrink(6);

                for (Room roomToCheck: rooms) {    
                    if (roomToCheck != room) {
                        for (Edge edgeToCheck : roomToCheck.edges()) {
                            if (partial.sharesEdge(edgeToCheck,6)) {
                                edge.connectedEdges().add(edgeToCheck);
                                edge.isInternal(true);
                                if (mapModel.checkRoomConnection(room.getRoomType(), roomToCheck.getRoomType())) {
                                    if (edge.getAlignment() == edgeToCheck.getAlignment()) {
                                        room.addRoomConnection(roomToCheck.getRoomType(),edge);
                                        //System.out.println("Adding " + roomToCheck.getRoomType() + " TO " + room.getRoomType());
                                    }
                                }
                            }                          
                        }
                    }
                }
            }
            room.printRoomConnections();
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
//        for (Room room: rooms) {
//            System.out.println("Room");
//            for (Edge edge: room.edges()) {
//                System.out.printf("Edge isInternal: %s\n", edge.isInternal());
//                System.out.printf("x1: %d, y1: %d, scope1: %s -> x2: %d, y2: %d, scope2: %s \n", 
//                        edge.point1().x, edge.point1().y, edge.point1().scope.toString(),
//                        edge.point2().x, edge.point2().y, edge.point2().scope.toString());
//            }
//
//            System.out.println("===========================");
//        }

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
        
        // Calculate offset from centre of building to 0,0
        // Used to display building with origin in the centre of the building
        Vector2f origin = new Vector2f((float)this.polygonHelper.boundingRect().lengthX() / 2.0f, 
                                       (float)this.polygonHelper.boundingRect().lengthY() / 2.0f);
        origin.x += this.polygonHelper.boundingRect().minX;
        origin.y += this.polygonHelper.boundingRect().minY;
        
        this.building = new Building();
        this.building.setLocation(-origin.x, 0, -origin.y);
        this.building.setWealthIndicator(this.wealthIndicator);
        this.building.setBuildingTheme(this.buildingTheme);
 
        // Add External walls
        for (Edge edge : polygonHelper.edges()) {
            Wall wall = new Wall(edge);
            wall.isInternal = false;
            this.building.addExternalWall(wall);
        }
        
        //Add Floor
        this.building.getFloor().setExternalPoints(this.polygonHelper.points());
        this.building.getFloor().setBoundingRect(this.polygonHelper.boundingRect());
        
        // Add Rooms
        for (Room room : this.rooms) {
            this.building.addRoom(room);

            // Add Door Connections
            ArrayList<Room.RoomType> roomConnections = room.getRoomConnections();
            
            if (roomConnections.size() >= 1) { // Has Room Connections
                ArrayList<Edge> connectionEdges = room.getRoomConnectionEdges();
                BuildingItem door = null;
                Point doorLocation = null;
                for (int i=0; i<roomConnections.size(); i++) {
                    door = new Barrel();
                    doorLocation = connectionEdges.get(i).getMidPoint();
                    door.setLocation(doorLocation.x(), 0, doorLocation.y());
                    room.getFurniture().add(door); 
                }
            }
            
            // hack - stuff the furniture item in the midlle of the room
            float x = (float) room.bounds().minX + ((float) (room.bounds().maxX-room.bounds().minX)/ 2.0f);
            float z = (float) room.bounds().minY + ((float) (room.bounds().maxY-room.bounds().minY)/ 2.0f);
            
            BuildingItem furniture = null;
            
            if (room.roomType == RoomType.LivingRoom) {
                // Add Door
                int dx;
                int dy;  

                BuildingItem door = new Door();

                if (room.edges().get(0).alignment() == EdgeAlignment.HORIZONTAL) {
                    dx = Math.min(room.edges().get(0).x1(), room.edges().get(0).x2()) + room.edges().get(0).getLength() / 2;
                    dy = room.edges().get(0).y1() + 4;           
                } else {
                    dx = room.edges().get(0).x1() + 4;
                    dy = Math.min(room.edges().get(0).y1(), room.edges().get(0).y2()) + room.edges().get(0).getLength() / 2; 
                    door.setRotation(0, -90, 0);         
                }

                door.setLocation(dx, 0, dy);
                room.getFurniture().add(door);                 
                
                furniture = new Barrel();
            } else if (room.getRoomType() == RoomType.DiningRoom) {
                furniture = new Table();
            } else if (room.getRoomType() == RoomType.MasterBedroom) {
                furniture = new Bed();
            }
            if (furniture != null) {
                furniture.setLocation(x, 0, z);
                room.getFurniture().add(furniture);                 
            }

        }
       
        
        // Generate 3D position data for our building (in device coords)
        this.building.Generate3DPositions(); 
    }

        
}

