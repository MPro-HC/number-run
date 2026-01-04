package io.numberrun.Component;

public class Timer implements Component {

    public enum TimerMode {
        Once,
        Loop
    }

    private final float duration;
    private float timeLeft;
    private final TimerMode mode;
    private boolean isFinished = false;
    private boolean justCompleted = false;  // ループ完了を1フレームだけ検出

    public Timer(
            float duration, // milliseconds
            TimerMode mode
    ) {
        this.duration = duration;
        this.timeLeft = duration;
        this.mode = mode;
    }

    public void tick(float deltaTime) {
        this.justCompleted = false;  // 毎フレームリセット
        this.timeLeft -= deltaTime;
        if (this.timeLeft <= 0) {
            this.justCompleted = true;  // ループ完了またはタイマー終了
            if (mode == TimerMode.Loop) {
                this.timeLeft += duration;
            } else {
                this.timeLeft = 0;
                this.isFinished = true;
            }
        }
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    /**
     * ループが一回完了した、または Onceモードで終了した場合に true を返す tick() を呼んだフレームでのみ true になる
     */
    public boolean justCompleted() {
        return justCompleted;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void restart() {
        this.timeLeft = duration;
        this.isFinished = false;
        this.justCompleted = false;
    }

    public float getProgress() {
        return 1.0f - (timeLeft / duration);
    }

    public float getTimeLeft() {
        return timeLeft;
    }
}
