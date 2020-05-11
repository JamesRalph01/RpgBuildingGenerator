/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import org.joml.Vector3f;

/**
 *
 * @author chrisralph
 */
public class BuildingItem {
    
    private final Vector3f minBounds;
    private final Vector3f maxBounds;
    
    public BuildingItem() {
        minBounds = new Vector3f(0,0,0);
        maxBounds = new Vector3f(0,0,0);
    }
    
    public Vector3f getMinBounds() {
        return minBounds;
    }
    
    public Vector3f getMaxBounds() {
        return maxBounds;
    }
    
    public float getWidth() {
        return maxBounds.x = minBounds.x;
    }
    
    public float getHeight() {
        return maxBounds.z = minBounds.z;
    }
    
    public float getDepth() {
        return maxBounds.y = minBounds.y;    
    }
    

    
}
