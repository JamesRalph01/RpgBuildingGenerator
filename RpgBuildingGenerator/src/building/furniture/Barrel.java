/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building.furniture;

import building.BuildingItem;

/**
 *
 * @author chrisralph
 */
public class Barrel extends BuildingItem {

    public Barrel() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.texture = "T_Table_Albedo.png";
        this.obj = "table.obj";
        this.scaleFactor = 0.1f;
    }
}
