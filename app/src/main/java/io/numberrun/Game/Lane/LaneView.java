package io.numberrun.Game.Lane;

import java.awt.Color;
import java.awt.Point;

import io.numberrun.Component.Renderable;
import io.numberrun.UI.Graphics;

public class LaneView implements Renderable {

    private int zOrder = -100;

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    private final Color borderColor = new Color(0x3bb1e4);
    private final Color backgroundColorStart = new Color(0x021550);// new Color(0xE0F7FF);
    private final Color backgroundColorEnd = new Color(0x09335d);// new Color(0xFBFDFF);
    private final float borderMinWidth = 4.0f;
    private final float borderMaxWidth = 20.0f;

    public LaneView(
            int width,
            int height
    ) {
        // top-left: (width * 0.25, 0)
        // top-right: (width * 0.75, 0)
        // bottom-left: (width * 0.0, height)
        // bottom-right: (width * 1.0, height)

        this.topLeft = new Point((int) (-width * 0.05f), (int) (-height / 2f));
        this.topRight = new Point((int) (width * 0.05f), (int) (-height / 2f)); // 上の方はだいぶ細くしていい
        this.bottomLeft = new Point((int) (-width * (0.6f)), (int) (height / 2));
        this.bottomRight = new Point((int) (width * (0.6f)), (int) (height / 2)); // 実際ちょっと画面からはみ出すようにしている
    }

    @Override
    public void render(Graphics g) {
        // 灰色で背景塗りつぶし
        g.fillPolygonGradientVertical(
                new int[]{topLeft.x, topRight.x, bottomRight.x, bottomLeft.x},
                new int[]{topLeft.y, topRight.y, bottomRight.y, bottomLeft.y},
                this.backgroundColorStart,
                this.backgroundColorEnd
        );

        // top-left -> bottom-left にかけての line
        // 手前に行くほど太く、奥に行くほど太くするため多角形で表現する
        float borderWidthTop = borderMinWidth;
        float borderWidthBottom = borderMaxWidth;
        g.fillPolygon(
                new int[]{
                    topLeft.x - (int) (borderWidthTop / 2), topLeft.x + (int) (borderWidthTop / 2),
                    bottomLeft.x + (int) (borderWidthBottom / 2), bottomLeft.x - (int) (borderWidthBottom / 2)
                },
                new int[]{
                    topLeft.y, topLeft.y,
                    bottomLeft.y, bottomLeft.y
                },
                this.borderColor
        );

        // top-right -> bottom-right にかけての line
        // 同様
        g.fillPolygon(
                new int[]{
                    topRight.x - (int) (borderWidthTop / 2), topRight.x + (int) (borderWidthTop / 2),
                    bottomRight.x + (int) (borderWidthBottom / 2), bottomRight.x - (int) (borderWidthBottom / 2)
                },
                new int[]{
                    topRight.y, topRight.y,
                    bottomRight.y, bottomRight.y
                },
                this.borderColor
        );
    }

    @Override
    public int getZOrder() {
        return zOrder; // 背景に近いほど小さい値
    }

    @Override
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public int maxWidth() {
        return bottomRight.x - bottomLeft.x;
    }

    public int maxHeight() {
        return bottomLeft.y - topLeft.y;
    }

    public int minWidth() {
        return topRight.x - topLeft.x;
    }
}
