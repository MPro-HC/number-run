package io.numberrun.Game.GameOver;

import java.awt.event.KeyEvent;
import java.util.List;

import io.numberrun.Game.Grid.GridLine;
import io.numberrun.Game.Grid.GridLineSpawnSystem;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Level.Level;
import io.numberrun.Game.Player.PlayerState;
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

// ゲームオーバーからゲームプレイに戻る処理
public class GameOverExitSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // TODO: 他の入力イベントにも対応？
        // とりあえずスペースキーを判定
        if (!inputState.isKeyPressed(KeyEvent.VK_SPACE)) {
            // スペース以外なら無視
            return;
        }

        // 広告が表示中、存在しているなら閉じれないように
        List<Entity> ads = world.query(GameOverAd.class);
        if (!ads.isEmpty()) {
            return;
        }

        // SceneState を取得して、ゲームオーバーシーンからゲームプレイシーンに戻す処理を実装する
        // 1. world から SceneState を持つエンティティを取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームプレイシーンに戻す判定を行う
        if (sceneState.getCurrentScene() != SceneType.GAME_OVER) {
            return;
        }

        // ゲームオーバーシーンからゲームプレイシーンに戻す処理を実行する
        // 1. SceneState をゲームプレイシーンにセット
        sceneState.setCurrentScene(SceneType.GAMEPLAY);

        // オーバーレイをデスポーン
        despawnOverlay(world);

        //  壁を全てデスポーン
        despawnAllWalls(world);

        // レベル状態をリセット
        resetLevel(world);

        //  プレイヤーをデスポーン
        respawnPlayer(world);

        // グリッドをリスポーン
        respawnGridLines(world);
    }

    public static void exitGameOverEnterGamePlay(World world) {
        // 広告が表示中、存在しているなら閉じれないように
        List<Entity> ads = world.query(GameOverAd.class);
        if (!ads.isEmpty()) {
            return;
        }

        // SceneState を取得して、ゲームオーバーシーンからゲームプレイシーンに戻す処理を実装する
        // 1. world から SceneState を持つエンティティを取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームプレイシーンに戻す判定を行う
        if (sceneState.getCurrentScene() != SceneType.GAME_OVER) {
            return;
        }

        // ゲームオーバーシーンからゲームプレイシーンに戻す処理を実行する
        // 1. SceneState をゲームプレイシーンにセット
        sceneState.setCurrentScene(SceneType.GAMEPLAY);

        // オーバーレイをデスポーン
        despawnOverlay(world);

        //  壁を全てデスポーン
        despawnAllWalls(world);

        // レベル状態をリセット
        resetLevel(world);

        //  プレイヤーをデスポーン
        respawnPlayer(world);

        // グリッドをリスポーン
        respawnGridLines(world);
    }

    public static void exitGameOverEnterTitle(World world) {
        // 広告が表示中、存在しているなら閉じれないように
        List<Entity> ads = world.query(GameOverAd.class);
        if (!ads.isEmpty()) {
            return;
        }

        // SceneState を取得して、ゲームオーバーシーンからゲームプレイシーンに戻す処理を実装する
        // 1. world から SceneState を持つエンティティを取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // タイトルへ
        if (sceneState.getCurrentScene() != SceneType.GAME_OVER) {
            return;
        }

        // ゲームオーバーシーンからタイトルシーンに戻す処理を実行する
        // 1. SceneState をタイトルシーンにセット
        sceneState.setCurrentScene(SceneType.TITLE);

        // オーバーレイをデスポーン
        despawnOverlay(world);

        //  壁を全てデスポーン
        despawnAllWalls(world);

        // レベル状態をリセット
        resetLevel(world);

        //  プレイヤーをデスポーン
        respawnPlayer(world);

        // グリッドをリスポーン
        respawnGridLines(world);
    }

    private static void despawnOverlay(World world) {
        List<Entity> overlays = world.query(GameOverOverlay.class);
        for (Entity overlay : overlays) {
            overlay.destroy();
        }
    }

    // 壁をデスポーン
    private static void despawnAllWalls(World world) {
        List<Entity> walls = world.query(Wall.class);
        for (Entity wall : walls) {
            wall.destroy();
        }
    }

    // プレイヤーをスポーン
    private static void respawnPlayer(World world) {
        List<Entity> players = world.query(PlayerState.class);
        for (Entity player : players) {
            player.destroy();
        }
        PlayerView.setupInitialPlayer(world);
    }

    // グリッドをリスポーン
    private static void respawnGridLines(World world) {
        {
            List<Entity> lines = world.query(GridLine.class);
            for (Entity line : lines) {
                line.destroy();
            }
        }

        List<Entity> laneViews = world.query(LaneView.class);
        if (laneViews.isEmpty()) {
            return;
        }
        laneViews.get(0).getComponent(LaneView.class).ifPresent(laneView -> {
            GridLineSpawnSystem.setupInitialLines(world, laneView);
        });
    }

    // レベルをリセット
    private static void resetLevel(World world) {
        // レベル情報を取得して、生成回数をリセット
        List<Entity> levels = world.query(Level.class);
        if (levels.isEmpty()) {
            return;
        }

        Level level = levels.get(0).getComponent(Level.class).get();
        level.reset();
    }

}
