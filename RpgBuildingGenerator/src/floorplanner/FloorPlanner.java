/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import util.Rect;
import java.util.ArrayList;
import org.joml.Rectangled;
import org.joml.Vector2d;
import org.joml.Vector2f;
import shapes.Shape;
import shapes.BuildingOutline;
import util.ConvexHull;
import util.CoordSystemHelper;
import org.joml.Vector2i;
import util.GeoHelper;

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
    private float normalisedX, normalisedY, transX, transY;
    private boolean activeFloorPlan = false;
    
    public FloorPlanner() {
        this.buildingType = BuildingType.TAVERN;
    }
    
    
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
    
    public void generate(BuildingOutline buildingOutline, int w, int h) {
        
        // TODO: need to look at being consistent with coordinate systems.
        // at the moment w and h are screen coords whereas buidlingOutline has OpenGL coord system (-1, 1)
    
        normalisedX = 2.0f / (float) w;
        normalisedY = 2.0f / (float) h;
        transX = -1.0f;
        transY = -1.0f;
        
        //Step 1: Find largest rectangle inside user drawn building outline
        // Rect bounds = findLargestRect(buildingOutline, w, h);
        Rect bounds = new Rect(10,10, w-20, h-20);
                
        switch (buildingType) {
            case TAVERN:
                mapModel = new TavernMapModel(w, h);
                break;
            case CHURCH:
                mapModel = new ChurchMapModel(w, h);
                break;
            case HOUSE:
                mapModel = new HouseMapModel(w, h);
                break;       
        }
        
        algorithm = new TreemapLayout();
        algorithm.layout(mapModel, bounds);
        
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

        Rect rect;
        
        Mappable[] items = mapModel.getItems();
        float positionData[] = new float[items.length*3*8];

        int p = 0;
        for (int i=0; i<items.length; i++) {
            rect = items[i].getBounds();
            
            //1
            positionData[p++] = normalisedX * (float) rect.x + transX;
            positionData[p++] = normalisedY * (float) rect.y + transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) (rect.x + rect.w) + transX;
            positionData[p++] = normalisedY * (float) rect.y + transY;
            positionData[p++] = 1.0f;

            //2
            positionData[p++] = normalisedX * (float) (rect.x + rect.w) + transX;
            positionData[p++] = normalisedY * (float) rect.y + transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) (rect.x + rect.w) + transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) + transY;
            positionData[p++] = 1.0f;

            //3
            positionData[p++] = normalisedX * (float) (rect.x + rect.w) + transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) + transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) rect.x + transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) + transY;
            positionData[p++] = 1.0f;

            //4
            positionData[p++] = normalisedX * (float) rect.x + transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) + transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) rect.x + transX;
            positionData[p++] = normalisedY * (float) rect.y + transY;
            positionData[p++] = 1.0f;          
        }
        return positionData;
    }

    @Override
    public float[] getColourData() {

        Mappable[] items = mapModel.getItems();
        float colourData[] = new float[items.length*3*8];
        int p = 0;
        while (p < colourData.length) {
            colourData[p++] = 102f/255f;
            colourData[p++] = 224f/255f;
            colourData[p++] = 20f/255f;                
        }
        return colourData;
    }
    
    public int numbervertices() {
        return mapModel.getItems().length*8;
    }
    
    /*private Rect findLargestRect(BuildingOutline buildingOutline, int deviceWidth, int deviceHeight) {
        
        ConvexHull convexHull = new ConvexHull();
        ArrayList<Vector2i> deviceCoords;
        Rect largestRect = new Rect();
        
        //Prove
        Vector2i p1 = new Vector2i(50,70);
        Vector2f p2 = CoordSystemHelper.deviceToOpenGL(deviceWidth, deviceHeight, p1);
        Vector2i p3 = CoordSystemHelper.openGLToDevice(deviceWidth, deviceHeight, p2);
        System.out.printf("width %d, height %d \n", deviceWidth, deviceHeight);
        System.out.printf("normx %f, normy %f \n", 2.0f / deviceWidth, 2.0f / deviceHeight);
        System.out.printf("p1 x %d, y %d \n", p1.x, p1.y);
        System.out.printf("p2 x %f, y %f \n", p2.x, p2.y);
        System.out.printf("p3 x %d, y %d \n", p3.x, p3.y);
        
        //Convert building outline to device coords and add to Convex hull 

        deviceCoords = CoordSystemHelper.openGLToDevice(deviceWidth, deviceHeight, buildingOutline.points()); 
        deviceCoords.forEach((Vector2i point) -> {
            System.out.printf("Original building outline %d, %d \n", point.x, point.y);
        });
        convexHull.addPointsToHull(deviceCoords);        
        convexHull.forEach((Vector2i point) -> {
            System.out.printf("Converted building outline %d, %d \n", point.x, point.y);
        });
        
        convexHull.computeLargestRectangle();
        
        if (convexHull.rectangles().isEmpty() == false) {
            largestRect = convexHull.rectangles().get(6);
            System.out.printf("Final rect x %f, y %f, w %f, h %f \n", largestRect.x, largestRect.y, largestRect.w, largestRect.h);
         } 
        return largestRect;
    } */
    
    private Rect findLargestRect(BuildingOutline buildingOutline, int deviceWidth, int deviceHeight) {
        Rect largestRect = new Rect();
        ArrayList<Vector2i> polygon;
        Rectangled bounds;
        Vector2i pointToCheck;
        
        polygon = buildingOutline.points();
        bounds = buildingOutline.boundingRect();
                
        // Move across x and y axis of bounding rect, checking if point is inside Polygon
        for (int scanX = (int)bounds.minX; scanX < (int) bounds.maxX; scanX++) {
            for (int scanY = (int)bounds.minY; scanY < (int) bounds.maxY; scanY++) {
                pointToCheck = new Vector2i(scanX, scanY);
                if (GeoHelper.isPointInsidePolygon(polygon, pointToCheck)) {
                    // Found point inside polygon
                    // 1) Check across x axis from TL to find furthest point inside polygon
                    // 2) Check across y axis from TL
                    // 3) Check across x axis from BL (from 1)
                    // 4) Check across y axis from 
                }          
            }        
        }
        
        return largestRect;
    }
}

