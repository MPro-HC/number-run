package io.numberrun.Game.Level;

import java.util.List;
import java.util.Random;

import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
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

    // レーンに壁などのオブジェクトを生成する処理と World に spawn する処理
    // 生成間隔（秒）
    private static final float SPAWN_INTERVAL_SEC = 1.5f;

    // 壁が奥→手前に流れてくる速度（Lane座標 / 秒）
    private static final float WALL_SPEED = 0.15f;

    // 生成位置・削除位置（LaneY）
    private static final float SPAWN_Y = -0.65f;   // 奥側ちょい外から出す
    private static final float DESPAWN_Y = 0.80f;  // 手前側に抜けたら消す

    private static final float LEFT_X = -0.25f;
    private static final float RIGHT_X = 0.25f;

    private final Random random = new Random();
    private float spawnTimer = 0f;

    @Override
    public int getPriority() {
        // 生成は別に早くても遅くても良いが、DEFAULTにしておく
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // LaneView が無いと壁の幅を決められないので何もしない
        List<Entity> laneEntities = world.query(LaneView.class);
        if (laneEntities.isEmpty()) {
            return;
        }

        LaneView laneView = laneEntities.get(0).getComponent(LaneView.class).get();

        // 画面外に抜けた壁を削除
        cleanupWalls(world);

        // 生成タイマー更新
        spawnTimer += deltaTime;

        // 壁が多すぎるときは生成しない（保険）
        int wallCount = world.query(Wall.class).size();
        if (wallCount > 30) {
            return;
        }

        // 一定間隔で「左右に1枚ずつ」生成
        while (spawnTimer >= SPAWN_INTERVAL_SEC) {
            spawnTimer -= SPAWN_INTERVAL_SEC;

            spawnWall(world, laneView, LEFT_X);
            spawnWall(world, laneView, RIGHT_X);
        }
    }

    private void cleanupWalls(World world) {
        for (Entity e : world.query(Wall.class, LaneTransform.class)) {
            LaneTransform lt = e.getComponent(LaneTransform.class).get();
            if (lt.getLaneY() > DESPAWN_Y) {
                e.destroy();
            }
        }
    }

    private void spawnWall(World world, LaneView laneView, float laneX) {
        // 種類と値を決める
        WallType type = randomWallType();
        int value = randomWallValue(type);

        // 表示テキスト
        String label = type.label() + value;

        // 壁サイズ：レーン幅の半分
        int wallWidth = laneView.maxWidth() / 2;
        int wallHeight = 400;

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
                new LaneSize(0.5f, 0.1f), // レーン幅の半分、高さは適当
                new LaneTransform(laneX, SPAWN_Y), // 奥から出す
                new LaneVelocity(0f, WALL_SPEED), // 手前へ流す（LaneMovementSystemが反映）
                new Wall(type, value) // 通過判定用
        );
    }

    private WallType randomWallType() {
        WallType[] types = WallType.values();
        return types[random.nextInt(types.length)];
    }

    private int randomWallValue(WallType type) {
        // ここはゲームバランスなので適当に調整してOK
        return switch (type) {
            case Add ->
                1 + random.nextInt(9);       // +1..+9
            case Subtract ->
                1 + random.nextInt(9);  // -1..-9
            case Multiply ->
                2 + random.nextInt(4);  // x2..x5
            case Divide ->
                2 + random.nextInt(3);    // /2../4
        };
    }

}
