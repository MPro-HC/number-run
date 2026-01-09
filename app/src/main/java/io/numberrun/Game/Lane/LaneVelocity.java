package io.numberrun.Game.Lane;

import io.numberrun.Component.Component;

public class LaneVelocity implements Component {

    private float vx;
    private float vy;

    public LaneVelocity() {
        this(0, 0);
    }

    public LaneVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }
}
