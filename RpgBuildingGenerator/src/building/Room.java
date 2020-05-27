/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.joml.Rectangled;
import org.joml.Vector3f;
import util.Point;
import util.Edge.EdgeAlignment;
import util.Edge;
import util.PolygonHelper;
/**
 *
 * @author chrisralph
 */
public class Room extends BuildingItem {

    public enum RoomType {
        MasterBedroom,
        LivingRoom,
        Kitchen,
        Bathroom,
        SpareRoom,
        Toilet,
        Utility,
        DiningRoom,
        TavernFloor,
        StoreRoom,
        ChurchFloor,
        Empty
    }
    
    public enum AreaType {
        SOCIAL,
        SERVICE,
        PRIVATE
    }
   
    private RoomType roomType;
    private AreaType areaType;
            
    private ArrayList<Edge> edges;
    private float[] colourData;
    private ArrayList<Wall> internalWalls = new ArrayList<>();
    private ArrayList<BuildingItem> furniture = new ArrayList<>();
    
    private ArrayList<Room> roomConnections = new ArrayList<>();
    private ArrayList<Edge> roomConnectionEdges = new ArrayList<>();
    
    
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

    public ArrayList<BuildingItem> getFurniture() {
        return this.furniture;
    }
    
    public ArrayList<Wall> getInternalWalls() {
        return this.internalWalls;
    } 
    
    public void addRoomConnection(Room roomConnection, Edge edge) {
        if (!this.roomConnections.contains(roomConnection)) {
            this.roomConnections.add(roomConnection);
            this.roomConnectionEdges.add(edge);
        }
    }
    
    public ArrayList<Room> getRoomConnections() {
        return this.roomConnections;
    }
    
    public ArrayList<Edge> getRoomConnectionEdges() {
        return this.roomConnectionEdges;
    }
    
    public AreaType getAreaType() {
        return this.areaType;
    }
    
    public RoomType getRoomType() {
        return this.roomType;
    }
    
    public void setAreaType(AreaType type) {
        this.areaType = type;
    }
    
    public void setRoomType(RoomType type) {
        this.roomType = type;
    }
    
    public void printRoomConnections() {
        System.out.println(this.roomType + " Room connections:");
        for (Room room: this.roomConnections) {
            System.out.println(room);
        }
    }
    
    public void Generate3DPositions(Vector3f screenOrigin, int wealthInd) {
        calcLocation(screenOrigin);
        calcInternalWalls(screenOrigin, wealthInd);
    }
   
    private void calcLocation(Vector3f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    
    private void calcInternalWalls(Vector3f screenOrigin, int wealthInd) {
        internalWalls.clear();
        for (Edge edge : this.edges) {
            if (edge.isInternal()) {
                Wall wall = new Wall(edge);
                wall.Generate3DPositionsInternal(screenOrigin, wealthInd, this.roomType);
                this.internalWalls.add(wall);
            }
        }
    }
    
    public boolean isRoomAbove(Edge thisEdge, Room connectedRoom) {
        ArrayList<Room> connectedRooms = connectedRoom.getRoomConnections();
        for (int i=0; i<connectedRooms.size(); i++) {
            if (connectedRooms.get(i).getRoomType() == this.roomType) {
                if (connectedRoom.getRoomConnectionEdges().get(i).y1() > thisEdge.y1()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isRoomLeft(Edge thisEdge, Room connectedRoom) {
        ArrayList<Room> connectedRooms = connectedRoom.getRoomConnections();
        for (int i=0; i<connectedRooms.size(); i++) {
            if (connectedRooms.get(i).getRoomType() == this.roomType) {
                if (connectedRoom.getRoomConnectionEdges().get(i).x1() < thisEdge.x1()) {
                    return true;
                }
            }
        }
        return false;
    }
        
    public Edge getDoorLocation(Edge thisEdge, Room connectedRoom) {
        ArrayList<Room> connectedRooms = connectedRoom.getRoomConnections();
        for (int i=0; i<connectedRooms.size(); i++) {
            if (connectedRooms.get(i).getRoomType() == this.roomType) {
                Edge edge2 = connectedRoom.getRoomConnectionEdges().get(i);
                        
                Point min1 = new Point(Math.min(thisEdge.x1(), thisEdge.x2()),Math.min(thisEdge.y1(),thisEdge.y2()));
                Point max1 = new Point(Math.max(thisEdge.x1(), thisEdge.x2()),Math.max(thisEdge.y1(),thisEdge.y2()));

                Point min2 = new Point(Math.min(edge2.x1(), edge2.x2()),Math.min(edge2.y1(),edge2.y2()));
                Point max2 = new Point(Math.max(edge2.x1(), edge2.x2()),Math.max(edge2.y1(),edge2.y2()));

                Point minInt = new Point(Math.max(min1.x, min2.x), Math.max(min1.y, min2.y));
                Point maxInt = new Point(Math.min(max1.x, max2.x), Math.min(max1.y, max2.y));
                
                return new Edge(minInt,maxInt);
            }
        }
        System.err.println("Can't get door location");
        return new Edge(new Point(0,0), new Point(0,0));
    }
    
    public Edge findFreeEdge() {
        ArrayList<Edge> freeEdges = new ArrayList<>();
        boolean isFree;
        for (Edge edge: this.edges) {
            isFree = true;
            for (Edge connectedEdge: this.roomConnectionEdges) {
                if (edge == connectedEdge) {
                    isFree = false;
                }
            }
            if (isFree) {
                freeEdges.add(edge);
            }
        }
        
        if (freeEdges.isEmpty()) {
            return null;
        }
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(freeEdges.size());
        Edge edge = freeEdges.get(index);
        return edge;
    }
    
    public String getEdgePlacing(Edge thisEdge) {
        String edgePlacement = null;
        Edge leftEdge = thisEdge;
        Edge rightEdge = thisEdge;
        Edge topEdge = thisEdge;
        Edge bottomEdge = thisEdge;
        for (Edge edge: this.edges())
        {
            if (edge != thisEdge && edge.getAlignment() != EdgeAlignment.SLANTED) {
                if (edge.getMidPoint().isAbove(thisEdge.getMidPoint())) {
                    topEdge = edge;
                }
                if (edge.getMidPoint().isLeft(thisEdge.getMidPoint())) {
                    leftEdge = edge;
                }
                if (edge.getMidPoint().isRight(thisEdge.getMidPoint())) {
                    rightEdge = edge;
                }
                if (edge.getMidPoint().isBelow(thisEdge.getMidPoint())) {
                    bottomEdge = edge;
                }
            }
        }
        
        if (topEdge == thisEdge) {edgePlacement = "TOP";}
        else if (leftEdge == thisEdge) {edgePlacement = "LEFT";}
        else if (rightEdge == thisEdge) {edgePlacement = "RIGHT";}
        else if (bottomEdge == thisEdge) {edgePlacement = "BELOW";}
        
        return edgePlacement;
    }
    
    
    @Override
    public String toString() {
        return "Room " + this.roomType;
    }
}
