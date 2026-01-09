package io.numberrun.System;

/**
 * 処理順の優先度を定義する列挙型
 */
public enum SystemPriority {
    HEIGHEST(0),
    HIGH(1),
    DEFAULT(50),
    LOW(100),
    VERY_LOW(1000);

    private final int priority;

    SystemPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
