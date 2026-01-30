package io.numberrun.Game.Scene;

import io.numberrun.Component.Component;

// ゲーム画面の遷移を管理
public class SceneState implements Component {

    private SceneType currentScene;

    public SceneState() {
        this.currentScene = SceneType.TITLE;
    }

    public SceneState(SceneType initialScene) {
        this.currentScene = initialScene;
    }

    public SceneType getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(SceneType sceneType) {
        this.currentScene = sceneType;
    }
}
