/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import floorplanner.FloorPlanner;

/**
 *
 * @author chrisralph
 */
public class DoubleBed extends BuildingItem{
    
    public DoubleBed(FloorPlanner.BuildingTheme theme) {
        super();        
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/bed/bed with side tables/";
                this.objFilename = "bed.obj";
                this.scaleFactor = 0.07f;                    
                break;
            case MEDIEVAL:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/bed/bed with side tables/";
                this.objFilename = "bed.obj";
                this.scaleFactor = 0.07f;                
                break;
            case FUTURISTIC:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/bed/bed with side tables/";
                this.objFilename = "bed.obj";
                this.scaleFactor = 0.07f;                
                break;
        }    
    
    }
}