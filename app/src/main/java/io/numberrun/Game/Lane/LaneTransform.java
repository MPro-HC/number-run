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

    private float movementMinX = -0.5f; // 移動可能な最小X座標
    private float movementMaxX = 0.5f; // 移動可能な最大X座標
    private float movementMinY = -1.0f; // 移動可能な最小Y座標
    private float movementMaxY = 1.0f; // 移動可能な最大Y座標

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
        // 移動可能な範囲内に制限
        if (laneX < 0) {
            laneX = Math.max(laneX, movementMinX);
        } else {
            laneX = Math.min(laneX, movementMaxX);
        }
        this.laneX = laneX;
    }

    public float getLaneY() {
        // 移動可能な範囲内に制限
        if (laneY < 0) {
            laneY = Math.max(laneY, movementMinY);
        } else {
            laneY = Math.min(laneY, movementMaxY);
        }
        return laneY;
    }

    public void setLaneY(float laneY) {
        this.laneY = laneY;
    }

    public boolean shouldScaleByZ() {
        return scaleByZ;
    }

    public LaneTransform setMovementLimit(float minX, float maxX, float minY, float maxY) {
        this.movementMinX = minX;
        this.movementMaxX = maxX;
        this.movementMinY = minY;
        this.movementMaxY = maxY;

        return this;
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
