package io.numberrun.Game.Lane;

import io.numberrun.Component.Component;

// 単に幅と高さを記録するだけ
public class LaneSize implements Component {

    private final float width; // レーンの幅
    private final float height; // レーンの高さ

    public LaneSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
