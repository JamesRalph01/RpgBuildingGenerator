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
    
    public FloorPlanner getFloorPlanner() {
        return this.floorPlanner;
    }
    
    public BuildingOutline getBuildingOutLine() {
        return this.buildingOutline;
    }
}
