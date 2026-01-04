package io.numberrun.Game.Lane;

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
        // TODO: LaneVelocity に基づいてプレイヤーの位置を更新する (MovementSystem.java 参照)
        // 1. for 分で world から LaneTransform と LaneVelocity を持つエンティティを query で取得する
        {
            // 2. それぞれの entity に対して、LaneTransform と LaneVelocity を取得する

            // 3. LaneTransform と LaneVelocity の両方が存在するなら
            {

                // 4. LaneVelocity の速度に deltaTime をかけたものを LaneTransform 座標に足して計算
                // 5. 計算した新しい座標をセットする
            }
        }
    }
}
