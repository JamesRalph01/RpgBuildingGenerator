/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import org.joml.Vector3f;
import util.Point;


public class BuildingItem {
    
    private final Vector3f minBounds;
    private final Vector3f maxBounds;
    private final Vector3f location;
    private final Vector3f rotation;
   
    public String rootPath; // Root folder path inside texture folder e.g. 'Furniture/Barrel/'
    public String[] textures;
    public String objFilename; // Obj filename only - assumed stored in rootPath folder
    public float scaleFactor;
    
    public float additionalRotation = 0;
    public float displacement = 0;
    public boolean placeInCentre = false;
    public boolean placeOnEdge = false;
    public boolean tavernBarPlacement = false;
    public boolean populateTavernFloor = false;
    public Point tavernTablePlacement; 

    
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
        scaleFactor = 1.0f;
    }
    
    public float getWidth() {
        return maxBounds.x = minBounds.x;
    }
    
    public float getHeight() {
        return maxBounds.y = minBounds.y;
    }
    
    public float getDepth() {
        return maxBounds.z = minBounds.z;    
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
