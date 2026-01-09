package io.numberrun.Game.Wall;

import io.numberrun.Component.Component;

// 実際にレーン上に置かれる壁のコンポーネント
public class Wall implements Component {

    private final WallType wallType;
    private final int value;

    public Wall(WallType wallType, int value) {
        this.wallType = wallType;
        this.value = value;
    }

    public WallType getWallType() {
        return wallType;
    }

    public int getValue() {
        return value;
    }
}
