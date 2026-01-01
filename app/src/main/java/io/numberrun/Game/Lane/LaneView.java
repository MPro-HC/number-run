package io.numberrun.Game.Lane;

import java.awt.Color;
import java.awt.Point;

import io.numberrun.Component.Renderable;
import io.numberrun.UI.Graphics;

public class LaneView implements Renderable {

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    private final Color borderColor = new Color(0x0090FF);
    private final Color backgroundColor = new Color(0xFBFDFF);

    public LaneView(
            int width,
            int height
    ) {
        // top-left: (width * 0.25, -height)
        // top-right: (width * 0.75, -height)
        // bottom-left: (width * 0.1, height)
        // bottom-right: (width * 0.9, height)

        this.topLeft = new Point((int) (-width * 0.25f), (int) (-height / 2f));
        this.topRight = new Point((int) (width * 0.25f), (int) (-height / 2f));
        this.bottomLeft = new Point((int) (-width * 0.45f), (int) (height / 2));
        this.bottomRight = new Point((int) (width * 0.45f), (int) (height / 2));
    }

    @Override
    public void render(Graphics g) {
        // 灰色で背景塗りつぶし
        g.fillPolygon(
                new int[]{topLeft.x, topRight.x, bottomRight.x, bottomLeft.x},
                new int[]{topLeft.y, topRight.y, bottomRight.y, bottomLeft.y},
                this.backgroundColor
        );

        // top-left -> bottom-left にかけての line
        g.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, this.borderColor);
        // top-right -> bottom-right にかけての line
        g.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y, this.borderColor);

    }

    @Override
    public int getZOrder() {
        return -100; // 背景に近いほど小さい値
    }
}
