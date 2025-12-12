package io.numberrun.Game.GlobalCursor;

import java.awt.Point;

import io.numberrun.Component.Component;

/**
 * グローバルなカーソル位置を保持するモデルコンポーネント
 */
public class GlobalCursorModel implements Component {

    private Point position;

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
