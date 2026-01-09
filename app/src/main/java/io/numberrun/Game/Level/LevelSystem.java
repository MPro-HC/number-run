package io.numberrun.Game.Level;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
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

    private static final float SPAWN_Y = -0.5f;      // 障害物が出現するY座標（奥）
    private static final float DESPAWN_Y = 0.6f;     // 障害物が消えるY座標（手前を超えた位置）
    private static final float WALL_WIDTH = 360f;    // 壁の幅
    private static final float WALL_HEIGHT = 160f;   // 壁の高さ

    private final Random random = new Random();

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void onStart(World world) {
        // Level エンティティが存在しない場合は生成する
        List<Entity> levels = world.query(Level.class);
        if (levels.isEmpty()) {
            world.spawn(new Level());
        }
    }

    @Override
    public void update(World world, float deltaTime) {
        // Level コンポーネントを取得
        List<Entity> levelEntities = world.query(Level.class);
        if (levelEntities.isEmpty()) {
            return;
        }

        Level level = levelEntities.get(0).getComponent(Level.class).get();

        if (!level.isActive()) {
            return;
        }

        // 1. 進行距離を更新
        level.addDistance(level.getScrollSpeed() * deltaTime);

        // 2. 生成判定：距離が次のスポーン距離に達したらスポーン
        while (level.getCurrentDistance() >= level.getSpawnDistance()) {
            spawnWallPair(world, level);
            scheduleNextSpawn(level);
        }

        // 3. 画面外に出た障害物を削除
        cleanupOffscreenEntities(world);
    }

    /**
     * 壁のペアをスポーンする（左右に2つ）
     */
    private void spawnWallPair(World world, Level level) {
        // 左側の壁
        WallType leftType = randomWallType();
        int leftValue = randomValue(leftType);
        spawnWall(world, level, -0.25f, leftType, leftValue);

        // 右側の壁
        WallType rightType = randomWallType();
        int rightValue = randomValue(rightType);
        spawnWall(world, level, 0.25f, rightType, rightValue);
    }

    /**
     * 単一の壁をスポーンする
     */
    private void spawnWall(World world, Level level, float laneX, WallType type, int value) {
        Color wallColor = getWallColor(type);
        Color textColor = getTextColor(type);
        String text = formatWallText(type, value);

        // 壁エンティティを生成
        world.spawn(
                new Transform(),
                // new Rectangle(WALL_WIDTH, WALL_HEIGHT, wallColor),
                new WallView(WALL_WIDTH, WALL_HEIGHT, wallColor,
                        wallColor, // border color
                        Color.BLACK, // text color
                        text
                ),
                new LaneTransform(laneX, SPAWN_Y),
                new LaneVelocity(0, level.getScrollSpeed()),
                new Wall(type, value)
        );
    }

    /**
     * 次のスポーン距離を計算してセット
     */
    private void scheduleNextSpawn(Level level) {
        float interval = level.getBaseSpawnInterval() / level.getDifficulty();
        // ±30% のランダム変動を加える
        float variance = interval * 0.3f;
        float randomOffset = (random.nextFloat() - 0.5f) * 2 * variance;

        level.setSpawnDistance(level.getSpawnDistance() + interval + randomOffset);
    }

    /**
     * 画面外に出た障害物を削除する
     */
    private void cleanupOffscreenEntities(World world) {
        for (Entity entity : world.query(Wall.class, LaneTransform.class)) {
            LaneTransform lt = entity.getComponent(LaneTransform.class).get();
            if (lt.getLaneY() > DESPAWN_Y) {
                entity.destroy();
            }
        }
    }

    // === ユーティリティメソッド ===
    private WallType randomWallType() {
        WallType[] types = WallType.values();
        return types[random.nextInt(types.length)];
    }

    private int randomValue(WallType type) {
        return switch (type) {
            case Add, Subtract ->
                random.nextInt(10) + 1;      // 1〜10
            case Multiply, Divide ->
                random.nextInt(5) + 2;    // 2〜6
        };
    }

    private Color getWallColor(WallType type) {
        return switch (type) {
            case Add ->
                new Color(0, 200, 100, 80);       // 緑（半透明）
            case Subtract ->
                new Color(229, 72, 77, 80);  // 赤（半透明）
            case Multiply ->
                new Color(0, 144, 255, 80);  // 青（半透明）
            case Divide ->
                new Color(255, 165, 0, 80);    // オレンジ（半透明）
        };
    }

    private Color getTextColor(WallType type) {
        return switch (type) {
            case Add ->
                new Color(0, 150, 75);       // 濃い緑
            case Subtract ->
                new Color(206, 44, 49); // 濃い赤
            case Multiply ->
                new Color(5, 136, 240); // 濃い青
            case Divide ->
                new Color(200, 120, 0);   // 濃いオレンジ
        };
    }

    private String formatWallText(WallType type, int value) {
        return switch (type) {
            case Add ->
                "+" + value;
            case Subtract ->
                "-" + value;
            case Multiply ->
                "x" + value;
            case Divide ->
                "÷" + value;
        };
    }
}
