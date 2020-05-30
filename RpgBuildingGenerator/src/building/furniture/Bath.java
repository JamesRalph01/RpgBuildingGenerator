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
public class Bath extends BuildingItem {
    
    public Bath (FloorPlanner.BuildingTheme theme) {
    
        super();        
        switch (theme) {
        case MODERN:
            // This bath crashes OpenGL!
            this.setBounds(0, 0, 5, 20, 20, 5);
            this.rootPath = "furniture/Bath/Modern Bath/";
            this.objFilename = "Modern Bath.obj";
            this.scaleFactor = 0.08f;                          
            break;
        case MEDIEVAL:        
            throw new UnsupportedOperationException("Not supported yet.");
        case FUTURISTIC:       
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }       
}
