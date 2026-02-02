package io.numberrun.Game.GameOver;

import java.util.List;

import io.numberrun.Component.NamedValue;
import io.numberrun.Component.Transform;
import io.numberrun.Component.Image;
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

        float mx = event.getMouseX();
        float my = event.getMouseY();

        // 画面がクリックされたら広告を退場させる
        List<Entity> ads = world.query(GameOverAd.class);

        for (Entity entity : ads) {
            GameOverAd ad = entity.getComponent(GameOverAd.class).get();
            Easing transition = ad.getTransition();

            // 既に退場中の場合はスキップ
            if (ad.isExiting()) {
                continue;
            }


            // ×ボタン領域だけ反応させる
            Entity closeButton = findCloseButtonChild(entity);
            if (closeButton == null) {
                continue;// 見つからないなら、誤動作を避けるため「閉じない」にしておく
            }
            if (!isClickOnCloseButton(entity, closeButton, mx, my)) {
                continue; // ×以外をクリック -> 何もしない
            }

            // 退場フラグをセット
            ad.setExiting(true);

            // Easingをリセットして退場アニメーションを開始
            transition.restart();
            break; // 広告を追加したとしてもひとつ閉じたら終わり
        }
    }
    private Entity findCloseButtonChild(Entity adEntity) {
        for (Entity child : adEntity.getChildren()) {
            if (!child.hasComponent(NamedValue.class)) continue;

            NamedValue<?> nv = child.getComponent(NamedValue.class).get();
            if ("closeButton".equals(nv.getName())) {
                return child;
            }
        }
        return null;
    }

    private boolean isClickOnCloseButton(Entity adEntity, Entity closeButton, float mx, float my) {
        Transform pt = adEntity.getComponent(Transform.class).get(); // 親（広告）
        Transform ct = closeButton.getComponent(Transform.class).get(); // 子(×）
        Image img = closeButton.getComponent(Image.class).get();

        // 親がscaleしているので、子の位置も親scaleの影響を受ける
        float gx = pt.getX() + ct.getX() * pt.getScaleX(); // ×の中心座標（グローバル）
        float gy = pt.getY() + ct.getY() * pt.getScaleY();

        float sx = pt.getScaleX() * ct.getScaleX();
        float sy = pt.getScaleY() * ct.getScaleY();

        float halfW = (img.getWidth() * sx) / 2f;
        float halfH = (img.getHeight() * sy) / 2f;

        return (gx - halfW <= mx && mx <= gx + halfW &&
                gy - halfH <= my && my <= gy + halfH);
    }


}
