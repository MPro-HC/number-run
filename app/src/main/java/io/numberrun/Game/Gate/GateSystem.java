package io.numberrun.Game.Gate;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Player.Power;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

public class GateSystem implements GameSystem {

    @Override
    public void update(World world, float deltaTime) {
        // 1. プレイヤー（Powerを持つエンティティ）を探す
        Entity player = null;
        for (Entity e : world.query(Power.class, Transform.class, Rectangle.class)) {
            player = e;
            break;
        }

        if (player == null) {
            return;
        }

        // プレイヤー情報の取得
        Power playerPower = player.getComponent(Power.class).get();
        Transform pTrans = player.getComponent(Transform.class).get();
        Rectangle pRect = player.getComponent(Rectangle.class).get();

        // 2. 全てのゲートとの当たり判定
        for (Entity gateEntity : world.query(Gate.class, Transform.class, Rectangle.class)) {
            Gate gate = gateEntity.getComponent(Gate.class).get();
            Transform gTrans = gateEntity.getComponent(Transform.class).get();
            Rectangle gRect = gateEntity.getComponent(Rectangle.class).get();

            // ★ここが変更点！物理的な「重なり」を判定する
            if (isColliding(pTrans, pRect, gTrans, gRect)) {

                // 計算実行
                applyGateEffect(playerPower, gate);

                System.out.println("Gate Passed! Power: " + playerPower.getValue());

                // ゲート消滅
                gateEntity.destroy();
            }
        }
    }

    // 矩形同士の衝突判定（AABB: Axis-Aligned Bounding Box）
    private boolean isColliding(Transform t1, Rectangle r1, Transform t2, Rectangle r2) {
        // 中心座標
        float x1 = t1.getX();
        float y1 = t1.getY();
        float x2 = t2.getX();
        float y2 = t2.getY();

        // 幅・高さの半分（中心からの距離）
        // ※RectangleにはgetWidth()が必要ですが、もしprivateなら
        //   Rectangleクラスに getter を追加してください！
        float w1 = r1.getWidth() / 2;
        float h1 = r1.getHeight() / 2;
        float w2 = r2.getWidth() / 2;
        float h2 = r2.getHeight() / 2;

        // 衝突判定：X軸とY軸の両方で重なっているか？
        boolean collisionX = Math.abs(x1 - x2) < (w1 + w2);
        boolean collisionY = Math.abs(y1 - y2) < (h1 + h2);

        return collisionX && collisionY;
    }

    private void applyGateEffect(Power power, Gate gate) {
        // (前と同じなので省略可能)
        switch (gate.getOperationType()) {
            case ADD:
                power.add(gate.getValue());
                break;
            case MULTIPLY:
                power.multiply(gate.getValue());
                break;
        }
    }
}
