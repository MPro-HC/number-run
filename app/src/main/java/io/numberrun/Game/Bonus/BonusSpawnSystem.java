package io.numberrun.Game.Bonus;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Text;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Wall.WallView;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

import java.awt.Color;
import java.awt.Font;

public class BonusSpawnSystem implements GameSystem {

    // 固定並び
    private static final int[] VALUES = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

    // LevelSystemと合わせる
    private static final float WALL_SPEED = 0.15f;
    private static final float SPAWN_Y = -0.5f;

    // ★短い出現間隔
    private static final float BONUS_SPAWN_INTERVAL_SEC = 0.6f;

    // 内部タイマー
    private float spawnTimer = 0f;

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState scene = scenes.get(0).getComponent(SceneState.class).get();
        if (scene.getCurrentScene() != SceneType.BONUS) return;

        // BONUS中だけ HUD を用意する
        if (world.query(BonusHUD.class).isEmpty()) {
            world.spawn(
                new BonusHUD(),
                new Text("SCORE: 0", Color.WHITE, new Font("SansSerif", Font.BOLD, 36), 150),
                new Transform(0, -300)
            );
        }

        // BonusState がなければ何もしない（ゴール通過で作る）
        List<Entity> bsList = world.query(BonusState.class);
        if (bsList.isEmpty()) return;

        BonusState bs = bsList.get(0).getComponent(BonusState.class).get();

        // 全部生成済みなら終了
        if (bs.spawnIndex >= VALUES.length) return;

        // LaneView を取る（サイズ計算用）
        Optional<Entity> laneViewEntity = world.query(LaneView.class).stream().findFirst();
        if (laneViewEntity.isEmpty()) return;
        LaneView laneView = laneViewEntity.get().getComponent(LaneView.class).get();

        // タイマー更新
        spawnTimer += deltaTime;

        // 速度×間隔ぶんだけ後ろにずらして重なりを防ぐ
        float gap = WALL_SPEED * BONUS_SPAWN_INTERVAL_SEC;

        // ★ここがポイント：壁が既にあっても追加で出す（同時に何枚でもOK）
        while (spawnTimer >= BONUS_SPAWN_INTERVAL_SEC && bs.spawnIndex < VALUES.length) {
            int value = VALUES[bs.spawnIndex];

            float y = SPAWN_Y - gap * bs.spawnIndex; // 連続で出しても縦に並ぶ

            spawnScoreWall(world, laneView, value, y);

            bs.spawnIndex++;
            spawnTimer -= BONUS_SPAWN_INTERVAL_SEC;
        }
    }

    private void spawnScoreWall(World world, LaneView laneView, int value, float laneY) {
        int wallWidth = laneView.maxWidth();
        int wallHeight = laneView.maxHeight() * 400 / 960;

        var bgStart = new Color(255, 235, 59);
        var bgEnd   = new Color(255, 193, 7);
        var border  = new Color(40, 40, 40);

        world.spawn(
            new Transform(),
            new WallView(
                wallWidth * 0.95f,
                wallHeight,
                List.of(bgStart, bgEnd),
                border,
                Color.WHITE,
                String.valueOf(value),
                6f,
                Color.BLACK
            ),
            new LaneSize(1.0f, 0.1f),
            new LaneTransform(0f, laneY),
            new LaneVelocity(0f, WALL_SPEED),
            new ScoreWall(value)
        );
    }
}
