package io.numberrun.Component;

import java.awt.Color;

import io.numberrun.UI.Graphics;

/**
 * グラデーションで塗りつぶされた矩形を描画するコンポーネント
 */
public class GradientRectangle implements Renderable {

    private float width;
    private float height;
    private final Color initialColorTop;
    private final Color initialColorBottom;
    private Color colorTop;
    private Color colorBottom;
    private float zOrder;

    public GradientRectangle(float width, float height, Color colorTop, Color colorBottom) {
        this(width, height, colorTop, colorBottom, 0);
    }

    public GradientRectangle(float width, float height, Color colorTop, Color colorBottom, float zOrder) {
        this.width = width;
        this.height = height;
        this.initialColorTop = colorTop;
        this.initialColorBottom = colorBottom;
        this.colorTop = colorTop;
        this.colorBottom = colorBottom;
        this.zOrder = zOrder;
    }

    @Override
    public void render(Graphics g) {
        g.fillGradientRectVertical(-width / 2, -height / 2, width, height, colorTop, colorBottom);
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

    public Color getColorTop() {
        return colorTop;
    }

    public void setColorTop(Color colorTop) {
        this.colorTop = colorTop;
    }

    public Color getColorBottom() {
        return colorBottom;
    }

    public void setColorBottom(Color colorBottom) {
        this.colorBottom = colorBottom;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

    public void setOpacity(float opacity) {
        int alphaTop = Math.min(255, Math.max(0, (int) (initialColorTop.getAlpha() * opacity)));
        int alphaBottom = Math.min(255, Math.max(0, (int) (initialColorBottom.getAlpha() * opacity)));
        this.colorTop = new Color(
                initialColorTop.getRed(),
                initialColorTop.getGreen(),
                initialColorTop.getBlue(),
                alphaTop
        );
        this.colorBottom = new Color(
                initialColorBottom.getRed(),
                initialColorBottom.getGreen(),
                initialColorBottom.getBlue(),
                alphaBottom
        );
    }
}
