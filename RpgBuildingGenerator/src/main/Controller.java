/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import floorplanner.FloorPlanner;
import shapes.BuildingOutline;

/**
 *
 * @author chrisralph
 */
public class Controller {
    
    private final BuildingOutline buildingOutline = new BuildingOutline();
    private final FloorPlanner floorPlanner = new FloorPlanner();
    
    public FloorPlanner getFloorPlanner() {
        return this.floorPlanner;
    }
    
    public BuildingOutline getBuildingOutLine() {
        return this.buildingOutline;
    }
}
