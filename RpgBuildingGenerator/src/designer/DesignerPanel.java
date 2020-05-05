/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import building.Room;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import main.Controller;
import util.Point;

/**
 *
 * @author chrisralph
 */
public class DesignerPanel extends JPanel implements MouseListener, MouseMotionListener {

    private HashMap<String, IDesignerComponent> designComponents;
    private Controller controller;
    private Point nearestGridPoint;
    
    public DesignerPanel() {
        designComponents = new HashMap<>();
        designComponents.put("Grid", new Grid());
        designComponents.put("GridCursor", new GridCursor()); 
        designComponents.put("EditCursorLine", new EditCursorLine()); 
        
        // Add listeners
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
        designComponents.put("BuildingOutline", controller.getBuildingOutLine());   
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background
        setBackground(Color.BLACK);  // set background color for this JPanel

        Set set = designComponents.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            IDesignerComponent component = (IDesignerComponent) mentry.getValue();
            component.paint(g);
        } 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        EditCursorLine editCursorLine = (EditCursorLine) designComponents.get("EditCursorLine");
        
        controller.getBuildingOutLine().addPoint(nearestGridPoint);
        editCursorLine.fromPoint(nearestGridPoint);
        
        if (controller.getBuildingOutLine().isComplete) {
            editCursorLine.setEnabled(false);
        } else {
            editCursorLine.setEnabled(true);         
        }
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
     }

    @Override
    public void mouseEntered(MouseEvent e) {
        IDesignerComponent gridCursor = designComponents.get("GridCursor");
        EditCursorLine editCursorLine = (EditCursorLine) designComponents.get("EditCursorLine");
        gridCursor.setEnabled(true);
        if (controller.getBuildingOutLine().size() > 0 &&
            controller.getBuildingOutLine().isComplete == false) {
            editCursorLine.setEnabled(true);
        } 
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        IDesignerComponent gridCursor = designComponents.get("GridCursor");
        EditCursorLine editCursorLine = (EditCursorLine) designComponents.get("EditCursorLine");
        gridCursor.setEnabled(false);  
        editCursorLine.setEnabled(false);
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        Point cursorPosition;
        
        Grid grid = (Grid) designComponents.get("Grid");
        GridCursor gridCursor = (GridCursor) designComponents.get("GridCursor");
        EditCursorLine editCursorLine = (EditCursorLine) designComponents.get("EditCursorLine");
        
        cursorPosition = new Point(e.getX(), e.getY());
        nearestGridPoint = grid.getNearestGridPoint(cursorPosition);
        gridCursor.cursorPosition(nearestGridPoint);
        
        editCursorLine.ToPoint(nearestGridPoint);
        
        this.repaint();
    }
    
    public void Generate() {
        controller.getFloorPlanner().generate(controller.getBuildingOutLine());
        designComponents.put("Floorplan", controller.getFloorPlanner().get2DFloorplan()); 
        this.repaint();
    }
    
    public void Clear() {
        EditCursorLine editCursorLine = (EditCursorLine) designComponents.get("EditCursorLine");
        controller.getBuildingOutLine().clear();
        controller.getFloorPlanner().clear();
        editCursorLine.setEnabled(false);
        
        designComponents.remove("Floorplan"); 
        this.repaint();
    }
}
