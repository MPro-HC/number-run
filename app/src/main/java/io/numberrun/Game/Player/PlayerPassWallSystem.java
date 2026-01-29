package io.numberrun.Game.Player;

// プレイヤーが壁を通過したときの処理を管理するシステム
import java.util.List;

import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.Game.Wall.WallType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerPassWallSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {

        // 壁を通過したかの判定 + 通過した際の効果を適用する
        // 1. world から PlayerState, LaneTransform を持つエンティティを取得する
        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) {
            return;
        }
        Entity player = players.get(0);
        PlayerState playerState = player.getComponent(PlayerState.class).get();
        LaneTransform playerLT = player.getComponent(LaneTransform.class).get();

        // 2. PlayerView の位置情報を取得する
        float playerX = playerLT.getLaneX();
        float playerY = playerLT.getLaneY();

        // 3. world から Wall, LaneTransform を持つエンティティを取得する
        List<Entity> walls = world.query(Wall.class, LaneTransform.class);

        for (Entity wallEntity : walls) {
            Wall wall = wallEntity.getComponent(Wall.class).get();
            LaneTransform wallLT = wallEntity.getComponent(LaneTransform.class).get();
            LaneSize wallSize = wallEntity.getComponent(LaneSize.class).get();

            float wallX = wallLT.getLaneX();
            float wallY = wallLT.getLaneY();
            float wallWidth = wallSize.getWidth();
            float wallHeight = wallSize.getHeight();

            // X 座標が wall を通過する位置にいるかどうか
            boolean passX = (playerX + 0.01f >= wallX - wallWidth / 2) && (playerX - 0.01f <= wallX + wallWidth / 2);
            if (!passX) {
                continue;
            }

            // 4. 各壁エンティティについて、PlayerView の位置と比較して通過したか判定する
            float vy = wallEntity.getComponent(LaneVelocity.class)
                    .map(LaneVelocity::getVy)
                    .orElse(0f);

            float prevWallY = wallY - vy * deltaTime;

            boolean crossed;
            if (vy != 0f) {
                // 速度情報がある場合は、前フレーム位置と比較して通過を判定する
                crossed = (prevWallY < playerY) && (wallY >= playerY);
            } else {
                // 速度情報がない場合は雑に「プレイヤーYを超えたら通過」とする
                crossed = (wallY >= playerY);
            }

            if (!crossed) {
                continue;
            }

            // 4.1 通過していたら、Wall の効果を PlayerState に適用する
            applyWallEffect(playerState, wall);
            // 4.2 壁エンティティを world から削除する (無効化する)
            wallEntity.destroy();
        }
    }

    private void applyWallEffect(PlayerState playerState, Wall wall) {
        WallType type = wall.getWallType();
        int value = wall.getValue();
        playerState.setNumber(
                type.getAppliedNumber(playerState.getNumber(), value)
        );
    }
}
