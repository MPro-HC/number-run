package io.numberrun.Component;

import java.awt.Color;

import io.numberrun.UI.Graphics;

public class Oval implements Renderable {

    private float width;
    private float height;
    private boolean filled;
    private Color color;
    private float zOrder;

    public Oval(float width, float height, Color color) {
        this(width, height, color, true, 0);
    }

    public Oval(float width, float height, Color color, boolean filled) {
        this(width, height, color, filled, 0);
    }

    public Oval(float width, float height, Color color, boolean filled, float zOrder) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.filled = filled;
        this.zOrder = zOrder;
    }

    @Override
    public void render(Graphics g) {
        if (filled) {
            g.fillOval(-width / 2, -height / 2, width, height, color);
        } else {
            g.drawOval(-width / 2, -height / 2, width, height, color);
        }
    }

    @Override
    public float getZOrder() {
        return zOrder;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

}
