package io.numberrun.Game;
import io.numberrun.Component.Component;

/**
 * 現在どのレーンにいるかを表すデータ
 */
public class LanePosition implements Component {
    private int laneIndex;  // 0:左, 1:中央, 2:右
    private int maxLanes;   // 最大レーン数

    public LanePosition(int initialLane, int maxLanes) {
        this.laneIndex = initialLane;
        this.maxLanes = maxLanes;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public void setLaneIndex(int laneIndex) {
        // 範囲チェック（0 ～ maxLanes-1 の間だけ移動可能）
        if (laneIndex >= 0 && laneIndex < maxLanes) {
            this.laneIndex = laneIndex;
        }
    }

    public int getMaxLanes() {
        return maxLanes;
    }
}
