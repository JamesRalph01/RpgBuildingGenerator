/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.util.ArrayList;
import org.joml.Vector2i;
import org.joml.Vector2f;

/**
 *
 * @author chrisralph
 */
public class CoordSystemHelper {
    
    public static int deviceWidth, deviceHeight;
    private static float normalisedX, normalisedY;
    
    public static ArrayList<Vector2i> openGLToDevice(ArrayList<Vector2f> openGLCoords) {
        float transX, transY;
        ArrayList<Vector2i> deviceCoords = new ArrayList<>();
        

        transX = 1.0f;
        transY = 1.0f;
       
        openGLCoords.forEach((openGLPoint) -> {
            Vector2i devicePoint = new Vector2i();
            devicePoint.x = (int) ((openGLPoint.x + transX) / normalisedX);  
            devicePoint.y = (int) ((openGLPoint.y + transY) / normalisedY);
            deviceCoords.add(devicePoint);      
        });
        return deviceCoords;
    }
    
    public static Vector2i openGLToDevice(Vector2f openGLPoint) {
        float transX, transY;
        
        transX = 1.0f;
        transY = 1.0f;
        
        Vector2i devicePoint = new Vector2i();
        devicePoint.x = (int) ((openGLPoint.x + transX) / normalisedX);  
        devicePoint.y = (int) ((openGLPoint.y + transY) / normalisedY);

        return devicePoint;
    }
    
    public static ArrayList<Vector2f> deviceToOpenGL(ArrayList<Vector2i> deviceCoords) {
        float transX, transY;
        ArrayList<Vector2f> openGLCoords;
        
        transX = -1.0f;
        transY = -1.0f;
        
        openGLCoords = new ArrayList<>();
        
        deviceCoords.forEach((devicePoint) -> {
            Vector2f openGLPoint = new Vector2f();
            openGLPoint.x = normalisedX * (float) devicePoint.x + transX;
            openGLPoint.y = normalisedY * (float) devicePoint.y + transY;
            openGLCoords.add(openGLPoint);
        });
        
        return openGLCoords;
    }
    
    public static Vector2f deviceToOpenGL(Vector2i devicePoint) {
        float transX, transY;
        
        transX = -1.0f;
        transY = -1.0f;
        
        Vector2f openGLPoint = new Vector2f();
        openGLPoint.x = normalisedX * (float) devicePoint.x + transX;
        openGLPoint.y = normalisedY * (float) devicePoint.y + transY;

        return openGLPoint;
    }
    
    public static float[] deviceToOpenGLf(ArrayList<Vector2i> deviceCoords) {
        float transX, transY;
        float[] openGLCoords;
        int i = 0;
        
        transX = -1.0f;
        transY = -1.0f;

        openGLCoords= new float[deviceCoords.size() * 3]; 
        for (Vector2i point : deviceCoords) {
             openGLCoords[i++] = point.x;
             openGLCoords[i++] = point.y;
             openGLCoords[i++] = 1.0f;  
         }        
        return openGLCoords;
    }
    
    public static void initDevice(int width, int height) {
        deviceWidth = width;
        deviceHeight = height;
        normalisedX = 2.0f / (float) deviceWidth;
        normalisedY = 2.0f / (float) deviceHeight;
    }
}
