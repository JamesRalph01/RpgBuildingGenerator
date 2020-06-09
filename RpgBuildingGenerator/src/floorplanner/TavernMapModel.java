/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanner;

import building.Room.RoomType;
import building.Room.AreaType;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chrisralph
 */
public class TavernMapModel implements MapModel {
    
    private MapItem[] privateRooms;
    private MapItem[] socialRooms;
    private MapItem[] serviceRooms;
    private MapItem[] totalAreas; // S, Se, P
    
    private int[] roomRatio;
    private String[] labels;
    
    private double totalArea;
    private int socialAreaRatio;
    private int serviceAreaRatio;
    private int privateAreaRatio;
    private int socialTotal;
    private int serviceTotal;
    private int privateTotal;
    private int numSocialRooms;
    private int numServiceRooms;
    private int numPrivateRooms;
    
    private Map<RoomType, RoomType[]> roomConnections = new HashMap<RoomType, RoomType[]>() {{
        put(RoomType.TavernFloor, new RoomType[]{RoomType.Kitchen,RoomType.Toilet});
        put(RoomType.Kitchen,     new RoomType[]{RoomType.TavernFloor,RoomType.StoreRoom});
        put(RoomType.Toilet,      new RoomType[]{RoomType.TavernFloor});
        put(RoomType.StoreRoom,   new RoomType[]{RoomType.Kitchen});
    }};

