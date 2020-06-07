/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import floorplanner.FloorPlanner;

public class SingleBed extends BuildingItem{
    
    public SingleBed(FloorPlanner.BuildingTheme theme) {
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
                this.rootPath = "furniture/bed/old bed/";
                this.objFilename = "old_bed.obj";
                this.scaleFactor = 0.0005f;                
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