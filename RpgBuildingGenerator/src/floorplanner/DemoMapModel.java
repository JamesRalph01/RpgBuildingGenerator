package floorplanner;

import building.Room;
import java.util.stream.IntStream;

/**
 * Simple Map Model
 *
 */
public class DemoMapModel implements MapModel {

    Mappable[] items;

    /**
     * Creates a Map Model instance based on the relative size of the mappable items
     * and the frame size.
     *
     * @param itemRatio Items representing relative areas
     * @param width Width of the display area
     * @param height Height of the display area
     */
    public DemoMapModel(int[] itemRatio, double width, double height) {
        this.items = new MapItem[itemRatio.length];
        double totalArea = width * height;
        double sum = IntStream.of(itemRatio).sum();

        for (int i = 0; i < items.length; i++) {
            items[i] = new MapItem(totalArea / sum * itemRatio[i], 0, Room.RoomType.Empty, Room.AreaType.SOCIAL);
        }
    }

    
    @Override
    public Mappable[] getAreaRatios() {
        return this.items;
    }

    @Override
    public Mappable[] getSocialRatios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Mappable[] getServiceRatios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Mappable[] getPrivateRatios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkRoomConnection(Room.RoomType room, Room.RoomType toCheck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
