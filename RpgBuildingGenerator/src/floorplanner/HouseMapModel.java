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
public class HouseMapModel implements MapModel {

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
        put(RoomType.LivingRoom,    new RoomType[]{RoomType.Kitchen,RoomType.MasterBedroom,
                                                   RoomType.Toilet,RoomType.DiningRoom,RoomType.SpareRoom});
        put(RoomType.Kitchen,       new RoomType[]{RoomType.LivingRoom,RoomType.Utility,
                                                   RoomType.DiningRoom,RoomType.MasterBedroom,RoomType.SpareRoom});
        put(RoomType.MasterBedroom, new RoomType[]{RoomType.LivingRoom,RoomType.Bathroom,
                                                   RoomType.SpareRoom,RoomType.Kitchen});
        put(RoomType.Bathroom,      new RoomType[]{RoomType.SpareRoom,RoomType.MasterBedroom,});
        put(RoomType.SpareRoom,     new RoomType[]{RoomType.MasterBedroom,RoomType.Bathroom,
                                                   RoomType.SpareRoom,RoomType.LivingRoom,RoomType.Kitchen});
        put(RoomType.Utility,       new RoomType[]{RoomType.Kitchen});
        put(RoomType.Toilet,        new RoomType[]{RoomType.LivingRoom,RoomType.DiningRoom});
        put(RoomType.DiningRoom,    new RoomType[]{RoomType.LivingRoom,RoomType.Kitchen,RoomType.Toilet});
    }};

    public HouseMapModel(double width, double height) {
  
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
                case "Li":
                    roomType = RoomType.LivingRoom;
                    areaType = AreaType.SOCIAL;
                    socialCount ++;
                    break;
                case "Dr":
                    roomType = RoomType.DiningRoom;
                    areaType = AreaType.SOCIAL;
                    socialCount ++;
                    break;
                case "Mb":
                    roomType = RoomType.MasterBedroom;
                    areaType = AreaType.PRIVATE;
                    privateCount ++;
                    break;
                case "Br":
                    roomType = RoomType.Bathroom;
                    areaType = AreaType.PRIVATE;
                    privateCount ++;
                    break;
                case "To":
                    roomType = RoomType.Toilet;
                    areaType = AreaType.SOCIAL;
                    socialCount ++;
                    break;
                case "Sr":
                    roomType = RoomType.SpareRoom;
                    areaType = AreaType.PRIVATE;
                    privateCount ++;
                    break;
                case "Ki":
                    roomType = RoomType.Kitchen;
                    areaType = AreaType.SERVICE;
                    serviceCount ++;
                    break;
                case "Ut":
                    roomType = RoomType.Utility;
                    areaType = AreaType.SERVICE;
                    serviceCount ++;
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
        
        this.roomRatio = new int[]{30,30,40}; // S,Se,P
        this.socialTotal = 0; this.serviceTotal = 0; this.privateTotal = 0;
        ArrayList<Integer> socialRatios = new ArrayList<>();
        ArrayList<String> socialLabels = new ArrayList<>();
        socialRatios.add(50); this.socialTotal += 50;
        socialLabels.add("Li"); // Living Room
        ArrayList<Integer> serviceRatios = new ArrayList<>();
        ArrayList<String> serviceLabels = new ArrayList<>();
        serviceRatios.add(50); this.serviceTotal += 50;
        serviceLabels.add("Ki"); // Kitchen
        ArrayList<Integer> privateRatios = new ArrayList<>();
        ArrayList<String> privateLabels = new ArrayList<>();
        privateRatios.add(100); this.privateTotal += 100;
        privateRatios.add(25); this.privateTotal += 25;
        privateLabels.add("Mb"); // Master Bedroom
        privateLabels.add("Br"); // Bathroom
        
        Random rand = new Random();
        
        if (totalArea > 30000) {
            // n = num of extra rooms needed
            int n = (int)(totalArea-30000)/10000;
            char[] availableRooms = {'T','U','U','S','S','S','D','D','D'};
            for (int i=0; i<n; i++) {
                if (i<=3) {
                    // Add additional Rooms
                    int randValue = rand.nextInt(availableRooms.length);
                    char room = availableRooms[randValue];
                    switch (room) {
                        case 'T':
                            // Toilet
                            socialRatios.add(20); this.socialTotal += 20;
                            this.roomRatio[0] += 10;
                            socialLabels.add("To");
                            break;
                        case 'U':
                            // Utility Room
                            serviceRatios.add(20); this.serviceTotal += 20;
                            this.roomRatio[1] += 10;
                            serviceLabels.add("Ut");
                            break;
                        case 'S':
                            // Spare Room
                            privateRatios.add(40); this.privateTotal += 40;
                            this.roomRatio[2] += 30;
                            privateLabels.add("Sr");
                            break;
                        case 'D':
                            // Dining Room
                            socialRatios.add(40); this.socialTotal += 40;
                            this.roomRatio[0] += 20;
                            socialLabels.add("Dr");
                            break;
                    }
                    availableRooms = removeElements(availableRooms,room);
                }
                if (i>3){
                    // Add extra bedrooms
                    privateRatios.add(40); this.privateTotal += 40;
                    this.roomRatio[2] += 30;
                    privateLabels.add("Sr");
                }
            }
        }      
        
        this.socialAreaRatio = this.roomRatio[0];
        this.serviceAreaRatio = this.roomRatio[1];
        this.privateAreaRatio = this.roomRatio[2];
        this.roomRatio = new int[socialRatios.size()+serviceRatios.size()+privateRatios.size()];
        this.labels = new String[socialLabels.size()+serviceLabels.size()+privateLabels.size()];
        
        // Add ratios to roomRatios
        int index=0, sum;
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
        
        /*System.out.println("Size = "+ totalArea);
        for (int i=0; i<this.roomRatio.length; i++) {
            System.out.print(this.roomRatio[i] + " ");
        }
        System.out.println("");
        for (String label : this.labels) {
            System.out.print(label + " ");
        }
        System.out.println("");*/
 
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
