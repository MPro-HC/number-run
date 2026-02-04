package io.numberrun.Game.Result;

import java.awt.event.KeyEvent;
import java.util.List;

import io.numberrun.Game.Goal.FinalScore;
import io.numberrun.Game.Goal.Goal;
import io.numberrun.Game.Goal.GoalFlag;
import io.numberrun.Game.Grid.GridLine;
import io.numberrun.Game.Grid.GridLineSpawnSystem;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Level.Level;
import io.numberrun.Game.Obstacle.Obstacle;
import io.numberrun.Game.Player.PlayerView;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

public class ResultExitSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // とりあえずスペースでリトライ
        if (!inputState.isKeyPressed(KeyEvent.VK_SPACE)) return;

        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        if (sceneState.getCurrentScene() != SceneType.RESULT) return;

        exitResultEnterGamePlay(world);
    }

    public static void exitResultEnterGamePlay(World world) {
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        if (sceneState.getCurrentScene() != SceneType.RESULT) return;

        sceneState.setCurrentScene(SceneType.GAMEPLAY);

        despawnOverlay(world);
        despawnAllWalls(world);
        despawnAllObstacles(world);
        despawnGoalAndFlags(world);

        resetLevel(world);
        respawnPlayer(world);
        respawnGridLines(world);
    }

    public static void exitResultEnterTitle(World world) {
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        if (sceneState.getCurrentScene() != SceneType.RESULT) return;

        sceneState.setCurrentScene(SceneType.TITLE);

        despawnOverlay(world);
        despawnAllWalls(world);
        despawnAllObstacles(world);
        despawnGoalAndFlags(world);

        resetLevel(world);
        respawnPlayer(world);
        respawnGridLines(world);
    }

    private static void despawnOverlay(World world) {
        for (Entity e : world.query(ResultOverlay.class)) e.destroy();
    }

    private static void despawnAllWalls(World world) {
        for (Entity e : world.query(Wall.class)) e.destroy();
    }

    private static void despawnAllObstacles(World world) {
        for (Entity e : world.query(Obstacle.class)) e.destroy();
    }

    private static void despawnGoalAndFlags(World world) {
        for (Entity e : world.query(Goal.class)) e.destroy();
        for (Entity e : world.query(GoalFlag.class)) e.destroy();
        for (Entity e : world.query(FinalScore.class)) e.destroy();
    }

    private static void resetLevel(World world) {
        List<Entity> levels = world.query(Level.class);
        if (!levels.isEmpty()) {
            levels.get(0).getComponent(Level.class).get().reset();
        }
    }

    private static void respawnPlayer(World world) {
        // 既存プレイヤーを消してから再生成
        for (Entity p : world.query(io.numberrun.Game.Player.PlayerState.class)) p.destroy();
        PlayerView.setupInitialPlayer(world);
    }

    private static void respawnGridLines(World world) {
        // 既存の線を消す
        List<Entity> lines = world.query(GridLine.class);
        for (Entity line : lines) {
            line.destroy();
        }

        // LaneView を取得して、初期線を生成
        List<Entity> laneViews = world.query(LaneView.class);
        if (laneViews.isEmpty()) {
            return;
        }

        laneViews.get(0).getComponent(LaneView.class).ifPresent(laneView -> {
            GridLineSpawnSystem.setupInitialLines(world, laneView);
        });
    }

}