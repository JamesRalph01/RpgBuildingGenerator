/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.ArrayList;
import org.joml.Rectangled;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.Edge;
import util.Point;

/**
 *
 * @author chrisralph
 */
public class RoomTest {
    
    private Room room;
    
    public RoomTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ArrayList<Edge> edges = new ArrayList<>();
        
        edges.add(new Edge(new Point(5,10), new Point(20,10)));
        edges.add(new Edge(new Point(20,10), new Point(20,20)));
        edges.add(new Edge(new Point(20,20), new Point(5,20)));
        edges.add(new Edge(new Point(5,20), new Point(5,10)));
        
        room = new Room(edges);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of edges method, of class Room.
     */
    @Test
    public void testEdges() {
        System.out.println("edges");
        ArrayList<Edge> result = room.edges();
        assertTrue(result.size()==4);
        assertEquals(new Edge(new Point(5,10), new Point(20,10)), result.get(0));
        assertEquals(new Edge(new Point(20,10), new Point(20,20)), result.get(1));
        assertEquals(new Edge(new Point(20,20), new Point(5,20)), result.get(2));
        assertEquals(new Edge(new Point(5,20), new Point(5,10)), result.get(3));                     
    }

    /**
     * Test of points method, of class Room.
     */
    @Test
    public void testPoints() {
        System.out.println("points");
        ArrayList<Point> result = room.points();
        assertTrue(result.size()==4);
        assertEquals(new Point(5,10), result.get(0));
        assertEquals(new Point(20,10), result.get(1));
        assertEquals(new Point(20,20), result.get(2));
        assertEquals(new Point(5,20), result.get(3));
    }

    /**
     * Test of bounds method, of class Room.
     */
    @Test
    public void testBounds() {
        System.out.println("bounds");
        Rectangled expResult = null;
        Rectangled result = room.bounds();
        
        expResult = new Rectangled(5,10,20,20);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPositionData method, of class Room.
     */
    @Test
    public void testGetPositionData() {
        System.out.println("getPositionData");
    }

    /**
     * Test of getColourData method, of class Room.
     */
    @Test
    public void testGetColourData() {
        System.out.println("getColourData");
    }

    /**
     * Test of numbervertices method, of class Room.
     */
    @Test
    public void testNumbervertices() {
        System.out.println("numbervertices");
        int expResult = 4;
        int result = room.numbervertices();
        assertEquals(expResult, result);
    }

    /**
     * Test of adjust method, of class Room.
     */
    @Test
    public void testAdjust_3args() {
        System.out.println("adjust");
        Point adjustmentPoint = new Point(15,5);
        Point pointOnEdge = new Point(15,10);
        Edge edgeToAdjust = room.edges().get(0);
        room.adjust(adjustmentPoint, pointOnEdge, edgeToAdjust);
        assertTrue(room.edges().size()==5);
        assertEquals(new Edge(new Point(5,10), new Point(15,5)), room.edges().get(0));
        assertEquals(new Edge(new Point(15,5), new Point(20,10)), room.edges().get(1));
        assertEquals(new Edge(new Point(20,10), new Point(20,20)), room.edges().get(2));
        assertEquals(new Edge(new Point(20,20), new Point(5,20)), room.edges().get(3));
        assertEquals(new Edge(new Point(5,20), new Point(5,10)), room.edges().get(4));   
    }

    /**
     * Test of adjust method, of class Room.
     */
    @Test
    public void testAdjust_Point_Point() {
        System.out.println("adjust");
        Point currentPoint = new Point(5,20);
        Point adjustedPoint = new Point(5,25);
        room.adjust(currentPoint, adjustedPoint);
        assertEquals(adjustedPoint, room.edges().get(2).point2());
        assertEquals(adjustedPoint, room.edges().get(3).point1());
    }
    
}
