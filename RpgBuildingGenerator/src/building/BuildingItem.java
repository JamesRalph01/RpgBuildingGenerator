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
    private final Vector3f location;
    private final Vector3f rotation;
    public String texture;
    public String obj;
    public float scaleFactor;

    
    public BuildingItem() {
        minBounds = new Vector3f(0,0,0);
        maxBounds = new Vector3f(0,0,0);
        location = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }
    
    public Vector3f getMinBounds() {
        return minBounds;
    }
    
    public Vector3f getMaxBounds() {
        return maxBounds;
    }
    
    public void setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        minBounds.set(minX, minY, minZ);
        maxBounds.set(maxX, maxY, maxZ);
        scaleFactor = 0.01f;
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
    
    public Vector3f getLocation() {
        return location;
    }
    
    public void setLocation(float x, float y, float z) {
        this.location.set(x,y,z);
    }
    
    public Vector3f getRotation() {
        return rotation;
    }
    
    public void setRotation(float x, float y, float z) {
        this.rotation.set(x,y,z);
    }
    
}
