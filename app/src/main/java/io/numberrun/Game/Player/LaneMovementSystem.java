package io.numberrun.Game.Player;

import io.numberrun.Component.Transform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

public class LaneMovementSystem implements GameSystem {

    // 「どこからどこまで移動できるか」はゲームのルール（Model）なのでここで定義
    private static final float MIN_X = 200.0f; // 左レーンの中心X座標
    private static final float MAX_X = 600.0f; // 右レーンの中心X座標
    private static final float TOTAL_WIDTH = MAX_X - MIN_X;

    @Override
    public void update(World world, float deltaTime) {
        for (Entity entity : world.query(LanePosition.class, Transform.class)) {
            LanePosition lane = entity.getComponent(LanePosition.class).get();
            Transform transform = entity.getComponent(Transform.class).get();

            // データの状態を、物理的な位置(Transform)に反映させる
            // 線形補間: 左端 + (全体幅 * 割合)
            float targetX = MIN_X + (TOTAL_WIDTH * lane.getNormalizedPosition());

            transform.setX(targetX);
        }
    }
}