/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.ArrayList;

/**
 *
 * @author chrisralph
 */
public class HouseMapModel implements MapModel {

    private MapItem[] items;
    private int[] roomRatio;

    public HouseMapModel(double width, double height) {
        this.roomRatio = new int[]{30,30,40}; // S,Se,P
        
        double totalArea = width * height;
        
        this.items = new MapItem[roomRatio.length];
        System.out.println("TOTAL AREA " + totalArea);
        double sum = IntStream.of(roomRatio).sum();

        for (int i = 0; i < items.length; i++) {
            items[i] = new MapItem(totalArea / sum * roomRatio[i], 0);
        }
    }
    
    @Override
    public Mappable[] getItems() {
        return items;
    }
    
    private void getRatios(double totalArea) {
        ArrayList<Integer> socialRatio = new ArrayList<>();
        socialRatio.add(50);
        ArrayList<Integer> serviceRatio = new ArrayList<>();
        serviceRatio.add(50);
        ArrayList<Integer> privateRatio = new ArrayList<>();
        privateRatio.add(50);
        privateRatio.add(25);
        
        if (totalArea > 30000) {
            // n = num of extra rooms needed
            int n = (int)(totalArea-30000)/10000;
            Random rand = new Random(); 
            char[] availableRooms = {'T','U','U','S','S','S','D','D','D'};
            for (int i=0; i<n; i++) {
                // Add additional Rooms
                int randValue = rand.nextInt(availableRooms.length);
                char room = availableRooms[randValue];
                switch (room) {
                    case 'T':
                        // Toilet
                        privateRatio.add(20);
                        this.roomRatio[2] += 10;
                        break;
                    case 'U':
                        // Utility Room
                        serviceRatio.add(20);
                        this.roomRatio[1] += 10;
                        break;
                    case 'S':
                        // Spare Room
                        privateRatio.add(40);
                        this.roomRatio[2] += 20;
                        break;
                    case 'D':
                        // Dining Room
                        socialRatio.add(40);
                        this.roomRatio[0] += 20;
                        break;
                }
                
                availableRooms = removeElements(availableRooms,room);
            }
            if (n>4){
                // Add extra bedrooms
                for (int j=4; j<n; j++) {
                    privateRatio.add(40);
                    this.roomRatio[2] += 20;
                    
                }
            }
        }
        socialratio = 40,3,42
        30
        
        newvalue = 40+3+42/40 * 
        85/30 = 2.833 
        40/2.833 = 14.12        
                
        int sPercent = this.roomRatio[0];
        int sePercent = this.roomRatio[1];
        int pPercent = this.roomRatio[2];
        this.roomRatio = new int[socialRatio.size()+serviceRatio.size()+privateRatio.size()];
        // Add ratios to roomRatios
        int x=0;
        for (Integer s:socialRatio){
            this.roomRatio[x]
        }
        
    }
    
    private static char[] removeElements(char[] arr, char key) 
    { 
          // Move all other elements to beginning  
          int index = 0; 
          for (int i=0; i<arr.length; i++) 
             if (arr[i] != key) 
                arr[index++] = arr[i]; 
  
         // Create a copy of arr[]  
         return Arrays.copyOf(arr, index); 
    } 
    
}
