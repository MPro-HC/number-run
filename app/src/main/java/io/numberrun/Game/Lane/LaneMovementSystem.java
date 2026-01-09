package io.numberrun.Game.Lane;

import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

/**
 * LaneVelocity に基づいて LaneTransform の座標を更新するシステム 障害物を手前に移動させる処理を担当
 */
public class LaneMovementSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // LaneTransform と LaneVelocity を持つエンティティを取得
        for (Entity entity : world.query(LaneTransform.class, LaneVelocity.class)) {
            // コンポーネントを取得
            LaneTransform transform = entity.getComponent(LaneTransform.class).orElse(null);
            LaneVelocity velocity = entity.getComponent(LaneVelocity.class).orElse(null);

            // 両方存在する場合のみ処理
            if (transform != null && velocity != null) {
                // 速度 × deltaTime を現在座標に加算
                float newX = transform.getLaneX() + velocity.getVx() * deltaTime;
                float newY = transform.getLaneY() + velocity.getVy() * deltaTime;

                // 新しい座標をセット
                transform.setLaneX(newX);
                transform.setLaneY(newY);
            }
        }
    }
}
