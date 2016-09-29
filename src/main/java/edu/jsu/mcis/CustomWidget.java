package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    
    private final Color DEFAULT_COLOR = Color.white;
    private final Color HEXAGON_SELECTED_COLOR = Color.green;
    private final Color OCTAGON_SELECTED_COLOR = Color.red;
    
    public boolean hexagonSelected, octagonSelected;    
    private Point[] hexagonVertices, octagonVertices;


    public CustomWidget() {
        observers = new ArrayList<>();
        hexagonSelected = true;
        octagonSelected = false;

        hexagonVertices = new Point[6];
        for(int i = 0; i < hexagonVertices.length; i++) { hexagonVertices[i] = new Point(); }

        octagonVertices = new Point[8];
        for(int i = 0; i < octagonVertices.length; i++) { octagonVertices[i] = new Point(); }

        Dimension dim = getPreferredSize();
        calculateVertices(hexagonVertices, dim.width, dim.height, 0, dim.width/3, dim.height/2);
        calculateVertices(octagonVertices, dim.width, dim.height, Math.PI*0.125, dim.width - (dim.width/3), dim.height/2);
                
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }


    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }

    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(hexagonSelected, octagonSelected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(
        Point[] vertices, int width, int height, double translation, int horizontalAlign, int verticalAlign) {
            int side = Math.min(width, height) / 2;
            for(int i = 0; i < vertices.length; i++) {
                double t = translation + (i * (Math.PI / (vertices.length / 2)));
                double x = Math.cos(t);
                double y = Math.sin(t);
                vertices[i].setLocation(
                    horizontalAlign + (x * (side/4)),
                    verticalAlign + (y * (side/4))
                );
            }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int width = getWidth();
        int height = getHeight();

        calculateVertices(hexagonVertices, width, height, 0, width/3, height/2);
        Shape shape = getPolygonShapes(hexagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (hexagonSelected) {
            g2d.setColor(HEXAGON_SELECTED_COLOR);
        } else {
            g2d.setColor(DEFAULT_COLOR);
        }
        g2d.fill(shape);

        calculateVertices(
            octagonVertices, width, height, Math.PI * 0.125, width - (width/3), height/2
        );
        shape = getPolygonShapes(octagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (octagonSelected) {
            g2d.setColor(OCTAGON_SELECTED_COLOR);
        } else {
            g2d.setColor(DEFAULT_COLOR);
        }

        g2d.fill(shape);
    
    }

    public void mouseClicked(MouseEvent event) {
        Shape shape = getPolygonShapes(hexagonVertices);
        if(shape.contains(event.getX(), event.getY())) {
            hexagonSelected = !hexagonSelected;
            octagonSelected = !hexagonSelected;
            notifyObservers();
            repaint(shape.getBounds());
        } else {
            shape = getPolygonShapes(octagonVertices);
            if(shape.contains(event.getX(), event.getY())) {
                octagonSelected = !octagonSelected;
                hexagonSelected = !octagonSelected;
                notifyObservers();
                repaint(shape.getBounds());
            }
        }
    }

    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    public Shape[] getShapes() {
        return new Shape[] {
            getPolygonShapes(hexagonVertices),
            getPolygonShapes(octagonVertices)
        };
    }


    public Shape getPolygonShapes(Point[] vertices) {
        int[] x = new int[vertices.length];
        int[] y = new int[vertices.length];
        
        for(int i = 0; i < vertices.length; i++) {
            x[i] = vertices[i].x;
            y[i] = vertices[i].y;
        }
        Shape shape = new Polygon(x, y, vertices.length);
        
        return shape;
    }
}