    public TavernMapModel(double width, double height) {
        this.totalArea = width * height;
        getRatios();
                
        // Make map model of 3 main areas
        this.totalAreas = new MapItem[3];
        double sum = this.socialAreaRatio + this.serviceAreaRatio + this.privateAreaRatio;
        System.out.println(totalArea + " : " + sum + " : " + socialAreaRatio);
        this.totalAreas[0] = new MapItem(totalArea / sum * this.socialAreaRatio, 0, RoomType.Empty, AreaType.SOCIAL);
        System.out.println(totalArea + " : " + sum + " : " + serviceAreaRatio);
        this.totalAreas[1] = new MapItem(totalArea / sum * this.serviceAreaRatio, 0, RoomType.Empty, AreaType.SERVICE);
        System.out.println(totalArea + " : " + sum + " : " + privateAreaRatio);
        this.totalAreas[2] = new MapItem(totalArea / sum * this.privateAreaRatio, 0, RoomType.Empty, AreaType.PRIVATE);
        
        
        // Calculate social, service and private map sizes
        this.socialRooms = new MapItem[this.numSocialRooms];
        this.serviceRooms = new MapItem[this.numServiceRooms];
        this.privateRooms = new MapItem[this.numPrivateRooms];
        
        int socialCount = -1, serviceCount = -1, privateCount = -1;
        for (int i = 0; i < roomRatio.length; i++) {

            RoomType roomType;
            AreaType areaType;
            switch (this.labels[i]) {
                case "Tf":
                    roomType = RoomType.TavernFloor;
                    areaType = AreaType.SOCIAL;
                    socialCount ++;
                    break;
                case "Ki":
                    roomType = RoomType.Kitchen;
                    areaType = AreaType.SERVICE;
                    serviceCount ++;
                    break;
                case "Sr":
                    roomType = RoomType.StoreRoom;
                    areaType = AreaType.SERVICE;
                    serviceCount ++;
                    break;
                case "To":
                    roomType = RoomType.Toilet;
                    areaType = AreaType.PRIVATE;
                    privateCount ++;
                    break;
                default:
                    roomType = RoomType.Empty;
                    areaType = AreaType.SOCIAL;
                    socialCount ++;
                    break;
            }
            // Add rooms to relevant map models
            switch (areaType) {
                case SOCIAL:
                    System.out.println("SOCIAL");
                    System.out.println(this.totalAreas[0].size + " : " + this.socialTotal + " : " + roomRatio[i]);
                    socialRooms[socialCount] = new MapItem(this.totalAreas[0].size / this.socialTotal * roomRatio[i], 0, roomType, areaType);
                    break;
                case SERVICE:
                    System.out.println("SERVICE");
                    System.out.println(this.totalAreas[1].size + " : " + this.serviceTotal + " : " + roomRatio[i]);
                    serviceRooms[serviceCount] = new MapItem(this.totalAreas[1].size / this.serviceTotal * roomRatio[i], 0, roomType, areaType);
                    break;
                case PRIVATE:
                    System.out.println("PRIVATE");
                    System.out.println(this.totalAreas[2].size + " : " + this.privateTotal + " : " + roomRatio[i]);
                    privateRooms[privateCount] = new MapItem(this.totalAreas[2].size / this.privateTotal * roomRatio[i], 0, roomType, areaType);
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }
    }
    
    @Override
    public Mappable[] getAreaRatios() {
        return this.totalAreas;
    }
    
    @Override
    public Mappable[] getSocialRatios() {
        return this.socialRooms;
    }
    
    @Override
    public Mappable[] getServiceRatios() {
        return this.serviceRooms;
    }
    
    @Override
    public Mappable[] getPrivateRatios() {
        return this.privateRooms;
    }

    private void getRatios() {
        this.roomRatio = new int[]{80,10,0}; // S,Se,P
        this.socialTotal = 0; this.serviceTotal = 0; this.privateTotal = 0;
        ArrayList<Integer> socialRatios = new ArrayList<>();
        ArrayList<String> socialLabels = new ArrayList<>();
        socialRatios.add(100); this.socialTotal += 100;
        socialLabels.add("Tf"); // Tavern Floor
        ArrayList<Integer> serviceRatios = new ArrayList<>();
        ArrayList<String> serviceLabels = new ArrayList<>();
        serviceRatios.add(70); this.serviceTotal += 70;
        serviceLabels.add("Ki"); // Kitchen
        ArrayList<Integer> privateRatios = new ArrayList<>();
        ArrayList<String> privateLabels = new ArrayList<>();
        
        if (totalArea > 40000) {
            // Toilet
            privateRatios.add(20); this.privateTotal += 20;
            this.roomRatio[2] += 10;
            privateLabels.add("To");
        }
        if (totalArea > 40000) {
            // StoreRoom
            serviceRatios.add(30); this.serviceTotal += 30;
            this.roomRatio[1] += 5;
            serviceLabels.add("Sr");
        }
        
        this.socialAreaRatio = this.roomRatio[0];
        this.serviceAreaRatio = this.roomRatio[1];
        this.privateAreaRatio = this.roomRatio[2];
        this.roomRatio = new int[socialRatios.size()+serviceRatios.size()+privateRatios.size()];
        this.labels = new String[socialLabels.size()+serviceLabels.size()+privateLabels.size()];
        
        // Add ratios to roomRatios
        int index=0, sum;
        Random rand = new Random();
        char[] availableSections = {'S','E','P'}; // E = service
        
        for (int it=0; it<3; it++) {
            sum=0;
            int randValue = rand.nextInt(availableSections.length);
            char section = availableSections[randValue];
            
            switch (section) {
                case 'S':
                    for (Integer i:socialRatios){
                        sum += i;
                    }   
                    for (int s=0; s<socialRatios.size(); s++){
                        this.roomRatio[index] = socialRatios.get(s);
                        this.labels[index] = socialLabels.get(s);
                        index++;
                    }   
                    break;
                case 'E':
                    for (Integer i:serviceRatios){
                        sum += i;
                    }   
                    for (int se=0; se<serviceRatios.size(); se++){
                        this.roomRatio[index] = serviceRatios.get(se);
                        this.labels[index] = serviceLabels.get(se);
                        index++;
                    }   
                    break;
                case 'P':
                    for (Integer i:privateRatios){
                        sum += i;
                    }   
                    for (int p=0; p<privateRatios.size(); p++){
                        this.roomRatio[index] = privateRatios.get(p);
                        this.labels[index] = privateLabels.get(p);
                        index++;
                    }   
                    break;
                default:
                    break;
            }
            availableSections = removeElements(availableSections,section);
        }
        this.numSocialRooms = socialRatios.size();
        this.numServiceRooms = serviceRatios.size();
        this.numPrivateRooms = privateRatios.size();
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
    
    @Override
    public boolean checkRoomConnection(RoomType room, RoomType toCheck) {
        for (RoomType type: this.roomConnections.get(room)) {
            if (type.equals(toCheck)) {
                return true;
            }
        }
        return false;
    }
    
}
