package io.numberrun.Game.Wall;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import io.numberrun.Component.Renderable;
import io.numberrun.Component.Text;
import io.numberrun.UI.Graphics;

public class WallView implements Renderable {

    private final float width;
    private final float height;

    private List<Color> backgroundColors;
    private Color borderColor;
    private final Text text;
    private float zOrder = 0;

    public WallView(
            float width,
            float height,
            List<Color> backgroundColors,
            Color borderColor,
            Color textColor,
            String text,
            float textBorderWidth,
            Color textBorderColor
    ) {
        this.width = width;
        this.height = height;
        this.backgroundColors = backgroundColors;
        this.borderColor = borderColor;
        this.text = new Text(text, textColor, new Font("SansSerif", Font.BOLD, 96), 0, textBorderColor, textBorderWidth); // デフォルトで黒色のテキスト
    }

    @Override
    public void render(Graphics g) {
        // 背景
        g.fillGradientRectVertical(-width / 2, -height / 2, width, height, backgroundColors.get(0), backgroundColors.get(1));

        // 枠
        g.drawRect(-width / 2, -height / 2, width, height, borderColor);

        // テキスト
        this.text.render(g);
    }

    @Override
    public float getZOrder() {
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

    public WallView withText(String text) {
        this.text.setText(text);
        return this;
    }

    public WallView withBackgroundColors(List<Color> colors) {
        this.backgroundColors = colors;
        return this;
    }

    public WallView withBorderColor(Color color) {
        this.borderColor = color;
        return this;
    }

    public WallView withTextColor(Color color) {
        this.text.setColor(color);
        return this;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

}
