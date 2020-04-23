/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.joml.Rectangled;
import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */

public class ConvexHull extends ArrayList<Vector2i> {
    
    int status;
    private int start, stop; //tangents for iterative convex hull
    private int xmin,xmax,ymin,ymax;  //position of hull
    int yxmax; //y coord of xmax
    Vector2i rectp;
    int recth, rectw;
    boolean changed;
    
    /* largest rectangles with corners on AC, BD, ABC, ABD, ACD, BCD */
    ArrayList<Rect> RectList;  
    
    /* fixed aspect ratio */
    private boolean fixed;
    private int fixedX, fixedY;
    
    public ConvexHull() {
        this.fixed = false;
        this.fixedX = 1;
        this.fixedY = 1;
        RectList = new ArrayList<>();
    }
    
      public void addPointsToHull(ArrayList<Vector2i> points) {
        
        //this code is terrible!  ported from original
        
        points.forEach((Vector2i p) -> {
 
            if (this.size() <2){
                this.add(p);
            }

            else if (this.size() == 2){
                Vector2i ha = this.get(0);
                Vector2i hb = this.get(1);
                if (this.onLeft(ha, hb, p)){
                    this.add(p);
                }
                else{
                    this.add(1, p);
                    this.status = 2;
                }
            }
            else{
                if (this.addPointToHull(p)){
                    this.status = 2;
                }
                else{
                    this.status = 3;
                }
            }
        });
        

    }
    
    /* position of point w.r.t. hull edge
     * sign of twice the area of triangle abc
     */
    private boolean onLeft(Vector2i a, Vector2i b, Vector2i c){
        int area = (b.x -a.x)*(c.y - a.y) - (c.x - a.x)*(b.y - a.y);
        return (area<0);
    }
    
    /* check if point is outside
     * true is point is on right of all vertices
     * finds tangents if point is outside
     */
    private boolean pointOutside(Vector2i p){//, int start, int stop){
        
        boolean ptIn = true, currIn, prevIn = true;
        
        Vector2i a = (Vector2i)this.get(0);
        Vector2i b;
        
        for(int i=0; i<this.size(); i++){
            
            b = (Vector2i)this.get((i+1)%this.size());
            currIn = onLeft(a, b, p);
            ptIn = ptIn && currIn;
            a = b;
            
            if(prevIn && !currIn){ start = i;} /* next point outside, 1st tangent found */
            if(!prevIn && currIn){ stop = i;}  /* 2nd tangent */
            prevIn = currIn;
            
        }
        return !ptIn;
    }
    
    /* check if point is outside, insert it, maintaining general position */
    public boolean addPointToHull(Vector2i p) {
        
        /* index of tangents */
        start=0;
        stop=0;
        
        if(!pointOutside(p)){
            return false;
        }
        
        /* insert point */
        int numRemove;
        
        if (stop > start){
            numRemove = stop-start-1;
            if (numRemove>0){
                this.removeRange(start+1, stop);
            }
            this.add(start+1, p);
        }
        else{
            numRemove = stop+this.size()-start-1;
            if (numRemove > 0){
                if (start+1 < this.size()){
                    this.removeRange(start+1, this.size());
                }
                if(stop-1 >= 0){
                    this.removeRange(0, stop);
                }
            }
            this.add(p);
          
        }
        System.out.println("changing");
        this.changed = true;
        return true;
    } //addPointToHull
    
    /* compute edge list
     * set xmin, xmax
     * used to find largest rectangle by scanning horizontally
     */
    ArrayList<GeomEdge> computeEdgeList(){
        ArrayList<GeomEdge> l = new ArrayList<>();
        Vector2i a,b;
        GeomEdge e;
        
        a = this.get(this.size()-1);
        for(int i=0; i<this.size(); i++){
            b = this.get(i);
            //b = (Vector2i)this.elementAt(i+1);
            
            if (i==0){
                this.xmin = a.x;
                this.xmax = a.x;
                this.ymin = a.y;
                this.ymax = a.y;
            }
            else{
                if (a.x < this.xmin){
                    this.xmin = a.x;
                }
                if (a.x > this.xmax){
                    this.xmax  = a.x;
                    this.yxmax = a.y;
                }
                if (a.y < this.ymin){
                    this.ymin = a.y;
                }
                if (a.y > this.ymax){
                    this.ymax  = a.y;
                }
            }
            e = new GeomEdge(a,b);
            l.add(e);
            a = b;
        } //for
        // b = (Vector2i)this.elementAt(this.size()-1);
        // a = (Vector2i)this.elementAt(0);
        // e = new GeomEdge(b,a);
        // l.add(e);
        return l;
    }
    
