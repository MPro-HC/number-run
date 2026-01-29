package io.numberrun.Component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Optional;

import io.numberrun.UI.Graphics;

/**
 * テキストを描画するコンポーネント
 */
public class Text implements Renderable {

    private String text;
    private Color color;
    private Font font;
    private float zOrder;
    private boolean isCentered = true;
    private Optional<Float> borderWidth = Optional.empty();
    private Optional<Color> borderColor = Optional.empty();

    public Text(String text, Color color) {
        this(text, color, new Font("SansSerif", Font.PLAIN, 14), 0);
    }

    public Text(String text, Color color, Font font) {
        this(text, color, font, 0);
    }

    public Text(String text, Color color, Font font, float zOrder) {
        this.text = text;
        this.color = color;
        this.font = font;
        this.zOrder = zOrder;
    }

    public Text(String text, Color color, Font font, float zOrder, Color borderColor, float borderWidth) {
        this.text = text;
        this.color = color;
        this.font = font;
        this.zOrder = zOrder;
        this.borderColor = Optional.of(borderColor);
        this.borderWidth = Optional.of(borderWidth);
    }

    @Override
    public void render(Graphics g) {
        if (this.isCentered) {
            if (borderColor.isPresent() && borderWidth.isPresent()) {
                g.drawTextCentered(
                        text,
                        0,
                        0,
                        color,
                        font,
                        borderColor.get(),
                        borderWidth.get()
                );
                return;
            }

            g.drawTextCentered(text, 0, 0, color, font);
        } else {
            // どうせ使わない
            g.drawText(text, 0, 0, color, font);
        }
    }

    @Override
    public float getZOrder() {
        return zOrder;
    }

    @Override
    public float getWidth() {
        // フォントメトリクスを使用して高さを取得
        FontMetrics metrics = new Canvas().getFontMetrics(font);
        return metrics.stringWidth(text);
    }

    @Override
    public float getHeight() {
        // フォントメトリクスを使用して高さを取得
        FontMetrics metrics = new Canvas().getFontMetrics(font);
        return metrics.getHeight();
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

    public void setIsCentered(boolean isCentered) {
        this.isCentered = isCentered;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

}
