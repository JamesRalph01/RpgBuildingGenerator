/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;
import util.Rect;


public class BarTable extends BuildingItem {

    public BarTable() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Table/Bar Table/";
        this.objFilename = "bar table.obj";
        this.scaleFactor = 0.0020f;
    }  

}
