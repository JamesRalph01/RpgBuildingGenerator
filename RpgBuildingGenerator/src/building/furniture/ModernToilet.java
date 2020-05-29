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
public class ModernToilet extends BuildingItem {

    public ModernToilet() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Modern Toilet/";
        this.objFilename = "hsdc00.obj";
        this.scaleFactor = 0.003f;
    }    
    
}