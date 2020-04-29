package floorplanner;

import util.Rect;

/**
 * A simple implementation of the Mappable interface.
 */
public class MapItem implements Mappable {
	double size;
	Rect bounds;
	int order = 0;
	int depth;
        String roomType;

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public MapItem() {
		this(1, 0, "NA");
	}

	public MapItem(double size, int order, String roomType) {
		this.size = size;
		this.order = order;
		bounds = new Rect();
                this.roomType = roomType;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public Rect getBounds() {
		return bounds;
	}

	public void setBounds(Rect bounds) {
		this.bounds = bounds;
	}

	public void setBounds(double x, double y, double w, double h) {
		bounds.setRect(x, y, w, h);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
        
        public String getRoomType() {
            return this.roomType;
        }
}
