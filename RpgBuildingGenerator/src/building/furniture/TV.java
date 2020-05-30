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
public class TV extends BuildingItem {

    public TV (FloorPlanner.BuildingTheme theme) {
        super();        
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/tv/Entertainment stand/";
                this.objFilename = "entertainment stand.obj";
                this.scaleFactor = 0.002f;                    
                break;
            case MEDIEVAL:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/tv/Entertainment stand/";
                this.objFilename = "entertainment stand.obj";
                this.scaleFactor = 0.002f;                    
                break;
            case FUTURISTIC:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/tv/Entertainment stand/";
                this.objFilename = "entertainment stand.obj";
                this.scaleFactor = 0.002f;                    
                break;
            default:
                break;
        }
        
    }    
    
}
