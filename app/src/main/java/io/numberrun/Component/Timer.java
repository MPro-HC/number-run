package io.numberrun.Component;

public class Timer implements Component {

    public enum TimerMode {
        Once,
        Loop,
        PingPong,  // 0→1→0→1 と往復するモード
    }

    private final float duration;
    private float timeLeft;
    private final TimerMode mode;
    private boolean isFinished = false;
    private boolean justCompleted = false;  // ループ完了を1フレームだけ検出
    private boolean isReversing = false;  // PingPongモード用: 逆方向に進んでいるかどうか

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

        if (mode == TimerMode.PingPong) {
            // PingPongモード: 方向に応じて加減算
            if (isReversing) {
                this.timeLeft += deltaTime;
                if (this.timeLeft >= duration) {
                    this.justCompleted = true;
                    this.timeLeft = duration - (this.timeLeft - duration);
                    this.isReversing = false;
                }
            } else {
                this.timeLeft -= deltaTime;
                if (this.timeLeft <= 0) {
                    this.justCompleted = true;
                    this.timeLeft = -this.timeLeft;
                    this.isReversing = true;
                }
            }
        } else {
            // Once/Loopモード: 従来の処理
            this.timeLeft -= deltaTime;
            if (this.timeLeft <= 0) {
                this.justCompleted = true;
                switch (mode) {
                    case TimerMode.Loop -> {
                        this.timeLeft += duration;
                    }
                    case TimerMode.Once -> {
                        this.timeLeft = 0;
                        this.isFinished = true;
                    }
                }
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
