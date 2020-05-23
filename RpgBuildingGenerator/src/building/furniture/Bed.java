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
public class Bed extends BuildingItem{
    
    public Bed() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Wooden Bed/";
        this.objFilename = "wooden bed.obj";
        this.scaleFactor = 0.0005f;
        this.textures = new String[] {
            "textures/M_bed_BaseColor.png",
            "textures/M_pillow_blanket_BaseColor.png"}; 
    }    
    
}
