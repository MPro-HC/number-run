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

    public float easeOutCubic() {
        float p = progress() - 1;
        return p * p * p + 1;
    }

    public boolean isFinished() {
        return timer.getIsFinished();
    }
}
