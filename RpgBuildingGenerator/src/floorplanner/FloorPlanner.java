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
import shapes.Shape;
import shapes.BuildingOutline;
import util.ConvexHull;
import util.CoordSystemHelper;
import org.joml.Vector2i;
import util.PolygonHelper;

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
    
    
    private Rectangled findLargestRect(BuildingOutline buildingOutline, int deviceWidth, int deviceHeight) {
        ArrayList<Vector2i> pointsToCheck = new ArrayList<>();
        ArrayList<Rectangled> rectangles = new ArrayList<>();
        ArrayList<Vector2i> polygon;
        Rectangled bounds;
        Vector2i pointToCheck;
        PolygonHelper polygonHelper;
        
        polygon = buildingOutline.points();
        bounds = buildingOutline.boundingRect();
        polygonHelper = new PolygonHelper(polygon);
        pointsToCheck = calcPointstoCheck(bounds, polygon, polygonHelper);
        
        for (Vector2i point : pointsToCheck) {
            rectangles.add(calcLargestRect(point, polygon, polygonHelper));
        }
        
        
        return largestRect;
    }

    private ArrayList<Vector2i> calcPointstoCheck(Rectangled bounds, ArrayList<Vector2i> polygon, PolygonHelper polygonHelper) {
    
            ArrayList<Vector2i> pointsToCheck = new ArrayList<>();
        
            // Create 100 sample points within the bounding rectangle
            for (int i=0; i<100; i++) {
                Vector2i point = new Vector2i();
                point.x = (int) ((Math.random() * (bounds.maxX-bounds.minX)) + bounds.minX);
                point.y = (int) ((Math.random() * (bounds.maxY-bounds.minY)) + bounds.minX);
                if (polygonHelper.isPointInsidePolygon(point)) {
                    pointsToCheck.add(point);                   
                }
            }
            return pointsToCheck;
    }
    
    private Rectangled calcLargestRect(Vector2i pointToCheck, ArrayList<Vector2i> polygon, PolygonHelper polygonHelper) {
        int ScanX, ScanY;
        Rectangled rect = new Rectangled();
        
        ScanX = 1;
        ScanY = 1;
        do
        {
            
        } while (rectangle inside polygon);   
         
        
        return rect;
    }
}

