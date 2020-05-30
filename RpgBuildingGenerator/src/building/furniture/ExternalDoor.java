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
public class ExternalDoor extends BuildingItem 
{
    public ExternalDoor(FloorPlanner.BuildingTheme theme, int wealthIndicator) {
        super();
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/Modern External Door/";
                this.objFilename = "door.obj";
                this.scaleFactor = 0.0008f;
                break;
            case MEDIEVAL:
                throw new UnsupportedOperationException("Not supported yet.");
            case FUTURISTIC:
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }      
}
