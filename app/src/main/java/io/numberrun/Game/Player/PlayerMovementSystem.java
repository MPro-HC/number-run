package io.numberrun.Game.Player;

import java.awt.event.KeyEvent;
import java.util.List;

import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.System.Entity;
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
        // Sample.java の PlayerMovementSystem を参考にプレイヤーの動きを実装する
        List<Entity> players = world.query(PlayerView.class, LaneVelocity.class);

        // 1. world から PlayerView, LaneVelocity を持つエンティティを取得する
        for (Entity entity : players) {
            // 2. LaneVelocity を取得して、
            entity.getComponent(LaneVelocity.class).ifPresent(laneVelocity -> {
                // 3. もし左右キーが押されていたら、速度をセットする
                // 3.0 速度 vx を 0 に初期化
                float vx = 0.0f;
                // 3.1 もし ← キーか A が押されていたら
                if (inputState.isKeyPressed(KeyEvent.VK_LEFT) || inputState.isKeyPressed(KeyEvent.VK_A)) {
                    // 3.1.1 速度 vx を -SPEED に設定
                    vx = -SPEED;
                }
                // 3.2 もし → キーか D が押されていたら
                if (inputState.isKeyPressed(KeyEvent.VK_RIGHT) || inputState.isKeyPressed(KeyEvent.VK_D)) {
                    // 3.2.1 速度 vx を SPEED に設定
                    vx = SPEED;
                }
                // 4. LaneVelocity に vx をセットする
                laneVelocity.setVx(vx);
            });
        }
    }

}
