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
    private ArrayList<Vector2i> points = new ArrayList<>();
    
    public FloorPlanner() {
        this.buildingType = BuildingType.TAVERN;
    } 
    
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
    
    public void generate(BuildingOutline buildingOutline, int w, int h) {

        normalisedX = 2.0f / (float) w;
        normalisedY = 2.0f / (float) h;
        transX = -1.0f;
        transY = -1.0f;
        
        //Step 1: Find largest rectangle inside user drawn building outline
        PolygonHelper polygonHelper = new PolygonHelper(buildingOutline.points());
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
        Rect bounds;
        Mappable[] items = mapModel.getItems();
        
        points = new ArrayList<>();
        
        int p = 0;
        for (int i=0; i<items.length; i++) {
            bounds = items[i].getBounds();
            
            points.add(new Vector2i((int)bounds.x,(int)bounds.y));
            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y));

            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y));
            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));

            points.add(new Vector2i((int)bounds.x+(int)bounds.w,(int)bounds.y+(int)bounds.h));
            points.add(new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h));

            points.add(new Vector2i((int)bounds.x,(int)bounds.y+(int)bounds.h));
            points.add(new Vector2i((int)bounds.x,(int)bounds.y));     
        }
       
        return CoordSystemHelper.deviceToOpenGLf(points);
    }

    @Override
    public float[] getColourData() {

         float colourData[];
         int i = 0;
         
         colourData = new float[points.size() * 3];   
         //White 255,255,255
         for (Vector2i point : points) {
            colourData[i++] = 102f/255f;
            colourData[i++] = 224f/255f;
            colourData[i++] = 20f/255f;   
         }
        
        /*Mappable[] items = mapModel.getItems();
        float colourData[] = new float[items.length*3*8];
        int p = 0;
        while (p < colourData.length) {
            colourData[p++] = 102f/255f;
            colourData[p++] = 224f/255f;
            colourData[p++] = 20f/255f;                
        } */
        return colourData;
    }
    
    public int numbervertices() {
        return points.size();
        //return mapModel.getItems().length*8;
    }

}

