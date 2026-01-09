package io.numberrun.Game.Player;

import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

public class PlayerMovementSystem implements GameSystem {

    private static final float SPEED = 1.0f;

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority();
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // TODO: Sample.java の PlayerMovementSystem を参考にプレイヤーの動きを実装する

        // 1. world から PlayerView, LaneVelocity を持つエンティティを取得する
        {
            // 2. LaneVelocity を取得して、
            {
                // 3. もし左右キーが押されていたら、速度をセットする
                // 3.0 速度 vx を 0 に初期化
                // 3.1 もし ← キーが押されていたら
                {
                    // 3.1.1 速度 vx を -SPEED に設定
                }
                // 3.2 もし → キーが押されていたら
                {
                    // 3.2.1 速度 vx を SPEED に設定
                }
                // 4. LaneVelocity に vx をセットする
            }
        }
    }

}
