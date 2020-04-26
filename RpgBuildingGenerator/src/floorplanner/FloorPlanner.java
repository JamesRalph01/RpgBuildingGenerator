/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import util.Rect;
import java.util.ArrayList;
import org.joml.Rectangled;
import shapes.Shape;
import shapes.BuildingOutline;
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
        Rect bounds = findLargestRect(buildingOutline, w, h);
        //Rect bounds = new Rect(10,10, w-20, h-20);
                
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
        ArrayList<Vector2i> pointsToCheck = new ArrayList<>();
        ArrayList<Rectangled> rectangles = new ArrayList<>();
        Rectangled largestRect = new Rectangled(0,0,0,0);
        ArrayList<Vector2i> polygon;
        Rectangled bounds;
        Vector2i pointToCheck;
        PolygonHelper polygonHelper;
        
        polygon = buildingOutline.points();
        bounds = buildingOutline.boundingRect();
        polygonHelper = new PolygonHelper(polygon);
        pointsToCheck = calcPointstoCheck(bounds, polygon, polygonHelper);
        
        // find largest rect for each sample point
        for (Vector2i point : pointsToCheck) {
            rectangles.add(calcLargestRect(point, polygon, polygonHelper));
        }
        
        // Now pick best candidate based on size and aspect ratio

        for (Rectangled rect: rectangles) {
            double areaLargest, areaToCheck, aspectRatio;
            double w, h;
            
            w = rect.maxX - rect.minX;
            h = rect.maxY = rect.minX;
            areaToCheck =  w * h;
            aspectRatio = Math.max(w/h, h/w);
            
            w = largestRect.maxX - largestRect.minX;
            h = largestRect.maxY = largestRect.minX;
            areaLargest =  w * h;
            
            if (aspectRatio > 0.3 ) {
                if (areaToCheck > areaLargest) {
                    largestRect = rect;
                }
            }          
        }
        
        return new Rect(largestRect.minX, 
                        largestRect.minY, 
                        largestRect.maxX - largestRect.minX, 
                        largestRect.maxY - largestRect.minY);
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
        Rectangled rect = new Rectangled(pointToCheck.x, pointToCheck.y, pointToCheck.x, pointToCheck.y);
        
        // expand rectange until it intersects with polygon edge on both width and height.
        boolean expandwidthL = true;
        boolean expandwidthR = true;
        boolean expandheightT = true;
        boolean expandheightB = true;
        
        do
        {   
            Rectangled rectToTest = new Rectangled(rect);

            // Check if height and width can be expanded
            rectToTest.minY -= 1.0;
            if (expandheightT && polygonHelper.isRectInsidePolygon(rectToTest) == false) {
                expandheightT = false;
            }
            rectToTest = rect;
            rectToTest.maxY += 1.0;
            if (expandheightB && polygonHelper.isRectInsidePolygon(rectToTest) == false) {
                expandheightB = false;
            }
            rectToTest.minX -= 1.0;
            if (expandwidthL && polygonHelper.isRectInsidePolygon(rectToTest) == false) {
                expandwidthL = false;
            }
            rectToTest = rect;
            rectToTest.maxY += 1.0;
            if (expandwidthR && polygonHelper.isRectInsidePolygon(rectToTest) == false) {
                expandwidthR = false;
            }
            rectToTest = rect;
            if (expandwidthL) rect.minX -= 1.0;
            if (expandwidthR) rect.maxX += 1.0;
            if (expandheightT) rect.minY -= 1.0;
            if (expandheightB) rect.maxY -= 1.0;

        } while (expandwidthL || expandwidthR || expandheightT || expandheightB);   
         
        return rect;
    }
}

