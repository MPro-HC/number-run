package io.numberrun.Game.GameOver;

import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

// シーンステートがゲームオーバーになったら、何かしらのゲームオーバーを表示する
public class GameOverSystem implements GameSystem {

    @Override
    public void update(World world, float deltaTime) {
        // ゲームオーバーの時に、 GameOverOverlay が存在しなかったら GameOverOverlay を表示する
    }
}
