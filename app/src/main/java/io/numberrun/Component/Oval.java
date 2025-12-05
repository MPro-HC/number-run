package io.numberrun.Component;

import java.awt.Color;
import io.numberrun.UI.Graphics;

/**
 * 円/楕円を描画するコンポーネント
 */
public class Oval implements Renderable {

    private float width;
    private float height;
    private Color color;
    private boolean filled;
    private int zOrder;

    public Oval(float radius, Color color) {
        this(radius * 2, radius * 2, color, true, 0);
    }

    public Oval(float width, float height, Color color) {
        this(width, height, color, true, 0);
    }

    public Oval(float width, float height, Color color, boolean filled, int zOrder) {
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
    public int getZOrder() {
        return zOrder;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

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

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
}
