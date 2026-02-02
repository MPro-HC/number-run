package io.numberrun.Component;

import java.awt.Color;

import io.numberrun.UI.Graphics;

/**
 * 矩形を描画するコンポーネント
 */
public class Rectangle implements Renderable {

    private float width;
    private float height;
    private Color color;
    private boolean filled;
    private float zOrder;

    public Rectangle(float width, float height, Color color) {
        this(width, height, color, true, 0);
    }

    public Rectangle(float width, float height, Color color, boolean filled) {
        this(width, height, color, filled, 0);
    }

    public Rectangle(float width, float height, Color color, boolean filled, float zOrder) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.filled = filled;
        this.zOrder = zOrder;
    }

    @Override
    public void render(Graphics g) {
        if (filled) {
            g.fillRect(-width / 2, -height / 2, width, height, color);
        } else {
            g.drawRect(-width / 2, -height / 2, width, height, color);
        }
    }

    @Override
    public float getZOrder() {
        return zOrder;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }
}
