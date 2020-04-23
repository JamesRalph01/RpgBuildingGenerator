/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */
public class GeomPoint extends Vector2i {
    
    public GeomPoint(int ptx, int pty){
        this.x = ptx;
        this.y = pty;  
    }
    
    /*
    int min(int a, int b){
        if(a<=b) return a; else return b;
    }
    
    int max(int a, int b){
        if (a>=b) return a; else return b;
    } */
}