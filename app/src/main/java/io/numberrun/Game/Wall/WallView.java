package io.numberrun.Game.Wall;

import java.awt.Color;
import java.awt.Font;

import io.numberrun.Component.Renderable;
import io.numberrun.Component.Text;
import io.numberrun.UI.Graphics;

public class WallView implements Renderable {

    private final float width;
    private final float height;
    private Color backgroundColor;
    private Color borderColor;
    private final Text text;
    private int zOrder = 0;

    public WallView(float width, float height, Color backgroundColor, Color borderColor, Color textColor, String text) {
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.text = new Text(text, textColor, new Font("SansSerif", Font.BOLD, 48)); // デフォルトで黒色のテキスト
    }

    @Override
    public void render(Graphics g) {
        // 背景
        g.fillRect(-width / 2, -height / 2, width, height, backgroundColor);

        // 枠
        g.drawRect(-width / 2, -height / 2, width, height, borderColor);

        // テキスト
        this.text.render(g);
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public WallView setText(String text) {
        this.text.setText(text);
        return this;
    }

    public WallView setBackgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    public void setTextColor(Color color) {
        this.text.setColor(color);
    }

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

}
