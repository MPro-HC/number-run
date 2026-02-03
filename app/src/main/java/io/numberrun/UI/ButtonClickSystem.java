package io.numberrun.UI;

import java.util.List;

import io.numberrun.Component.Button;
import io.numberrun.Component.Renderable;
import io.numberrun.Component.Transform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class ButtonClickSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // マウスクリックイベントのみ処理
        if (event.getType() != InputType.MOUSE_CLICKED) {
            return;
        }

        float mx = event.getMouseX();
        float my = event.getMouseY();

        // Button を取得
        List<Entity> buttonEntities = world.query(Button.class, Renderable.class, Transform.class);

        for (Entity entity : buttonEntities) {
            Button button = entity.getComponent(Button.class).get();
            Renderable renderable = entity.getComponent(Renderable.class).get();
            Transform transform = entity.getComponent(Transform.class).get();

            // クリックされた座標がボタンの範囲内にあるかチェック
            if (isClickOnButton(renderable, transform, mx, my)) {
                button.onClick(world);
            }
        }

    }

    private boolean isClickOnButton(Renderable renderable, Transform transform, float mx, float my) {
        float bx = transform.getX(); // center
        float by = transform.getY(); // center
        float bw = renderable.getWidth();
        float bh = renderable.getHeight();

        return (mx >= bx - bw / 2) && (mx <= bx + bw / 2)
                && (my >= by - bh / 2) && (my <= by + bh / 2);
    }
}
