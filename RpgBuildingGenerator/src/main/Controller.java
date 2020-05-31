/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import designer.BuildingOutline;
import floorplanner.FloorPlanner;

/**
 *
 * @author chrisralph
 */
public class Controller {
       
    private final BuildingOutline buildingOutline = new BuildingOutline();
    private final FloorPlanner floorPlanner = new FloorPlanner();  
    public boolean showOutline = true;
    
    private boolean saveImage = false;
    private String imageFilename = null;
    
    public boolean getSaveImage() {
        return this.saveImage;
    }
    
    public void setSaveImage(boolean saveImage) {
        this.saveImage = saveImage;
    }

    public String getimageFilename() {
        return this.imageFilename;
    }
    
    public void setimageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
    
    public FloorPlanner getFloorPlanner() {
        return this.floorPlanner;
    }
    
    public BuildingOutline getBuildingOutLine() {
        return this.buildingOutline;
    }
}
