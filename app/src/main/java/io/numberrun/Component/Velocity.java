package io.numberrun.Component;

/**
 * 速度を扱うコンポーネント 移動するエンティティに使用
 */
public class Velocity implements Component {

    private float vx;
    private float vy;

    public Velocity() {
        this(0, 0);
    }

    public Velocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }
}
