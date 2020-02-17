/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

/**
 *
 * @author chrisralph
 */
public class FloorPlanner {
    
    TreemapLayout algorithm;
    DemoMapModel mapModel;
    
    public void testTreemap(int w, int h) {
        Rect bounds = new Rect(0, 0, w, h);
        algorithm = new TreemapLayout();
        mapModel = new DemoMapModel(new int[] {6, 6, 4, 3, 2, 2, 1}, 1200, 800);
        algorithm.layout(mapModel, bounds);
    }
    
}
