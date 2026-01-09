package io.numberrun.Game;

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
        // TODO: プレイヤーの状態を確認して、ゲームオーバーにする処理を実装する
        // 1. world から PlayerState を持つエンティティを取得
        {
            // 2. PlayerState の状態を確認して、ゲームオーバー条件を満たしていたら
            {
                // 2.1 ゲームオーバー処理を実行する 
                // SceneState を持つエンティティを取得し、
                // SceneState に GameOver をセットする
            }
        }
    }
}
