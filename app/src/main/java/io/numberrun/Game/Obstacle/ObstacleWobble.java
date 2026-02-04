package io.numberrun.Game.Obstacle;

import io.numberrun.Component.Component;

/**
 * レーン上で左右に往復するための情報 laneX = baseX + amplitude * sin(omega * t + phase)
 */
public class ObstacleWobble implements Component {

    private final float baseX;
    private final float amplitude;
    private final float omega;   // rad/s
    private final float phase;   // rad
    private float t = 0f;

    public ObstacleWobble(float baseX, float amplitude, float omega, float phase) {
        this.baseX = baseX;
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    public float getBaseX() {
        return baseX;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public float getOmega() {
        return omega;
    }

    public float getPhase() {
        return phase;
    }

    public float getT() {
        return t;
    }

    public void addTime(float dt) {
        this.t += dt;
    }
}
