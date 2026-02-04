package io.numberrun.Game.Goal;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Image;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Scene.Scene;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class GoalSpawnSystem implements GameSystem {

    // LevelSystemと合わせる
    private static final float WALL_SPEED = 0.15f;
    private static final float SPAWN_Y = -0.5f;

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {

        // ゲームプレイ中のみ動作
        Optional<Entity> sceneEntity = world.query(Scene.class, SceneState.class).stream().findFirst();
        if (sceneEntity.isEmpty()) return;
        if (sceneEntity.get().getComponent(SceneState.class).get().getCurrentScene() != SceneType.GAMEPLAY) {
            return;
        }

        // GoalFlagがなければ作る
        List<Entity> flags = world.query(GoalFlag.class);
        if (flags.isEmpty()) {
            world.spawn(new GoalFlag(3)); // <-デバッグ用に3にしてるだけ
            return;
        }

        GoalFlag flag = flags.get(0).getComponent(GoalFlag.class).get();
        if (!flag.isSpawned()) return;

        // すでにゴールがあるなら生成しない
        if (!world.query(Goal.class).isEmpty()) return;

        // ゴール生成
        float sizePx = 800f;
        world.spawn(
                new Transform(),
                new Image(GoalSpawnSystem.class.getResource("/images/goal.png"), sizePx, sizePx).withZOrder(-5),
                new LaneSize(1.0f, 0.1f),
                new LaneTransform(0f, SPAWN_Y),
                new LaneVelocity(0f, WALL_SPEED),
                new Goal()
        );
        // この瞬間から通常生成を止める
    var goalflags = world.query(GoalFlag.class);
    if (!flags.isEmpty()) {
        GoalFlag gf = goalflags.get(0).getComponent(GoalFlag.class).get();
        gf.setStopSpawns(true);
    }

    }
}