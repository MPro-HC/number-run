package io.numberrun.Game.Player;

// プレイヤーが壁を通過したときの処理を管理するシステム
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.System.Entity;

import java.util.List;

import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.Game.Wall.WallType;

public class PlayerPassWallSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {

        // TODO: プレイヤーが壁を通過したときの処理を実装する

        // プレイ中以外は何もしない
        List<Entity> scenes = world.query(SceneState.class);
        if (!scenes.isEmpty()) {
            SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
            if (sceneState.getCurrentScene() != SceneType.GAMEPLAY) return;
        }

        // 壁を通過したかの判定 + 通過した際の効果を適用する
        // 1. world から PlayerState, LaneTransform を持つエンティティを取得する
        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) return;
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

            float wallX = wallLT.getLaneX();
            float wallY = wallLT.getLaneY();

            // 同じレーン側か判定（左/右）
            // playerX<0 なら左、playerX>=0 なら右 とみなす
            boolean sameLane =
                    (playerX < 0 && wallX < 0) ||
                    (playerX >= 0 && wallX >= 0);

            if (!sameLane) continue;

            // 4. 各壁エンティティについて、PlayerView の位置と比較して通過したか判定する(ここが面倒かもしれない)
            float vy = wallEntity.getComponent(LaneVelocity.class)
                    .map(LaneVelocity::getVy)
                    .orElse(0f);

            float prevWallY = wallY - vy * deltaTime;

            boolean crossed;
            if (vy != 0f) {
                // 前はプレイヤーより上（小さい）で、今はプレイヤーより下（大きい）になった
                crossed = (prevWallY < playerY) && (wallY >= playerY);
            } else {
                // 速度情報がない場合は雑に「プレイヤーYを超えたら通過」とする
                crossed = (wallY >= playerY);
            }

            if (!crossed) continue;

            // 4.1 通過していたら、Wall の効果を PlayerState に適用する
            applyWallEffect(playerState, wall);
            // 4.2 壁エンティティを world から削除する (無効化する)
            wallEntity.destroy();
        }
    }
    private void applyWallEffect(PlayerState playerState, Wall wall) {
        WallType type = wall.getWallType();
        int value = wall.getValue();

        switch (type) {
            case Add -> playerState.addOwnNumber(value);
            case Subtract -> playerState.addOwnNumber(-value);
            case Multiply -> playerState.multiplyOwnNumber(value);
            case Divide -> playerState.divideOwnNumber(value);
        }
    }
}
