package io.numberrun.Component;

import java.awt.Color;

import io.numberrun.UI.Graphics;

public class Circle implements Renderable {

    private float radius;
    private boolean filled;
    private Color color;
    private int zOrder;

    public Circle(float radius, Color color) {
        this(radius, color, true, 0);
    }

    public Circle(float radius, Color color, boolean filled) {
        this(radius, color, filled, 0);
    }

    public Circle(float radius, Color color, boolean filled, int zOrder) {
        this.radius = radius;
        this.color = color;
        this.filled = filled;
        this.zOrder = zOrder;
    }

    @Override
    public void render(Graphics g) {
        if (filled) {
            g.fillOval(0, 0, radius * 2, radius * 2, color);
        } else {
            g.drawOval(0, 0, radius * 2, radius * 2, color);
        }
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }

    @Override
    public float getWidth() {
        return radius * 2;
    }

    @Override
    public float getHeight() {
        return radius * 2;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
