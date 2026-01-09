package io.numberrun.Game.Gate;

import io.numberrun.Component.Component;

/**
 * ゲート（数値ブロック）のデータ 通過したときにどう計算するか（種類）と、その値を持つ
 */
public class Gate implements Component {

    // 計算の種類
    public enum OperationType {
        ADD, // 足し算 (+)
        MULTIPLY    // 掛け算 (x)
    }

    private OperationType operationType;
    private int value;

    public Gate(OperationType operationType, int value) {
        this.operationType = operationType;
        this.value = value;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getValue() {
        return value;
    }
}
