/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import floorplanner.FloorPlanner;

public class Fridge extends BuildingItem {
    
    public Fridge (FloorPlanner.BuildingTheme theme) {
        super();        
        switch (theme) {
            case MODERN:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Fridge/Double_Door_Fridge/";
                this.objFilename = "Fridge.obj";
                this.scaleFactor = 0.0008f;                    
                break;
            case MEDIEVAL:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Fridge/Double_Door_Fridge/";
                this.objFilename = "Fridge.obj";
                this.scaleFactor = 0.0008f;                    
                break;
            case FUTURISTIC:
                this.setBounds(0, 0, 5, 20, 20, 5);
                this.rootPath = "furniture/Fridge/Double_Door_Fridge/";
                this.objFilename = "Fridge.obj";
                this.scaleFactor = 0.0008f;                    
                break;
        }        
    }       
}
