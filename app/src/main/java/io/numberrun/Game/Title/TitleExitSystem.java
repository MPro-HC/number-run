package io.numberrun.Game.Title;

import java.awt.event.KeyEvent;
import java.util.List;

import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

public class TitleExitSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    // @Override
    // public void update(World world, float deltaTime) {
    //     // 退場アニメーション
    //     List<Entity> ads = world.query(TitleOverlay.class, Renderable.class);
    //     for (Entity entity : ads) {
    //         Easing transition = entity.getComponent(Easing.class).get();
    //         // Easing を進める (deltaTime は秒単位なのでミリ秒に変換)
    //         transition.tick(deltaTime * 1000);
    //         Renderable renderable = entity.getComponent(Renderable.class).get();
    //         // 退場
    //         // もし easing が終了したら破棄する
    //         if (transition.isFinished()) {
    //             entity.destroy();
    //         }
    //     }
    // }
    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // TODO: 他の入力イベントにも対応？
        // とりあえずスペースキーを判定
        if (!inputState.isKeyPressed(KeyEvent.VK_SPACE)) {
            // スペース以外なら無視
            return;
        }

        // 必要に応じてタイトル画面を終了してゲームプレイシーンに戻す
        exitTitleEnterGameplay(world);
    }

    public static void exitTitleEnterGameplay(World world) {
        // SceneState を取得して、タイトルからゲームプレイシーンに戻す処理を実装する
        // 1. world から SceneState を持つエンティティを取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームプレイシーンに戻す判定を行う
        if (sceneState.getCurrentScene() != SceneType.TITLE) {
            return;
        }

        // タイトルからゲームプレイシーンに戻す処理を実行する
        // 1. SceneState をゲームプレイシーンにセット
        sceneState.setCurrentScene(SceneType.GAMEPLAY);

        // オーバーレイをデスポーン
        TitleExitSystem.despawnOverlay(world);

    }

    private static void despawnOverlay(World world) {
        List<Entity> overlays = world.query(TitleOverlay.class);
        for (Entity overlay : overlays) {
            overlay.destroy();
        }
    }

}
