/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import shapes.Shape;

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
    
    public void generate(int w, int h) {
        Rect bounds = new Rect(0, 0, w, h);
        
        normalisedX = 1.8f / (float) w;
        normalisedY = 1.8f / (float) h;
        transX = normalisedX * Math.abs((float) w / 2);
        transY = normalisedY * Math.abs((float) h / 2);
        
        switch (buildingType) {
            case TAVERN:
                mapModel = new TavernMapModel(w, h);
                break;
            case CHURCH:
                mapModel = new DemoMapModel(new int[] {6, 6, 4, 3, 2, 2, 7,8, 1}, w, h);
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
            positionData[p++] = normalisedX * (float) rect.x - transX;
            positionData[p++] = normalisedY * (float) rect.y - transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) (rect.x + rect.w) - transX;
            positionData[p++] = normalisedY * (float) rect.y - transY;
            positionData[p++] = 1.0f;

            //2
            positionData[p++] = normalisedX * (float) (rect.x + rect.w) - transX;
            positionData[p++] = normalisedY * (float) rect.y - transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) (rect.x + rect.w) - transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) - transY;
            positionData[p++] = 1.0f;

            //3
            positionData[p++] = normalisedX * (float) (rect.x + rect.w) - transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) - transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) rect.x - transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) - transY;
            positionData[p++] = 1.0f;

            //4
            positionData[p++] = normalisedX * (float) rect.x - transX;
            positionData[p++] = normalisedY * (float) (rect.y + rect.h) - transY;
            positionData[p++] = 1.0f;

            positionData[p++] = normalisedX * (float) rect.x - transX;
            positionData[p++] = normalisedY * (float) rect.y - transY;
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
}
