/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

/**
 *
 * @author chrisralph
 */
public class Door extends BuildingItem {
    
    public Door() {
        super();
        this.setBounds(0, 0, 5, 20, 20, 5);
        this.rootPath = "furniture/Door/Medieval_Door/";
        this.objFilename = "Medieval door.obj";
        this.scaleFactor = 0.0020f;
        this.textures = new String[] {
            "Privy Council Door.png"}; 
    }    
}
