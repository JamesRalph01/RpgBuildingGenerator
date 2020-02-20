/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import floorplanner.FloorPlanner;
import shapes.HouseOutline;

/**
 *
 * @author chrisralph
 */
public class Controller {
    
    private HouseOutline houseOutline = new HouseOutline();
    private FloorPlanner floorPlanner = new FloorPlanner();
    
    public FloorPlanner getFloorPlanner() {
        return this.floorPlanner;
    }
    
    public HouseOutline getHouseOutLine() {
        return this.houseOutline;
    }
}
