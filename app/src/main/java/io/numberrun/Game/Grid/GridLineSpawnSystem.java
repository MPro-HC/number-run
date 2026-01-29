package io.numberrun.Game.Grid;

import java.util.List;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class GridLineSpawnSystem implements GameSystem {

    private float spawnTimer = 0f;
    private final float spawnInterval = 0.1f; // ライン生成間隔（秒）

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

        // 画面外を削除
        cleanupLines(world);

        // TODO: レーンが流れてるかのチェック
        spawnTimer += deltaTime;

        while (spawnTimer >= spawnInterval) {
            spawnTimer -= spawnInterval;

            spawnLine(world, laneView);
        }
    }

    private void cleanupLines(World world) {
        List<Entity> lineEntities = world.query(GridLine.class, LaneTransform.class);
        for (Entity entity : lineEntities) {
            LaneTransform laneTransform = entity.getComponent(LaneTransform.class).get();
            // LaneYが一定以上手前に来たら削除
            if (laneTransform.getLaneY() > 0.6f) {
                entity.destroy();
            }
        }
    }

    private void spawnLine(World world, LaneView laneView) {
        // サイズはレーンと同じ幅
        float width = laneView.maxWidth();
        float height = 4; // 固定で細め

        // ラインを生成して World に追加する
        world.spawn(
                new GridLine(), // タグ
                new LaneTransform(
                        0f, // 中央
                        -0.5f // 一番上
                ),
                new LaneVelocity(0, GridLine.Y_SPEED), // 壁と同じスピード
                new Transform(),
                // 長方形で表現する
                new Rectangle(width, height, GridLine.LINE_COLOR, true)
        );
    }

    public static void setupInitialLines(World world, LaneView laneView) {
        // 最初に数本ラインを生成しておく
        final float lineSpacing = 0.02f; // ライン間隔
        final int initialLineCount = (int) (1 / lineSpacing);

        for (int i = 0; i < initialLineCount; i++) {
            float laneY = -0.5f + i * lineSpacing;
            world.spawn(
                    new GridLine(), // タグ
                    new LaneTransform(
                            0f, // 中央
                            laneY
                    ),
                    new LaneVelocity(0, GridLine.Y_SPEED), // 壁と同じスピード
                    new Transform(),
                    // 長方形で表現する
                    new Rectangle(laneView.maxWidth(), 4, GridLine.LINE_COLOR, true)
            );
        }
    }
}
