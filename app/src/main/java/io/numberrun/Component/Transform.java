package io.numberrun.Component;

/**
 * 位置情報を扱うコンポーネント
 */
public class Transform implements Component {
    private float x;
    private float y;
    private float rotation;
    private float scaleX;
    private float scaleY;

    public Transform() {
        this(0, 0);
    }

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }
}
