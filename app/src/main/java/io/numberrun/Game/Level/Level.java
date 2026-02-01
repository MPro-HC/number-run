package io.numberrun.Game.Level;

import io.numberrun.Component.Component;

/**
 * レベル進行状態を管理するコンポーネント 障害物の生成タイミングや難易度を制御する
 */
public class Level implements Component {

    private int spawnCount = 0;

    public int getSpawnCount() {
        return spawnCount;
    }

    public void incrementSpawnCount() {
        this.spawnCount += 1;
    }

    public void reset() {
        this.spawnCount = 0;
    }

}
