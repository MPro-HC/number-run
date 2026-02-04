package io.numberrun.Game.Goal;

import io.numberrun.Component.Component;

public class FinalScore implements Component {
    private final int score;
    private final int numberAtGoal;

    public FinalScore(int score, int numberAtGoal) {
        this.score = score;
        this.numberAtGoal = numberAtGoal;
    }

    public int getScore() { return score; }
    public int getNumberAtGoal() { return numberAtGoal; }
}