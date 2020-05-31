/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import designer.BuildingOutline;
import floorplanner.FloorPlanner;
import javax.swing.ProgressMonitor;

/**
 *
 * @author chrisralph
 */
public class Controller {
    
    private ProgressMonitor progressBar;
    private boolean loadingObjects = false;
    private final BuildingOutline buildingOutline = new BuildingOutline();
    private final FloorPlanner floorPlanner = new FloorPlanner();  
    public boolean showOutline = true;
    
    private boolean saveImage = false;
    private String imageFilename = null;
    
    public void setprogressBar(ProgressMonitor progressBar) {
        this.progressBar = progressBar;
    }

    public ProgressMonitor getProgressBar() {
        return this.progressBar;
    }
    
    public boolean getLoadingObjects() {
        return this.loadingObjects;
    }
    
    public void setLoadingObjects(boolean loadingObjects) {
        this.loadingObjects = loadingObjects;
    }    
    
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
