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
public class DiningTable extends BuildingItem {
    
   public DiningTable (FloorPlanner.BuildingTheme theme) {
        super();        
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/table/Modern Dining Table/";
                this.objFilename = "modern dining table.obj";
                this.scaleFactor = 0.0008f;                          
                break;
            case MEDIEVAL:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/table/Medieval Table/";
                this.objFilename = "Medieval table.obj";
                this.scaleFactor = 0.010f;            
                break;
            case FUTURISTIC:       
                throw new UnsupportedOperationException("Not supported yet.");
        }    
    }        
}
