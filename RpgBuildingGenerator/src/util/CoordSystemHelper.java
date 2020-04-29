/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.util.ArrayList;
import util.Point;
import org.joml.Vector2f;

/**
 *
 * @author chrisralph
 */
public class CoordSystemHelper {
    
    public static int deviceWidth, deviceHeight;
    private static float normalisedX, normalisedY;
    
    public static ArrayList<Point> openGLToDevice(ArrayList<Vector2f> openGLCoords) {
        float transX, transY;
        ArrayList<Point> deviceCoords = new ArrayList<>();
        

        transX = 1.0f;
        transY = 1.0f;
       
        openGLCoords.forEach((openGLPoint) -> {
            Point devicePoint = new Point();
            devicePoint.x = (int) ((openGLPoint.x + transX) / normalisedX);  
            devicePoint.y = (int) ((openGLPoint.y + transY) / normalisedY);
            deviceCoords.add(devicePoint);      
        });
        return deviceCoords;
    }
    
    public static Point openGLToDevice(Vector2f openGLPoint) {
        float transX, transY;
        
        transX = 1.0f;
        transY = 1.0f;
        
        Point devicePoint = new Point();
        devicePoint.x = (int) ((openGLPoint.x + transX) / normalisedX);  
        devicePoint.y = (int) -((openGLPoint.y + transY) / normalisedY);

        return devicePoint;
    }
    
    public static ArrayList<Vector2f> deviceToOpenGL(ArrayList<Point> deviceCoords) {
        float transX, transY;
        ArrayList<Vector2f> openGLCoords;
        
        transX = deviceWidth / 2;
        transY = deviceHeight / 2;
        
        openGLCoords = new ArrayList<>();
        
        deviceCoords.forEach((devicePoint) -> {
            Vector2f openGLPoint = new Vector2f();
            openGLPoint.x = (devicePoint.x - transX) * normalisedX;
            openGLPoint.y = -(devicePoint.y - transY) * normalisedY;
            openGLCoords.add(openGLPoint);
        });
        
        return openGLCoords;
    }
    
    public static Vector2f deviceToOpenGL(Point devicePoint) {
        float transX, transY;
        
        transX = deviceWidth / 2;
        transY = deviceHeight / 2;
        
        Vector2f openGLPoint = new Vector2f();
        openGLPoint.x = (devicePoint.x - transX) * normalisedX;
        openGLPoint.y = (devicePoint.y - transY) * normalisedY;

        return openGLPoint;
    }
    
    public static float[] deviceToOpenGLf(ArrayList<Point> deviceCoords) {
        float transX, transY;
        float[] openGLCoords;
        int i = 0;
        
        transX = deviceWidth / 2;
        transY = deviceHeight / 2;

        openGLCoords= new float[deviceCoords.size() * 3]; 
        for (Point point : deviceCoords) {
             openGLCoords[i++] = (point.x - transX) * normalisedX;
             openGLCoords[i++] = -(point.y - transY) * normalisedY;
             openGLCoords[i++] = 0.0f;  
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
