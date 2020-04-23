/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import java.util.ArrayList;
import shapes.Shape;
import shapes.BuildingOutline;
import util.ConvexHull;
import util.CoordSystemHelper;
import util.GeomPoint;
import org.joml.Vector2i;

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
        Rect bounds = findLargestRect(buildingOutline, w, h);
                
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
    
    private Rect findLargestRect(BuildingOutline buildingOutline, int deviceWidth, int deviceHeight) {
        
        ConvexHull convexHull = new ConvexHull();
        ArrayList<Vector2i> deviceCoords;
        Rect largestRect = new Rect();
        
        //Convert building outline to device coords and add to Convex hull 
        deviceCoords = CoordSystemHelper.openGLToDevice(deviceWidth, deviceHeight, buildingOutline.points()); 
        convexHull.addPointsToHull(deviceCoords);
        
        convexHull.computeLargestRectangle();
        convexHull.forEach((Vector2i point) -> {
            System.out.printf("Converted building outline %d, %d \n", point.x, point.y);
        });
        
       return largestRect;
    }
    
  
}

