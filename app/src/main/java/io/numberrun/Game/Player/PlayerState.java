package io.numberrun.Game.Player;

import io.numberrun.Component.Component;

// プレイヤーの情報を持つ (IPlayerState を実装する)
public class PlayerState implements Component {

    // 現在の「数値」の値を持っておく
    private int currentNumber = 1;

    public int getNumber() {
        return currentNumber;
    }

    public int addNumber(int value) {
        // 現在の数値に value を加算して更新し、更新後の数値を返す
        this.currentNumber += value;
        return currentNumber;
    }

    public int subtractNumber(int value) {
        // 現在の数値から value を減算して更新し、更新後の数値を返す
        this.currentNumber -= value;
        return currentNumber;
    }

    public int multiplyNumber(int value) {
        // 現在の数値に value を乗算して更新し、更新後の数値を返す
        this.currentNumber *= value;
        return currentNumber;
    }

    public int divideNumber(int value) {
        // 現在の数値を value で割算して更新し、更新後の数値を返す
        // もし小数になったら切り上げ処理を行う(ユーザーに有利にするため)
        if (value != 0) {
            this.currentNumber = (int) Math.ceil((double) currentNumber / value);
        }
        return currentNumber;
    }

}
