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
public class Fire extends BuildingItem {

    public Fire(FloorPlanner.BuildingTheme theme, int wealthIndicator) {
        super();
        switch (theme) {
            case MODERN:

                break;
            case MEDIEVAL:
                if (wealthIndicator > 50) {
                    this.setBounds(0, 0, 5, 20, 20, 5);
                    this.rootPath = "furniture/Fire/Medieval fireplace/";
                    this.objFilename = "fireplace.obj";
                    this.scaleFactor = 0.001f;                  
                } else {
                    this.setBounds(0, 0, 5, 20, 20, 5);
                    this.rootPath = "furniture/Fire/Firepit/";
                    this.objFilename = "firepit.obj";
                    this.scaleFactor = 0.01f;                      
                }

                break;
            case FUTURISTIC:

                break;
            default:
                break;
        }
    }    
    
}
