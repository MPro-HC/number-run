package io.numberrun.Game;

import java.util.List;

import io.numberrun.Game.Player.PlayerState;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

// プレイヤーの状態を確認して、ゲームオーバーにするシステム
public class GameOverSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // プレイヤーの状態を確認して、ゲームオーバーにする処理を実装する
        // 1. world から PlayerState を持つエンティティを取得
        List<Entity> players = world.query(PlayerState.class);
        if (players.isEmpty()) {
            return;
        }

        PlayerState playerState = players.get(0).getComponent(PlayerState.class).get();

        // SceneState を取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームオーバー判定はプレイ中のみ行う
        if (sceneState.getCurrentScene() != SceneType.GAMEPLAY) {
            return;
        }
        // 2. PlayerState の状態を確認して、ゲームオーバー条件を満たしていたら
        // 2.1 ゲームオーバー処理を実行する 
        // SceneState を持つエンティティを取得し、
        // SceneState に GameOver をセットする
        if (playerState.getNumber() <= 0) {
            sceneState.setCurrentScene(SceneType.GAME_OVER);
        }
    }
}
