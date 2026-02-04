package io.numberrun.Game.Bonus;

import io.numberrun.Component.Component;

public class BonusState implements Component {
    public int currentNumber;
    public int totalScore;

    public int spawnIndex;     // 次に生成する壁（10,20,...）の位置
    public int clearedCount;   // 通過して処理した壁の枚数

    public boolean initialized;

    public BonusState(int currentNumber) {
        this.currentNumber = currentNumber;
        this.totalScore = 0;
        this.spawnIndex = 0;
        this.clearedCount = 0;
        this.initialized = false;
    }
}
