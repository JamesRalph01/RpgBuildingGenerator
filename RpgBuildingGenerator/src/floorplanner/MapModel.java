package floorplanner;

import building.Room;

/**
 * Model object used by MapLayout to represent
 * data for a treemap.
 */
public interface MapModel
{
    /**
     * Get the list of items in this model.
     *
     * @return An array of the Mappable objects in this MapModel.
     */
    public Mappable[] getAreaRatios();
    public Mappable[] getSocialRatios();
    public Mappable[] getServiceRatios();
    public Mappable[] getPrivateRatios();
    public boolean checkRoomConnection(Room.RoomType room, Room.RoomType toCheck);
}