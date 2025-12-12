package io.numberrun.Game.GlobalCursor;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;

import javax.swing.JFrame;

import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

/**
 * 毎フレームグローバルなマウス位置を更新するシステム
 */
public class GlobalCursorSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> entities = world.query(
                GlobalCursorModel.class
        );
        if (entities.size() != 1) {
            return;
        }

        GlobalCursorModel cursorModel = entities.get(0).getComponent(GlobalCursorModel.class).get();

        // jframe
        JFrame frame = world.getGlobalFrame();

        // マウスのスクリーン座標を取得
        Point position = MouseInfo.getPointerInfo().getLocation();

        // JFrameの位置を取得
        Point frameLocation = frame.getLocationOnScreen();

        // JFrame内の座標に変換
        int x = position.x - frameLocation.x;
        int y = position.y - frameLocation.y;

        cursorModel.setPosition(
                new Point(x, y)
        );

    }

}
