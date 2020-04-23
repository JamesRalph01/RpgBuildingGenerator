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
    
    public static ArrayList<Vector2i> openGLToDevice(int deviceWidth, int deviceHeight, ArrayList<Vector2f> openGLCoords) {
        float normalisedX, normalisedY;
        float transX, transY;
        ArrayList<Vector2i> deviceCoords = new ArrayList<>();
        
        openGLCoords.forEach((openGLPoint) -> {
            Vector2i devicePoint = new Vector2i();
            devicePoint.x = (int) (openGLPoint.x + 1.0f) * deviceWidth;
            devicePoint.y = (int) (openGLPoint.y + 1.0f) * deviceHeight;
            deviceCoords.add(devicePoint);      
        });
        return deviceCoords;
    }
    
    public static ArrayList<Vector2f> deviceToOpenGL(int deviceWidth, int deviceHeight, ArrayList<Vector2i> deviceCoords) {
        float normalisedX, normalisedY;
        float transX, transY;
        ArrayList<Vector2f> openGLCoords;
        
        openGLCoords = new ArrayList<>();
        
        deviceCoords.forEach((devicePoint) -> {
            Vector2f openGLPoint = new Vector2f();
            openGLPoint.x = 2.0f / (float) devicePoint.x - 1.0f;
            openGLPoint.y = 2.0f / (float) devicePoint.y - 1.0f;
            openGLCoords.add(openGLPoint);
        });
       
        /*
        normalisedX = 2.0f / (float) deviceWidth;
        normalisedY = 2.0f / (float) deviceHeight;
        
        transX = normalisedX * Math.abs((float) deviceWidth / 2);
        transY = normalisedY * Math.abs((float) deviceHeight / 2);
        
        deviceCoords.forEach((devicePoint) -> {
            Vector2f openGLPoint = new Vector2f();
            openGLPoint.x = normalisedX * (float) devicePoint.x - transX;
            openGLPoint.y = normalisedY * (float) devicePoint.y - transY;
            openGLCoords.add(openGLPoint);
        }); */
        
        return openGLCoords;
    }
}
