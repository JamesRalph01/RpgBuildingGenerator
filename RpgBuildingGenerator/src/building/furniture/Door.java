/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import floorplanner.FloorPlanner.BuildingTheme;

/**
 *
 * @author chrisralph
 */
public class Door extends BuildingItem {
    
    public Door(BuildingTheme theme) {
        super();
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/metal door/";
                this.objFilename = "metal door.obj";
                this.scaleFactor = 0.025f;
                break;
            case MEDIEVAL:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/Medieval_Door/";
                this.objFilename = "Medieval door.obj";
                this.scaleFactor = 0.0020f;
                break;
            case FUTURISTIC:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/SciFi door/";
                this.objFilename = "scifi.obj";
                this.scaleFactor = 0.040f;
                break;
            default:
                break;
        }
    }    
}
