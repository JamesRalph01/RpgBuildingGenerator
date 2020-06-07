/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;


public class Barrels extends BuildingItem {

    public Barrels() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Store room barrels/";
        this.objFilename = "Store room barrels.obj";
        this.scaleFactor = 0.0025f;
    }
    
}
