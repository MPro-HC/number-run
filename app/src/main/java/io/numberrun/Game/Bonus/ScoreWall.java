package io.numberrun.Game.Bonus;

import io.numberrun.Component.Component;

public class ScoreWall implements Component {
    private final int value;

    public ScoreWall(int value) { this.value = value; }
    public int getValue() { return value; }
}
