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
    private String[] labels;

    public HouseMapModel(double width, double height) {
        this.roomRatio = new int[]{30,30,40}; // S,Se,P
        
        double totalArea = width * height;
        getRatios(totalArea);
        this.items = new MapItem[roomRatio.length];
        System.out.println("TOTAL AREA " + totalArea);
        double sum = IntStream.of(roomRatio).sum();

        for (int i = 0; i < items.length; i++) {
            items[i] = new MapItem(totalArea / sum * roomRatio[i], 0, this.labels[i]);
        }
    }
    
    @Override
    public Mappable[] getItems() {
        return items;
    }
    
    
    private void getRatios(double totalArea) {
        ArrayList<Integer> socialRatio = new ArrayList<>();
        ArrayList<String> socialLabels = new ArrayList<>();
        socialRatio.add(50);
        socialLabels.add("Li"); // Living Room
        ArrayList<Integer> serviceRatio = new ArrayList<>();
        ArrayList<String> serviceLabels = new ArrayList<>();
        serviceRatio.add(50);
        serviceLabels.add("Ki"); // Kitchen
        ArrayList<Integer> privateRatio = new ArrayList<>();
        ArrayList<String> privateLabels = new ArrayList<>();
        privateRatio.add(100);
        privateRatio.add(25);
        privateLabels.add("Mb"); // Master Bedroom
        privateLabels.add("Br"); // Bathroom
        
        Random rand = new Random();
        
        if (totalArea > 30000) {
            // n = num of extra rooms needed
            int n = (int)(totalArea-30000)/10000;
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
                        privateLabels.add("To");
                        break;
                    case 'U':
                        // Utility Room
                        serviceRatio.add(20);
                        this.roomRatio[1] += 10;
                        serviceLabels.add("Ut");
                        break;
                    case 'S':
                        // Spare Room
                        privateRatio.add(40);
                        this.roomRatio[2] += 30;
                        privateLabels.add("Sr");
                        break;
                    case 'D':
                        // Dining Room
                        socialRatio.add(40);
                        this.roomRatio[0] += 20;
                        socialLabels.add("Dr");
                        break;
                }
                
                availableRooms = removeElements(availableRooms,room);
            }
            if (n>4){
                // Add extra bedrooms
                for (int j=4; j<n; j++) {
                    privateRatio.add(40);
                    this.roomRatio[2] += 30;
                    privateLabels.add("Sr");
                    
                }
            }
        }      
                
        int sPercent = this.roomRatio[0];
        int sePercent = this.roomRatio[1];
        int pPercent = this.roomRatio[2];
        this.roomRatio = new int[socialRatio.size()+serviceRatio.size()+privateRatio.size()];
        this.labels = new String[socialLabels.size()+serviceLabels.size()+privateLabels.size()];
        
        // Add ratios to roomRatios
        int index=0, sum=0;
        char[] availableSections = {'S','E','P'}; // E = service
        
        for (int it=0; it<3; it++) {
            sum=0;
            int randValue = rand.nextInt(availableSections.length);
            char section = availableSections[randValue];
            
            switch (section) {
                case 'S':
                    for (Integer i:socialRatio){
                        sum += i;
                    }   
                    for (int s=0; s<socialRatio.size(); s++){
                        this.roomRatio[index] = socialRatio.get(s)/(sum/sPercent);
                        this.labels[index] = socialLabels.get(s);
                        index++;
                    }   
                    break;
                case 'E':
                    for (Integer i:serviceRatio){
                        sum += i;
                    }   
                    for (int se=0; se<serviceRatio.size(); se++){
                        this.roomRatio[index] = serviceRatio.get(se)/(sum/sePercent);
                        this.labels[index] = serviceLabels.get(se);
                        index++;
                    }   
                    break;
                case 'P':
                    for (Integer i:privateRatio){
                        sum += i;
                    }   
                    for (int p=0; p<privateRatio.size(); p++){
                        this.roomRatio[index] = privateRatio.get(p)/(sum/pPercent);
                        this.labels[index] = privateLabels.get(p);
                        index++;
                    }   
                    break;
                default:
                    break;
            }
            availableSections = removeElements(availableSections,section);
        }
        
        
        System.out.println("Size = "+ totalArea);
        for (int i=0; i<this.roomRatio.length; i++) {
            System.out.print(this.roomRatio[i] + " ");
        }
        System.out.println("");
        for (int j=0; j<this.labels.length; j++) {
            System.out.print(this.labels[j] + " ");
        }
        System.out.println("");
 
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
