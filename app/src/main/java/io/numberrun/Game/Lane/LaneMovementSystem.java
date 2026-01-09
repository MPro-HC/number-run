package io.numberrun.Game.Lane;

import java.util.List;

import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class LaneMovementSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // LaneVelocity に基づいてプレイヤーの位置を更新する (MovementSystem.java 参照)
        List<Entity> entities = world.query(LaneTransform.class, LaneVelocity.class);
        // 1. for 分で world から LaneTransform と LaneVelocity を持つエンティティを query で取得する
        for (Entity entity : entities) {
            // 2. それぞれの entity に対して、LaneTransform と LaneVelocity を取得する
            LaneTransform laneTransform = entity.getComponent(LaneTransform.class).get();
            LaneVelocity laneVelocity = entity.getComponent(LaneVelocity.class).get();

            // 3. LaneTransform と LaneVelocity の両方が存在するなら
            // 4. LaneVelocity の速度に deltaTime をかけたものを LaneTransform 座標に足して計算
            // 5. 計算した新しい座標をセットする
            laneTransform.setLaneX(laneTransform.getLaneX() + laneVelocity.getVx() * deltaTime);
            laneTransform.setLaneY(laneTransform.getLaneY() + laneVelocity.getVy() * deltaTime);
        }
    }
}
