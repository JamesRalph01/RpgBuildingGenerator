/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;


public class SciFiDoor extends BuildingItem {

    public SciFiDoor() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Door/SciFi door/";
        this.objFilename = "scifi.obj";
        this.scaleFactor = 0.040f;
    }    
    
}
