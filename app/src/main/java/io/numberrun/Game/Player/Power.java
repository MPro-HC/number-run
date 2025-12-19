package io.numberrun.Game.Player;

import io.numberrun.Component.Component;

/**
 * キャラクターの「人数」や「強さ」を表すデータ
 */
public class Power implements Component {
    private int value;

    public Power(int initialValue) {
        this.value = initialValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (this.value < 0) this.value = 0; // 負の値にならないようにする
    }

    // 加算用メソッド
    public void add(int amount) {
        this.value += amount;
        if (this.value < 0) this.value = 0;
    }

    // 乗算用メソッド
    public void multiply(int factor) {
        this.value *= factor;
    }
}