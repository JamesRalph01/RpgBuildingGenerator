/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.joml.Rectangled;
import org.joml.Vector2i;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chrisralph
 */
public class PolygonHelperTest {
    
    private ArrayList<Vector2i> polygon;
    private PolygonHelper polygonHelper;

    public PolygonHelperTest() {
        polygon = new ArrayList<>();
        polygon.add(new Vector2i(10,10));
        polygon.add(new Vector2i(15,15));
        polygon.add(new Vector2i(20,10));
        polygon.add(new Vector2i(20,20)); 
        polygon.add(new Vector2i(10,20)); 
        polygonHelper = new PolygonHelper(polygon);
    }
    
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isPointInsidePolygon method, of class PolygonHelper.
     */
    @Test
    public void testIsPointInsidePolygon() {
        boolean result;
        Vector2i pointToTest;
        
        System.out.println("isPointInsidePolygon");
        
        pointToTest = new Vector2i(15,15);
        result = polygonHelper.isPointInsidePolygon(pointToTest);
        assertEquals(true, result);
        
        pointToTest = new Vector2i(0,0);
        result = polygonHelper.isPointInsidePolygon(pointToTest);
        assertEquals(false, result);
        
        pointToTest = new Vector2i(15,10);
        result = polygonHelper.isPointInsidePolygon(pointToTest);
        assertEquals(false, result);
        
        pointToTest = new Vector2i(15,16);
        result = polygonHelper.isPointInsidePolygon(pointToTest);
        assertEquals(true, result);
        
        pointToTest = new Vector2i(10,10);
        result = polygonHelper.isPointInsidePolygon(pointToTest);
        assertEquals(false, result);
    }

    /**
     * Test of isRectInsidePolygon method, of class PolygonHelper.
     */
    @Test
    public void testIsRectInsidePolygon() {
        boolean result;
        Vector2i pointToTest;
        Rectangled rectToTest;
        
        System.out.println("isRectInsidePolygon");
        
        rectToTest = new Rectangled(11,11, 19,19);
        result = polygonHelper.isRectInsidePolygon(rectToTest);
        assertEquals(false, result);
        
        rectToTest = new Rectangled(11,16, 19,19);
        result = polygonHelper.isRectInsidePolygon(rectToTest);
        assertEquals(true, result);  
        
        rectToTest = new Rectangled(5,5, 25,25);
        result = polygonHelper.isRectInsidePolygon(rectToTest);
        assertEquals(false, result);
    }
    
    @Test
    public void testfindLargestRect() {
        Rect result;
        
        System.out.println("testfindLargestRect");
        
        result = polygonHelper.findLargestRect(new Vector2i(15,18));
        
        assertEquals(11.0f, result.x, 0);
        assertEquals(16.0f, result.y, 0);
        assertEquals(8.0f, result.w, 0);
        assertEquals(3.0f, result.h, 0);
    }
    
}
