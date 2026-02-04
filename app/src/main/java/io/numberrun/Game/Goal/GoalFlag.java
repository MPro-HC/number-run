package io.numberrun.Game.Goal;

import io.numberrun.Component.Component;

public class GoalFlag implements Component {
    private int counter;
    private boolean spawned = false;

    private boolean stopSpawns;

    public GoalFlag(int initial) {
        this.counter = initial;
        this.stopSpawns = false;
    }

    public int getCounter() { return counter; }
    public boolean isSpawned() { return spawned; }

    public boolean isStopSpawns() { return stopSpawns; }
    public void setStopSpawns(boolean stopSpawns) { this.stopSpawns = stopSpawns; }

    public void tryDecrement50() {
        if (spawned || counter <= 0) return;
        if (Math.random() < 0.5) counter--;
        if (counter <= 0) spawned = true;
    }
}