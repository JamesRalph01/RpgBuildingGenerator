/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shapes.BuildingOutline;
import building.Room;
import util.Point;

/**
 *
 * @author chrisralph
 */
public class FloorPlannerTest {
    
    public Room room;
    public BuildingOutline outline;
    
    public FloorPlannerTest() {
        outline = new BuildingOutline();
        
       outline.addPoint(new Point(5,20));
       outline.addPoint(new Point(15,10));
       outline.addPoint(new Point(25,5));
       outline.addPoint(new Point(35,5));
       outline.addPoint(new Point(45,15));
       outline.addPoint(new Point(45,35));
       outline.addPoint(new Point(30,40));
       outline.addPoint(new Point(13,40));
       
       // finish outline by finishing on same point
       outline.addPoint(new Point(5,20));
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

    @Test
    public void testGenerate() {
        System.out.println("Generate");
        FloorPlanner instance = new FloorPlanner();
        instance.setBuildingType(FloorPlanner.BuildingType.TEST);
        instance.generate(outline, 50, 50);
        
        
    }
    
//    
//    /**
//     * Test of setBuildingType method, of class FloorPlanner.
//     */
//    @Test
//    public void testSetBuildingType() {
//        System.out.println("setBuildingType");
//        FloorPlanner.BuildingType buildingType = null;
//        FloorPlanner instance = new FloorPlanner();
//        instance.setBuildingType(buildingType);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of generate method, of class FloorPlanner.
//     */
//    @Test
//    public void testGenerate() {
//        System.out.println("generate");
//        BuildingOutline buildingOutline = null;
//        int w = 0;
//        int h = 0;
//        FloorPlanner instance = new FloorPlanner();
//        instance.generate(buildingOutline, w, h);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of clear method, of class FloorPlanner.
//     */
//    @Test
//    public void testClear() {
//        System.out.println("clear");
//        FloorPlanner instance = new FloorPlanner();
//        instance.clear();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of activeFloorPlan method, of class FloorPlanner.
//     */
//    @Test
//    public void testActiveFloorPlan() {
//        System.out.println("activeFloorPlan");
//        FloorPlanner instance = new FloorPlanner();
//        boolean expResult = false;
//        boolean result = instance.activeFloorPlan();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPositionData method, of class FloorPlanner.
//     */
//    @Test
//    public void testGetPositionData() {
//        System.out.println("getPositionData");
//        FloorPlanner instance = new FloorPlanner();
//        float[] expResult = null;
//        float[] result = instance.getPositionData();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getColourData method, of class FloorPlanner.
//     */
//    @Test
//    public void testGetColourData() {
//        System.out.println("getColourData");
//        FloorPlanner instance = new FloorPlanner();
//        float[] expResult = null;
//        float[] result = instance.getColourData();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of numbervertices method, of class FloorPlanner.
//     */
//    @Test
//    public void testNumbervertices() {
//        System.out.println("numbervertices");
//        FloorPlanner instance = new FloorPlanner();
//        int expResult = 0;
//        int result = instance.numbervertices();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
    
}
