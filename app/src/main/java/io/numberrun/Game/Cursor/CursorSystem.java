package io.numberrun.Game.Cursor;

import java.awt.Point;
import java.util.List;

import io.numberrun.Component.Transform;
import io.numberrun.Game.GlobalCursor.GlobalCursorModel;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

// 毎フレームカーソルに位置に移動させるシステム
// MVC でいう Controller に相当する
public class CursorSystem implements GameSystem {

    @Override
    public int getPriority() {
        // 他の処理が行われてから実行したい
        return SystemPriority.LOW.getPriority();
    }

    // 毎フレーム呼ばれる
    @Override
    public void update(World world, float deltaTime) {

        List<Entity> entities = world.query(
                // この３つを持つエンティティを探す
                Transform.class,
                CursorView.class,
                GlobalCursorModel.class
        );

        // 必ず１つあるはず
        if (entities.size() != 1) {
            return;
        }

        // これからカーソルの位置に transform を移動させる
        // カーソルトラッカーのエンティティを取得
        Entity cursor = entities.get(0);

        // 現在の位置情報
        Transform transform = cursor.getComponent(Transform.class).get();

        // グローバルなカーソル座標が入ったモデル
        GlobalCursorModel model = cursor.getComponent(GlobalCursorModel.class).get();

        Point position = model.getPosition();

        // マウスカーソル位置をデバッグ表示できる
        // System.out.println("Updating cursor position to: (" + position.x + ", " + position.y + ")");
        transform.setPosition(position.x, position.y);

    }

}