    /* compute y intersection with an edge
     * first pixel completely inside
     * ceil function if edge is on top, floor otherwise
     * (+y is down)
     */
    private int yIntersect(int xi, GeomEdge e){
        
        int y;
        double yfirst = (e.m) * (xi-0.5) + e.b;
        double ylast = (e.m) * (xi+0.5) + e.b;
        
        if (!e.isTop){
            y = (int)Math.floor(Math.min(yfirst, ylast));
        }
        else {
            y = (int)Math.ceil(Math.max(yfirst, ylast));
        }
        return y;
    }
    
    /* find largest pixel completely inside
     * look through all edges for intersection
     */
    private int xIntersect(int y, ArrayList<GeomEdge> l){
        int x;
        double x0=0, x1=0;
        for(int i=0; i<this.size(); i++){
            GeomEdge e = l.get(i);
            if (e.isRight && e.ymin <= y && e.ymax >= y){
                x0 = (double)(y+0.5 - e.b)/e.m;
                x1 = (double)(y-0.5 - e.b)/e.m;
            }
        }
        x = (int)Math.floor(Math.min(x0,x1));
        //System.out.println("xIntersect, x is " + x);
        return x;
    }
    
    private GeomEdge findEdge(int x, boolean isTop, ArrayList<GeomEdge> l){
        GeomEdge e,emax=(GeomEdge)l.get(0);
        //int count = 0;
        for (int i=0; i<this.size(); i++){
            e = (GeomEdge)l.get(i);
            if (e.xmin == x){
                //count++;
                //if (count == 1){
                //    emax = e;
                //}
                //else{
                if (e.xmax != e.xmin){
                    if ((e.isTop && isTop)||(!e.isTop && !isTop)){
                        emax = e;
                    }
                }
            }
            
        }
        return emax;
    }
    
