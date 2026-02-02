package io.numberrun.Game.Effect;

import java.awt.Color;
import java.util.List;

import io.numberrun.Component.GradientRectangle;
import io.numberrun.Component.Timer;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Easing.Easing;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

public class DamageEffectSystem implements GameSystem {

    // #BB0D10 59%
    private static final Color colorTop = new Color(187, 13, 16, 150);
    // #BB0D10 50%
    private static final Color colorBottom = new Color(187, 13, 16, 128);

    @Override
    public void update(World world, float deltaTime) {
        // Easing を持った DamageEffect エンティティ
        List<Entity> damageEffects = world.query(Easing.class, DamageEffect.class);

        for (Entity entity : damageEffects) {
            Easing easing = entity.getComponent(Easing.class).get();
            // グラデーションを持ってるはず
            GradientRectangle rectangle = entity.getComponent(GradientRectangle.class).get();

            // Easing を進める (deltaTime は秒単位なのでミリ秒に変換)
            easing.tick(deltaTime * 1000);

            // 全体的なグラデーションの透明度を変更する
            // ただし、0->1 を 0->1->0 に変更する
            float easeValue = 1.0f - Math.abs(2.0f * easing.easeOutCubic() - 1.0f);
            rectangle.setOpacity(easeValue);

            // Easing が完了したらエンティティを削除する
            if (easing.isFinished()) {
                entity.destroy();
            }
        }
    }

    public static void spawnDamageEffect(World world, int windowWidth, int windowHeight) {
        world.spawn(
                new DamageEffect(),
                new Easing(
                        new Timer(
                                250, // milliseconds
                                Timer.TimerMode.Once
                        )
                ),
                new GradientRectangle(
                        windowWidth, // width
                        windowHeight, // height
                        colorTop, // top color
                        colorBottom, // bottom color
                        200 // zOrder
                ),
                new Transform()
        );

    }

}
