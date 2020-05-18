/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.*;

class triangulate
{
	static final int MAXPOLY = 200;
	static final double EPSILON	= 0.000001;
	static class Point
	{
		double x,y;
	}
	
	static class Polygon
	{
		Point p[] = new Point[MAXPOLY];
		int n;
		Polygon()
		{
			for(int i=0;i<MAXPOLY;i++)
				p[i] = new Point();
		}
	}
	
	static class Triangulation
	{
		int n;
		int t[][] = new int[MAXPOLY][3];
	}
	
	static Point copy_point(Point p)
	{
		Point ret = new Point();
		ret.x = p.x;
		ret.y = p.y;
		return ret;
	}
	
	static class Triangle
	{
		Point a, b, c;
	}
	
	static double signed_triangle_area(Point a, Point b, Point c)
	{
		return ((a.x*b.y-a.y*b.x+a.y*c.x-a.x*c.y+b.x*c.y-c.x*b.y)/2.0);
	}

	static boolean cw(Point a, Point b, Point c)
	{
		return signed_triangle_area(a,b,c) < -EPSILON;
	}
	
	static boolean point_in_triangle(Point p, Triangle t)
	{
		if(cw(t.a,t.b,p)) return false;
		if(cw(t.b,t.c,p)) return false;
		if(cw(t.c,t.a,p)) return false;
		return true;
	}
	
	static boolean ear_Q(int i, int j, int k, Polygon p)
	{
		Triangle t = new Triangle();
		t.a = p.p[i];
		t.b = p.p[j];
		t.c = p.p[k];
		
		if(cw(t.a,t.b,t.c)) return false;
		
		for(int m=0;m<p.n;m++)
			if(m!=i&&m!=j&&m!=k)
				if(point_in_triangle(p.p[m],t))
					return false;
		
		return true;
	}
	
	static Triangulation Triangulate(Polygon p)
	{
		int l[] = new int [MAXPOLY];
		int r[] = new int [MAXPOLY];
		for(int i=0;i<p.n;i++)
		{
			l[i] = (i-1+p.n)%p.n;
			r[i] = (i+1+p.n)%p.n;
		}
		Triangulation t = new Triangulation();
		t.n = 0;
		int i = p.n-1;
		while(t.n < p.n - 2)
		{
			i = r[i];
			if(ear_Q(l[i],i,r[i],p))
			{
				t.t[t.n][0] = l[i];
				t.t[t.n][1] = i;
				t.t[t.n][2] = r[i];
				t.n++;
				l[r[i]] = l[i];
				r[l[i]] = r[i];
			}
		}
		return t;
	}
	
	static double triangle_area(Point a, Point b, Point c)
	{
		return Math.abs(signed_triangle_area(a,b,c));
	}
	
	static double area_triangulation(Polygon p)
	{
		Triangulation t = new Triangulation();
		double total = 0;
		t = Triangulate(p);
		for(int i=0;i<t.n;i++)
			total+=triangle_area(p.p[t.t[i][0]],p.p[t.t[i][1]],p.p[t.t[i][2]]);
		return total;
	}
	
	static double area(Polygon p)
	{
		double total = 0;
		for(int i=0;i<p.n;i++)
		{
			int j = (i+1)%p.n;
			total += (p.p[i].x*p.p[j].y) - (p.p[j].x*p.p[i].y);
		}
		return total / 2;
	}

}