    /* compute 3 top and bottom 3 corner rectangle for each xi
     * find largest 2 corner rectangle
     */
    public void computeLargestRectangle(){
        
        this.changed = false;
        ArrayList<GeomEdge> edgeList = computeEdgeList();
        this.RectList = new ArrayList<>();
        
        GeomEdge top, bottom;
        int ymax, ymin, xright, xlo, xhi;
        int area, maxArea = 0;
        int maxAreaAC=0, maxAreaBD=0, maxAreaABC=0, maxAreaABD=0, maxAreaACD=0, maxAreaBCD=0;
        int width, height, maxh=0, maxw=0;
      
        /* all 2-corner and 3-corner largest rectangles */
        int aAC=0,aBD=0,aABC=0,aABD=0,aACD=0,aBCD=0;
        Vector2i pAC, pBD, pABC, pABD, pACD, pBCD;
        int hAC=0,wAC=0,hBD=0,wBD=0,hABC=0,wABC=0,hABD=0,wABD=0,hACD=0,wACD=0,hBCD=0,wBCD=0;
        boolean onA, onB, onC, onD;
        
        Vector2i maxp = new Vector2i(0,0);
        pAC = maxp; pBD = maxp; pABC = maxp; pABD = maxp; pACD = maxp; pBCD = maxp;
        
        ArrayList<Vector2i> xint = new ArrayList<>();
        
        for(int i=0;i<this.ymax;i++){
            int x = xIntersect(i,edgeList);
            Vector2i px = new Vector2i(x,i);
            xint.add(px);
        }
        //find first top and bottom edges
        top = findEdge(this.xmin, true, edgeList);
        bottom = findEdge(this.xmin, false, edgeList);
        
        //scan for rectangle left position
        for(int xi=this.xmin; xi<this.xmax;xi++){
            
            ymin = yIntersect(xi, top);
            ymax = yIntersect(xi, bottom);
            
            for(int ylo = ymax;ylo>=ymin;ylo--){//ylo from to to bottom
                
                for(int yhi = ymin; yhi<=ymax; yhi++){
                
                    if (yhi>ylo){
                        
                        onA = (yhi == ymax && !bottom.isRight);
                        onD = (ylo == ymin && !top.isRight);
                        
                        xlo = xint.get(ylo).x;//xIntersect(ylo,edgeList);
                        xhi = xint.get(yhi).x;//xIntersect(yhi,edgeList);
                        
                        xright = Math.min(xlo,xhi);
                        onC = (xright == xlo && this.yxmax >= ylo);
                        onB = (xright == xhi && this.yxmax <= yhi);
                        
                        height = yhi-ylo;
                        width = xright - xi;
                            
                        if (!this.fixed){                                                      
                        }//!fixed
                        else{
                          int fixedWidth = (int)Math.ceil( ((double) height*this.fixedX)/((double)this.fixedY));
                          if (fixedWidth <= width){
                              width = fixedWidth;
                          }
                          else{
                              width = 0;
                          }
                       }
                        area = width * height;
                        //AC 
                        if (onA && onC && !onB && !onD){                            
                            if (area > aAC){
                                aAC = area;
                                pAC = new Vector2i(xi, ylo);
                                hAC = height;
                                wAC = width;
                            }
                        }
                        //BD
                        if (onB && onD && !onA && !onC){
                            if (area > aBD){
                                aBD = area;
                                pBD = new Vector2i(xi, ylo);
                                hBD = height;
                                wBD = width;
                            }
                        }
                        //ABC
                        if (onA && onB && onC){
                            if (area > aABC){
                                aABC = area;
                                pABC = new Vector2i(xi, ylo);
                                hABC = height;
                                wABC = width;
                            }
                        }
                        //ABD
                        if (onA && onB && onD){
                            if (area > aABD){
                                aABD = area;
                                pABD = new Vector2i(xi, ylo);
                                hABD = height;
                                wABD = width;
                            }
                        }
                        //ACD
                        if (onA && onC && onD){
                            if (area > aACD){
                                aACD = area;
                                pACD = new Vector2i(xi, ylo);
                                hACD = height;
                                wACD = width;
                            }
                        }
                        //BCD
                        if (onB && onC && onD){
                            if (area > aBCD){
                                aBCD = area;
                                pBCD = new Vector2i(xi, ylo);
                                hBCD = height;
                                wBCD = width;
                            }
                        }
                        
                        if(area>maxArea){
                            maxArea = area;
                            maxp = new Vector2i(xi, ylo);
                            maxw = width;
                            maxh = height;
                           // System.out.println(onA + " " + onB + " " + onC + " " + onD);
                        }
                    }//yhi > ylo
                }//for yhi
            }//for ylo
            if (xi == top.xmax){
                top = findEdge(xi,  true, edgeList);
            }
            if(xi == bottom.xmax){
                bottom = findEdge(xi, false, edgeList);
            }
        }//xi
        
        this.rectp = maxp;
        this.recth = maxh;
        this.rectw = maxw;
       
        
        this.RectList.add(new Rect(pAC.x, pAC.y, wAC, hAC));
        this.RectList.add(new Rect(pBD.x, pBD.y, wBD, hBD));
        this.RectList.add(new Rect(pABC.x, pABC.y, wABC, hABC));
        this.RectList.add(new Rect(pABD.x, pABD.y, wABD, hABD));
        this.RectList.add(new Rect(pACD.x, pACD.y, wACD, hACD));
        this.RectList.add(new Rect(pBCD.x, pBCD.y, wBCD, hBCD));
        this.RectList.add(new Rect(maxp.x, maxp.y, maxw, maxh));
    }
    
    public ArrayList<Rect> rectangles() {
        return this.RectList;
    }
}
