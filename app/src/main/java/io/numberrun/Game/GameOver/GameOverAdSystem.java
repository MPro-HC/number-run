package io.numberrun.Game.GameOver;

import java.util.List;

import io.numberrun.Component.NamedValue;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Easing.Easing;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class GameOverAdSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // ease in と ease out アニメーション

        List<Entity> ads = world.query(GameOverAd.class, Easing.class);

        for (Entity entity : ads) {
            Easing easing = entity.getComponent(Easing.class).get();
            GameOverAd ad = entity.getComponent(GameOverAd.class).get();
            NamedValue<Float> initialYValue = entity.getComponent(NamedValue.class).get();
            Transform transform = entity.getComponent(Transform.class).get();

            // Easing を進める (deltaTime は秒単位なのでミリ秒に変換)
            easing.tick(deltaTime * 1000);

            // 座標を更新する
            float easeValue = easing.easeInOut();
            float initialY = initialYValue.getValue(); // 初期のY座標 == 画面下端
            if (ad.isExiting()) {
                // 退場
                // 0 -> 1 で 中央から画面上へ移動
                float newY = initialY * easeValue * -1.0f;
                transform.setY(newY);
            } else {
                // 入場
                // 0 -> 1 で 画面下から中央へ移動
                // 中央はゼロなのでマップすればいい
                float newY = initialY * (1.0f - easeValue);
                transform.setY(newY);
            }
        }
    }
}
