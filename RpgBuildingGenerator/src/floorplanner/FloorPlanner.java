/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import building.Building;
import building.BuildingItem;
import building.furniture.Door;
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
import static building.Room.RoomType.Kitchen;
import building.Wall;
import building.furniture.Bar;
import building.furniture.BarTable;
import building.furniture.Barrel;
import building.furniture.Barrels;
import building.furniture.Bath;
import building.furniture.DiningTable;
import building.furniture.DoubleBed;
import building.furniture.ExternalDoor;
import building.furniture.Fire;
import building.furniture.Fridge;
import building.furniture.KitchenSinkAndOven;
import building.furniture.KitchenTable;
import building.furniture.MetalDoor;
import building.furniture.ModernToilet;
import building.furniture.OldSofa;
import building.furniture.SciFiBox;
import building.furniture.SciFiDoor;
import building.furniture.Shelf;
import building.furniture.SingleBed;
import building.furniture.Stool;
import building.furniture.TV;
import building.furniture.Table;
import designer.FloorPlan;
import java.util.Arrays;
import java.util.Date;
import org.joml.Vector2f;
import util.Edge;
import util.Edge.EdgeAlignment;
import util.PolygonHelper;
import util.Point;



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
        
    private Color getRoomColour(RoomType roomType) {
        int R,G,B;
        
        if (!roomType.equals(RoomType.Empty)) {
            // Social
            switch (roomType) {
                // SOCIAL AREAS
                case LivingRoom:
                case DiningRoom:
                case TavernFloor:
                    R = 255;
                    G = 20;
                    B = 20;
                    break;
                // SERVICE AREAS
                case Kitchen:
                case Utility:
                case StoreRoom:
                    R = 255;
                    G = 255;
                    B = 20;
                    break;
                // PRIVATE AREAS
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
                                // Add roomConnections is valid
                                if (mapModel.checkRoomConnection(room.getRoomType(), roomToCheck.getRoomType())) {
                                    if (edge.getAlignment() == edgeToCheck.getAlignment()) {
                                        room.addRoomConnection(roomToCheck,edge);
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

                        // Check Point is external
                        if (p.scope == Point.Scope.EXTERNAL) {
                            Edge closestOutsideEdge;
                            Vector2d intersection = new Vector2d();
                            closestOutsideEdge = polygonHelper.closestEdge(edge, p);
                            if (closestOutsideEdge != null) {
                                // Check if edge intersects with the external edge
                                if (Intersectiond.intersectLineLine(edge.point1().x, edge.point1().y, 
                                                                    edge.point2().x, edge.point2().y,
                                                                    closestOutsideEdge.point1().x, closestOutsideEdge.point1().y,
                                                                    closestOutsideEdge.point2().x, closestOutsideEdge.point2().y,
                                                                    intersection)) {

                                    if (listPointAdjustments.containsKey(p) == false) {
                                        // Add new and old points to the hashmap of points that need adjusting
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

        HashMap<String, Point> labels = new HashMap<>();
        
        // Add external walls
        for (Edge edge : polygonHelper.edges()) {
            floorplan.addEdge(edge);
        }
        
        // Add internal walls
        for (Room room : rooms) {
            labels.put(room.RoomTypeText[room.getRoomType().ordinal()], room.centreOf());
            for (Edge edge : room.edges()) {
                if (edge.isInternal()) {
                    floorplan.addEdge(edge);
                }        
            }
        }
        
        floorplan.setLabels(labels);
        
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
            
            
            //           //
            //   DOORS   //
            //           //
            
            // Add Door Connections
            ArrayList<Room> roomConnections = room.getRoomConnections();
            
            if (roomConnections.size() >= 1) { // Has Room Connections
                ArrayList<Edge> connectionEdges = room.getRoomConnectionEdges();
                BuildingItem door;
                Point doorLocation;
                for (int i=0; i<roomConnections.size(); i++) {
                    door = new Door(this.buildingTheme, wealthIndicator);
                    //door = new ExternalDoor(this.buildingTheme, wealthIndicator);
                    Edge sharedEdge = room.getDoorLocation(connectionEdges.get(i),roomConnections.get(i));
                    int edgeLength;
                    if (connectionEdges.get(i).getAlignment() == EdgeAlignment.HORIZONTAL) {
                        edgeLength = Math.abs(sharedEdge.x1() - sharedEdge.x2());
                    }
                    else {
                        edgeLength = Math.abs(sharedEdge.y1() - sharedEdge.y2());
                    }
                    
                    if (edgeLength > 20) {
                        doorLocation = new Point((sharedEdge.x1()+sharedEdge.x2())/2,(sharedEdge.y1()+sharedEdge.y2())/2);
                        if (connectionEdges.get(i).getAlignment() == EdgeAlignment.HORIZONTAL) {
                            door.setRotation(0, 0, 0);
                            if (room.isRoomAbove(connectionEdges.get(i),roomConnections.get(i))) {
                                doorLocation.y -= 5; 
                            }
                            else {
                                doorLocation.y += 5;
                            } 
                        } else { // VERTICAL
                            door.setRotation(0, -90, 0); 
                            if (room.isRoomLeft(connectionEdges.get(i),roomConnections.get(i))) {
                                doorLocation.x += 5;
                            }
                            else {
                                doorLocation.x -= 5;
                            }   
                        }
                        door.setLocation(doorLocation.x(), 0, doorLocation.y());
                        room.getFurniture().add(door); 
                        System.out.println("DOOR ADDED between " + room.getRoomType() + " AND " + roomConnections.get(i));
                        System.out.println(edgeLength);
                    }
                }
            }
            
            //               //
            //   FURNITURE   //
            //               //
            
            ArrayList<BuildingItem> furniture= new ArrayList<>();
            BuildingItem temp = null;
            
            switch (room.getRoomType()) {
                case LivingRoom:
                    temp = new OldSofa();
                    temp.placeOnEdge = true;
                    temp.displacement = 10;
                    furniture.add(temp);
                    if (buildingTheme != BuildingTheme.MEDIEVAL)
                    {
                        temp = new TV(buildingTheme);
                        temp.placeInCentre = true;
                        temp.displacement = 10;
                        furniture.add(temp);
                    }
                    else {
                        temp = new Fire(buildingTheme,wealthIndicator);
                        if (this.wealthIndicator > 50) {
                            temp.placeOnEdge = true;
                            temp.displacement = 10;
                        }
                        else {
                            temp.placeInCentre = true;
                        }
                        furniture.add(temp);
                    }
                    break;
                case DiningRoom:
                    temp = new DiningTable(buildingTheme);
                    temp.placeInCentre = true;
                    furniture.add(temp);
                    break;
                case Kitchen:
                    temp = new KitchenTable(buildingTheme);
                    temp.placeInCentre = true; 
                    furniture.add(temp);
                    if (buildingTheme != BuildingTheme.MEDIEVAL)
                    {
                        temp = new KitchenSinkAndOven();
                        temp.placeOnEdge = true;
                        temp.displacement = 10;
                        furniture.add(temp);
                    }
                    break;
                case Utility:
                    if (buildingTheme != BuildingTheme.MEDIEVAL)
                    {
                        temp = new Fridge(this.buildingTheme);
                        temp.placeOnEdge = true;
                        temp.displacement = 10;
                    }
                    else {
                        if (this.wealthIndicator > 50) {
                            temp = new Barrels();
                        }
                        else {
                            temp = new Barrel();
                        }
                        temp.placeOnEdge = true;
                        temp.displacement = 10;

                    }
                    furniture.add(temp);
                    break;
                case MasterBedroom:
                    temp = new DoubleBed(buildingTheme);
                    temp.placeOnEdge = true;
                    temp.displacement = 15;
                    furniture.add(temp);
                    break;
                case SpareRoom:
                    temp = new SingleBed(buildingTheme);
                    temp.placeOnEdge = true;
                    furniture.add(temp);
                    break;
                case Toilet:
                    temp = new ModernToilet();
                    temp.placeOnEdge = true;
                    furniture.add(temp);
                    break;
                case Bathroom:
                    temp = new ModernToilet();
                    temp.placeOnEdge = true;
                    furniture.add(temp);
                    //furniture = new Bath(buildingTheme); <<< CRASHES
                    //placeInCentre = true;                 
                    break;
                case TavernFloor:
                    temp = new Bar();
                    temp.placeOnEdge = true;
                    furniture.add(temp);
                    
                    temp = new BarTable();
                    temp.placeInCentre = true;
                    furniture.add(temp);
                    
                    temp = new Fire(this.buildingTheme, this.wealthIndicator);
                    temp.placeOnEdge = true;
                    furniture.add(temp);                    
                    break;
                case StoreRoom:
                    temp = new Barrels();
                    temp.placeOnEdge = true;
                    furniture.add(temp);
                    break;
                case ChurchFloor:
                    break;
                default:
                    break;            
            }
            
            for (BuildingItem item: furniture) {
                if (furniture != null) {

                    float x = 0, z = 0;

                    if (item.placeInCentre) {
                        x = (float) room.bounds().minX + ((float) (room.bounds().maxX-room.bounds().minX)/ 2.0f);
                        z = (float) room.bounds().minY + ((float) (room.bounds().maxY-room.bounds().minY)/ 2.0f);
                    }
                    if (item.placeOnEdge) {
                        Edge freeEdge = room.findFreeEdge();
                        if (freeEdge != null) {
                            Point placement = freeEdge.getMidPoint();
                            String edgePlacement = room.getEdgePlacing(freeEdge);
                            System.out.println(edgePlacement);
                            if (edgePlacement != null) switch (edgePlacement) {
                                case "TOP":
                                    x = placement.x();
                                    z = placement.y() - item.displacement;
                                    item.setRotation(0, 180, 0);
                                    break;
                                case "LEFT":
                                    x = placement.x() + item.displacement;
                                    z = placement.y();
                                    item.setRotation(0, 90, 0);
                                    break;
                                case "RIGHT":
                                    x = placement.x() - item.displacement;
                                    z = placement.y();
                                    item.setRotation(0, -90, 0);
                                    break;
                                case "BELOW":
                                    x = placement.x();
                                    z = placement.y() + item.displacement;
                                    item.setRotation(0, 0, 0);
                                    break;
                                default:
                                    break;
                            }
                        }
                        else {
                            item = null; // Remove furniture: No free edge
                        }
                    }

                    
                    if (item != null) {
                        item.setLocation(x, 0, z);
                        room.getFurniture().add(item);                 
                    }              
                }

            }
        
        }
        
        
        // Generate 3D position data for our building (in device coords)
        this.building.Generate3DPositions(); 
    }
         
        
}

