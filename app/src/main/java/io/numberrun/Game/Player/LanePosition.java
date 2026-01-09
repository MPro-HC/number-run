package io.numberrun.Game.Player;

import io.numberrun.Component.Component;

public class LanePosition implements Component {
    // 0.0(左) ～ 1.0(中央) ～ 2.0(右) のような小数で管理
    private float currentPosition;
    private float minPosition;
    private float maxPosition;

    public LanePosition(float initialPos, float maxLanes) {
        this.currentPosition = initialPos;
        this.minPosition = 0.0f;
        this.maxPosition = maxLanes - 1.0f; // 3レーンなら最大値は 2.0
    }

    public float getPosition() {
        return currentPosition;
    }

    public void move(float amount) {
        this.currentPosition += amount;
        
        // 範囲制限（ルール）の適用
        if (this.currentPosition < minPosition) {
            this.currentPosition = minPosition;
        }
        if (this.currentPosition > maxPosition) {
            this.currentPosition = maxPosition;
        }
    }
    
    // 座標計算用：全体の中での進行度（0.0 ～ 1.0）を返す便利メソッド
    public float getNormalizedPosition() {
        if (maxPosition == 0) return 0;
        return currentPosition / maxPosition;
    }
}