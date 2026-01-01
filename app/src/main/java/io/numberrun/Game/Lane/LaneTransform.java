package io.numberrun.Game.Lane;

// レーン上の座標を表すコンポーネント
import io.numberrun.Component.Component;

public class LaneTransform implements Component {

    private float laneX; // レーン上のX座標
    private float laneY; // レーン上のY座標
    private boolean scaleByZ = true; // Zスケールを有効にするかどうか デフォルトで有効

    private final float minX = -0.5f; // 左
    private final float maxX = 0.5f; // 右
    private final float minY = -0.5f; // 上
    private final float maxY = 0.5f; // 下

    public LaneTransform() {
        this(0, 0);
    }

    public LaneTransform(float laneX, float laneY) {
        this.laneX = laneX;
        this.laneY = laneY;
    }

    public LaneTransform(float laneX, float laneY, boolean scaleByZ) {
        this.laneX = laneX;
        this.laneY = laneY;
        this.scaleByZ = scaleByZ; // プレイヤーなどはスケールを無効にするかも
    }

    public float getLaneX() {
        return laneX;
    }

    public void setLaneX(float laneX) {
        this.laneX = laneX;
    }

    public float getLaneY() {
        return laneY;
    }

    public void setLaneY(float laneY) {
        this.laneY = laneY;
    }

    public boolean shouldScaleByZ() {
        return scaleByZ;
    }

    // 固定値
    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
