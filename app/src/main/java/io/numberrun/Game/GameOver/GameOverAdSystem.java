package io.numberrun.Game.GameOver;

import java.util.List;

import io.numberrun.Component.NamedValue;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Easing.Easing;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;
import io.numberrun.UI.InputType;

public class GameOverAdSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // ease in と ease out アニメーション

        List<Entity> ads = world.query(GameOverAd.class);

        for (Entity entity : ads) {
            GameOverAd ad = entity.getComponent(GameOverAd.class).get();
            Easing transition = ad.getTransition();
            Easing pulse = ad.getPulse();
            NamedValue<Float> initialYValue = entity.getComponent(NamedValue.class).get();
            Transform transform = entity.getComponent(Transform.class).get();

            // Easing を進める (deltaTime は秒単位なのでミリ秒に変換)
            ad.tickEasing(deltaTime * 1000);

            // 座標を更新する
            float transitionValue = transition.easeOutSine();
            float initialY = initialYValue.getValue(); // 初期のY座標 == 画面下端
            if (ad.isExiting()) {
                // 退場
                // 0 -> 1 で 中央から画面下へ移動
                float newY = initialY * transitionValue;
                transform.setY(newY);

                // もし easing が終了したら破棄する
                if (transition.isFinished()) {
                    entity.destroy();
                }
            } else {
                if (transition.isFinished()) {
                    // スケールを変更する
                    float pulseValue = pulse.easeInOut();
                    float scale = 1.0f - 0.025f + 0.05f * pulseValue;
                    transform.setScale(scale);

                } else {
                    // 入場
                    // 0 -> 1 で 画面下から中央へ移動
                    // 中央はゼロなのでマップすればいい
                    float newY = initialY * (1.0f - transitionValue);
                    transform.setY(newY);
                }
            }
        }
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // マウスクリックイベントのみ処理
        if (event.getType() != InputType.MOUSE_CLICKED) {
            return;
        }

        // 画面がクリックされたら広告を退場させる
        List<Entity> ads = world.query(GameOverAd.class);

        for (Entity entity : ads) {
            GameOverAd ad = entity.getComponent(GameOverAd.class).get();
            Easing transition = ad.getTransition();

            // 既に退場中の場合はスキップ
            if (ad.isExiting()) {
                continue;
            }

            // 退場フラグをセット
            ad.setExiting(true);

            // Easingをリセットして退場アニメーションを開始
            transition.restart();
        }
    }

}
