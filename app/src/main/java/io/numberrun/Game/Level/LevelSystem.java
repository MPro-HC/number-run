package io.numberrun.Game.Level;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.Game.Scene.Scene;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.Game.Wall.WallType;
import io.numberrun.Game.Wall.WallView;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

/**
 * レーン上に壁などのオブジェクトを生成するシステム 距離ベースで障害物をスポーンし、画面外に出たものを削除する
 */
public class LevelSystem implements GameSystem {

    private static final float SPAWN_INTERVAL_SEC = 1.5f;
    private static final float WALL_SPEED = 0.15f;

    // 生成位置・削除位置（LaneY）
    private static final float SPAWN_Y = -0.5f;   // 奥側ちょい外から出す
    private static final float DESPAWN_Y = 0.80f;  // 手前側に抜けたら消す

    private static final float LEFT_X = -0.25f;
    private static final float RIGHT_X = 0.25f;

    private final Random random = new Random();
    private float spawnTimer = 0f;
    private final int bossInterval = 10; // ボス壁を出す通常壁の間隔

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // シーンがゲームプレイ中でないなら何もしない
        Optional<Entity> sceneEntity = world.query(Scene.class, SceneState.class).stream().findFirst();
        if (sceneEntity.isEmpty()) {
            return;
        }
        if (sceneEntity.get().getComponent(SceneState.class).get().getCurrentScene() != SceneType.GAMEPLAY) {
            return;
        }

        List<Entity> laneEntities = world.query(LaneView.class);
        if (laneEntities.isEmpty()) {
            return;
        }

        LaneView laneView = laneEntities.get(0).getComponent(LaneView.class).get();

        List<Entity> levelEntities = world.query(Level.class);
        if (levelEntities.isEmpty()) {
            return;
        }
        Level level = levelEntities.get(0).getComponent(Level.class).get();

        // 画面外に抜けた壁を削除
        cleanupWalls(world);

        // 壁が多すぎるときは生成しない（保険）
        int wallCount = world.query(Wall.class).size();
        if (wallCount > 30) {
            return;
        }

        // 生成タイマー更新
        spawnTimer += deltaTime;

        // 生成間隔を超えている分だけループして生成
        while (spawnTimer >= SPAWN_INTERVAL_SEC) {
            spawnTimer -= SPAWN_INTERVAL_SEC;

            //　処理落ち等で複数生成される場合、同じ位置に重ならないように
            // 「本来生成されるべきだった時間」からの経過分だけ、手前にずらして配置する
            float timeOffset = spawnTimer;
            float yOffset = timeOffset * WALL_SPEED;

            level.incrementSpawnCount();
            int spawnCount = level.getSpawnCount();
            System.out.println("Spawning walls, count=" + spawnCount);

            // 一定回数ごとにボスを出す
            if (spawnCount % bossInterval == 0) {
                // プレイヤーの現在の値を取得して、それが1に近くなるようにランダムに補正する
                int playerNumber = currentPlayerNumber(world);
                if (playerNumber == 0) {
                    // 既に死んでる
                    continue;
                }
                spawnWallBoss(world, laneView, yOffset, playerNumber);
            } else {
                spawnWallPair(world, laneView, yOffset, spawnCount);
            }

        }
    }

    private int currentPlayerNumber(World world) {
        List<Entity> players = world.query(
                PlayerState.class
        );
        if (players.isEmpty()) {
            return 0;
        }

        for (Entity e : players) {
            if (e.getComponent(PlayerState.class).isPresent()) {
                return e.getComponent(PlayerState.class).get().getNumber();
            }
        }
        return 0;
    }

    private void cleanupWalls(World world) {
        for (Entity e : world.query(Wall.class, LaneTransform.class)) {
            LaneTransform lt = e.getComponent(LaneTransform.class).get();
            if (lt.getLaneY() > DESPAWN_Y) {
                e.destroy();
            }
        }
    }

    // 引き算以外の壁タイプをランダムに取得 (足し算、掛け算)
    private WallType randomNonSubtractWallType() {
        WallType[] candidates = {WallType.Add, WallType.Multiply};
        return candidates[random.nextInt(candidates.length)];
    }

    private void spawnWallPair(World world, LaneView laneView, float yOffset, int spawnCount) {
        WallType leftType = randomWallType();
        WallType rightType = randomWallType();

        // 最初の方のみ、引き算同士の組み合わせの時だけ、右側を「引き算以外」に変更する
        if (spawnCount < 5 && leftType == WallType.Subtract && rightType == WallType.Subtract) {
            rightType = randomNonSubtractWallType();
        }

        int leftValue = randomWallValue(leftType, spawnCount);
        int rightValue = randomWallValue(rightType, spawnCount);

        while (leftValue == rightValue && leftType == rightType) {
            // 全く同じはつまらないので右を変える
            rightType = randomWallType();
            rightValue = randomWallValue(rightType, spawnCount);
        }

        spawnWall(
                world,
                laneView,
                LEFT_X,
                leftType,
                leftValue,
                yOffset,
                laneView.maxWidth() / 2
        );
        spawnWall(
                world,
                laneView,
                RIGHT_X,
                rightType,
                rightValue,
                yOffset,
                laneView.maxWidth() / 2
        );
    }

    private void spawnWallBoss(World world, LaneView laneView, float yOffset, int playerNumber) {
        // デカめの強制マイナス壁を生成する

        // 1~5くらい残るように調整
        int wallValue = Math.max(1, playerNumber - 5) + random.nextInt(5); // 5 引いて 0~4 を足す

        // 中心なので x は 0
        spawnWall(
                world,
                laneView,
                0f,
                WallType.Subtract,
                wallValue,
                yOffset,
                laneView.maxWidth()
        );
    }

    private void spawnWall(
            World world,
            LaneView laneView,
            float laneX,
            WallType type,
            int wallValue,
            float yOffset,
            int wallWidth
    ) {

        // 表示テキスト
        String label = type.label() + wallValue;

        // 壁サイズ：レーン幅の半分
        int wallHeight = laneView.maxHeight() * 400 / 960;

        // 壁本体（親Entity）
        world.spawn(
                new Transform(),
                new WallView(
                        wallWidth * 0.95f, // ちょっと小さめに
                        wallHeight,
                        List.of(type.backgroundColorStart(), type.backgroundColorEnd()), // background colors
                        type.borderColor(), // border color
                        type.textColor(), // text color
                        label,
                        type.textBorderWidth(), // text border width
                        type.textBorderColor() // text border color
                ),
                new LaneSize(0.5f, 0.1f), // レーン幅の半分よりちょっと小さくしてみた、高さは適当
                // Y座標にオフセットを加算して生成
                new LaneTransform(laneX, SPAWN_Y + yOffset),
                new LaneVelocity(0f, WALL_SPEED), // 手前へ流す（LaneMovementSystemが反映）
                new Wall(type, wallValue) // 通過判定用
        );
    }

    private WallType randomWallType() {
        // 48% Add, 48% Subtract, 4% Multiply
        float roll = random.nextFloat();
        if (roll < 0.48f) {
            return WallType.Add;
        } else if (roll < 0.96f) {
            return WallType.Subtract;
        }
        return WallType.Multiply;
    }

    private int randomWallValue(WallType type, int spawnCount) {
        // 壁を通過するごとにだんだんベースの数値が大きくなるように
        return switch (type) {
            case Add ->
                // 増やすのは難しく
                1 + random.nextInt(9 + spawnCount / 3);
            case Subtract ->
                1 + random.nextInt(9 + spawnCount / 2);
            case Multiply ->
                2;
            case Divide ->
                2 + random.nextInt(2);
        };
    }
}
