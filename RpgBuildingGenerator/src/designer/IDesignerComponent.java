/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Graphics;

/**
 *
 * @author chrisralph
 */
public interface IDesignerComponent {

    public boolean getEnabled();
    public void setEnabled(boolean enabled);
    public void paint(Graphics g);
    
}
