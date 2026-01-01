package io.numberrun.Game.Lane;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Renderable;
import io.numberrun.Component.Transform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

// レーン上の座標と画面上の座標を変換するシステム
public class LaneMappingSystem implements GameSystem {

    @Override
    public int getPriority() {
        // 最後の方に処理したい
        return SystemPriority.VERY_LOW.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // レーンの情報を取得する
        List<Entity> laneEntities = world.query(LaneView.class);
        if (laneEntities.isEmpty()) {
            return;
        }
        LaneView laneView = laneEntities.get(0).getComponent(LaneView.class).get();

        // レーン上にある物体を取得する
        List<Entity> onLaneEntities = world.query(
                LaneTransform.class,
                Transform.class
        );

        // 画面座標へ変換する
        for (Entity entity : onLaneEntities) {
            LaneTransform laneTransform = entity.getComponent(LaneTransform.class).get();
            Transform transform = entity.getComponent(Transform.class).get();
            Optional<Renderable> renderableOpt = entity.getComponent(Renderable.class);

            // Lane Y 座標が小さいほど縮小する
            float zScale = calculateZScale(laneTransform, laneView);

            // Y 座標をもとに中央に寄せた X 座標を計算する
            float globalX = mapX(laneTransform, laneView, zScale);

            // 中心座標が渡されるけどこれを底面座標に変換する
            float globalY = mapY(laneTransform, laneView, renderableOpt, zScale);

            // Transform に適用する
            transform.setPosition(globalX, globalY);

            // スケールも適用する（スケールが無効でない場合のみ）
            if (laneTransform.shouldScaleByZ()) {
                transform.setScaleX(zScale);
                transform.setScaleY(zScale);
            }
        }

    }

    // Z スケールを計算（オブジェクトのbottom-centerを基準とする）
    private float calculateZScale(LaneTransform laneTransform, LaneView laneView) {
        // Lane Y 座標を正規化（-0.5が奥、0.5が手前）
        float normalizedY = laneTransform.getLaneY();

        // 奥行きに応じたスケール計算
        // minY（-0.5、奥）→ minWidth / maxWidth = 0.5
        // maxY（0.5、手前）→ 1.0
        float minScale = (float) laneView.minWidth() / laneView.maxWidth();
        float maxScale = 1.0f;

        // Lane Y を [minY, maxY] から [minScale, maxScale] に線形補間
        float t = (normalizedY - laneTransform.getMinY()) / (laneTransform.getMaxY() - laneTransform.getMinY());
        return minScale + (maxScale - minScale) * t;
    }

    // レーン上の x 座標から、グローバルな座標に変換する
    private float mapX(
            LaneTransform laneTransform,
            LaneView laneView,
            float zScale
    ) {
        float idealX = laneTransform.getLaneX() * laneView.maxWidth();

        // 実際は y 座標が小さいほど中央に寄る
        return idealX * zScale;
    }

    // 中心Lane Y座標から、グローバルな中心Y座標に変換する
    private float mapY(
            LaneTransform laneTransform,
            LaneView laneView,
            Optional<Renderable> renderableOpt,
            float zScale
    ) {
        // Lane Y 座標を正規化（-0.5が奥、0.5が手前）
        float normalizedY = laneTransform.getLaneY();

        // Lane Y を画面の高さにマッピング
        // minY（-0.5、奥）→ レーンの上端
        // maxY（0.5、手前）→ レーンの下端
        float t = (normalizedY - laneTransform.getMinY()) / (laneTransform.getMaxY() - laneTransform.getMinY());
        float screenY = -laneView.maxHeight() / 2f + t * laneView.maxHeight();

        // オブジェクトの高さを考慮して底面を基準にする
        if (renderableOpt.isPresent()) {
            float objectHeight = renderableOpt.get().getHeight() * zScale;
            screenY -= objectHeight / 2f; // 中心から底面へ
        }

        return screenY;
    }

}
