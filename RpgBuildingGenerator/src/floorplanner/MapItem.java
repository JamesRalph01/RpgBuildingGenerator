package floorplanner;

import building.Room;
import util.Rect;

/**
 * A simple implementation of the Mappable interface.
 */
public class MapItem implements Mappable {
	double size;
	Rect bounds;
	int order = 0;
	int depth;
        //String roomType;
        Room.RoomType roomType;
        Room.AreaType areaType;

        @Override
	public void setDepth(int depth) {
		this.depth = depth;
	}

        @Override
	public int getDepth() {
		return depth;
	}

	public MapItem() {
		this(1, 0, Room.RoomType.Empty, Room.AreaType.SOCIAL);
	}

	public MapItem(double size, int order, Room.RoomType roomType, Room.AreaType areaType) {
		this.size = size;
		this.order = order;
		bounds = new Rect();
                this.roomType = roomType;
                this.areaType = areaType;
	}

        @Override
	public double getSize() {
		return size;
	}

        @Override
	public void setSize(double size) {
		this.size = size;
	}

        @Override
	public Rect getBounds() {
		return bounds;
	}

        @Override
	public void setBounds(Rect bounds) {
		this.bounds = bounds;
	}

        @Override
	public void setBounds(double x, double y, double w, double h) {
		bounds.setRect(x, y, w, h);
	}

        @Override
	public int getOrder() {
		return order;
	}

        @Override
	public void setOrder(int order) {
		this.order = order;
	}
        
        @Override
        public Room.RoomType getRoomType() {
            return this.roomType;
        }
        
        @Override
        public Room.AreaType getAreaType() {
            return this.areaType;
        }
}
