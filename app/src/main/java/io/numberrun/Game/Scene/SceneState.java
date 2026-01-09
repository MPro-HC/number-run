package io.numberrun.Game.Scene;

import io.numberrun.Component.Component;

// ゲーム画面の遷移を管理
public class SceneState implements Component {

    private SceneType currentScene;

    public SceneState() {
        this.currentScene = SceneType.MAIN_MENU;
    }

    public SceneType getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(SceneType currentScene) {
        this.currentScene = currentScene;
    }
}
