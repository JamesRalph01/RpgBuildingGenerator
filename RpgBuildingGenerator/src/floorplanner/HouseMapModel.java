/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import java.util.stream.IntStream;

/**
 *
 * @author chrisralph
 */
public class HouseMapModel implements MapModel {

    private MapItem[] items;

    public HouseMapModel(int width, int height) {
        int[] itemRatio = {4,5,1};
        
        
        this.items = new MapItem[itemRatio.length];
        double totalArea = width * height;
        double sum = IntStream.of(itemRatio).sum();

        for (int i = 0; i < items.length; i++) {
            items[i] = new MapItem(totalArea / sum * itemRatio[i], 0);
        }
    }
    
    @Override
    public Mappable[] getItems() {
        return items;
    }
    
}
