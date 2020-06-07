/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import floorplanner.FloorPlanner.BuildingTheme;


public class Door extends BuildingItem {
    
    public Door(BuildingTheme theme, int wealthIndicator) {
        super();
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/Modern door/";
                this.objFilename = "Modern door.obj";
                this.scaleFactor = 0.004f;
                break;
            case MEDIEVAL:
                if (wealthIndicator > 50) {
                    this.setBounds(0, 0, 5, 20, 20, 5);
                    this.rootPath = "furniture/Door/Medieval_Door/";
                    this.objFilename = "Medieval door.obj";
                    this.scaleFactor = 0.0020f;                    
                } else {
                    this.setBounds(0, 0, 5, 20, 20, 5);
                    this.rootPath = "furniture/Door/Poor medieval door/";
                    this.objFilename = "Poor medieval door.obj";
                    this.scaleFactor = 0.07f;                      
                }

                break;
            case FUTURISTIC:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Door/SciFi door/";
                this.objFilename = "scifi.obj";
                this.scaleFactor = 0.040f;
                break;
        }
    }    
}
