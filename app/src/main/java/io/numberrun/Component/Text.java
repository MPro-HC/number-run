package io.numberrun.Component;

import java.awt.Color;
import java.awt.Font;
import io.numberrun.UI.Graphics;

/**
 * テキストを描画するコンポーネント
 */
public class Text implements Renderable {

    private String text;
    private Color color;
    private Font font;
    private int zOrder;

    public Text(String text, Color color) {
        this(text, color, new Font("SansSerif", Font.PLAIN, 14), 0);
    }

    public Text(String text, Color color, Font font) {
        this(text, color, font, 0);
    }

    public Text(String text, Color color, Font font, int zOrder) {
        this.text = text;
        this.color = color;
        this.font = font;
        this.zOrder = zOrder;
    }

    @Override
    public void render(Graphics g) {
        g.drawText(text, 0, 0, color, font);
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
}
