package util;

public class Rect {
    public double x, y, w, h;

    public Rect() {
        this(0,0,1,1);
    }

    public Rect(Rect r) {
        setRect(r.x, r.y, r.w, r.h);
    }

    public Rect(double x, double y, double w, double h) {
        setRect(x, y, w, h);
    }

    public Rect(int x, int y, int w, int h) {
        setRect((double)x, (double)y, (double)w, (double)h);
    }
    
    public double aspectRatio() {
        return Math.max(w/h, h/w);
    }

    public void setRect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    @Override
    public String toString() {
        return ("X: " + this.x + ", Y:" + this.y + ", W:" + this.w + ", H:" + this.h);
    }
}
