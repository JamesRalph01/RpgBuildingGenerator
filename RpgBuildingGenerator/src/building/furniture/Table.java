/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;


public class Table extends BuildingItem {
    
    public Table() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Moroccan table/";
        this.objFilename = "Moroccan table.obj";
        this.scaleFactor = 0.0020f;
    }    
}
