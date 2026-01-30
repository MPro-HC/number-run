package io.numberrun.Game.Easing;

import io.numberrun.Component.Component;
import io.numberrun.Component.Timer;

public class Easing implements Component {

    private final Timer timer;

    public Easing(
            Timer timer
    ) {
        this.timer = timer;
    }

    public void tick(float deltaTime) {
        this.timer.tick(deltaTime);
    }

    private float progress() {
        return timer.getProgress();
    }

    public float easeInOut() {
        float progress = progress();
        return progress < 0.5
                ? 2 * progress * progress
                : -1 + (4 - 2 * progress) * progress;
    }

    public float easeIn() {
        float p = progress();
        return p * p * p;
    }

    public float easeOut() {
        float p = progress();
        float f = (p - 1);
        return f * f * f + 1;
    }

    public float easeOutSine() {
        float p = progress();
        return (float) Math.sin(p * (Math.PI / 2));
    }

    public float easeOutCubic() {
        float p = progress() - 1;
        return p * p * p + 1;
    }

    public boolean isFinished() {
        return timer.getIsFinished();
    }

    public void restart() {
        timer.restart();
    }
}
