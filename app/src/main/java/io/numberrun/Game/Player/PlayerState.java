package io.numberrun.Game.Player;

import io.numberrun.Component.Component;

// プレイヤーの情報を持つ (IPlayerState を実装する)
public class PlayerState implements Component {

    // 現在の「数値」の値を持っておく
    private int currentNumber = 1;

    public int getNumber() {
        return currentNumber;
    }

    public int setNumber(int number) {
        this.currentNumber = number;
        return currentNumber;
    }
}
