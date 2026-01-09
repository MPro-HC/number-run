package io.numberrun.Game.Stage;

import java.awt.Color;
import java.util.Random;

import io.numberrun.Game.Player.LanePosition;
import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.Game.Gate.Gate;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

/**
 * ステージ進行管理システム
 */
public class StageSystem implements GameSystem {

    private float timer = 0;
    private float spawnInterval = 2.0f; // 2秒ごとに生成
    private Random random = new Random();

    @Override
    public void update(World world, float deltaTime) {
        timer += deltaTime;

        // 一定時間が経過したら生成処理を行う
        if (timer >= spawnInterval) {
            spawnGate(world);
            timer = 0; // タイマーリセット
        }
    }

    // ゲートを生成するメソッド（Modelとしてのデータ定義）
    private void spawnGate(World world) {
        // 1. ランダムなレーンを決める (0.0 ～ 2.0 の間)
        // ※整数で 0, 1, 2 のどれかに寄せるなら random.nextInt(3)
        float randomLane = (float) random.nextInt(3); 
        
        // 2. ランダムな値を決める (+1 ～ +9)
        int gateValue = random.nextInt(9) + 1;
        
        // 3. 数値の生成（エンティティのスポーン）
        // ここで「ゲートが存在する」という事実をWorldに書き込む
        world.spawn(
            // 位置: 画面の上の方(Y=-50), レーンに応じたX座標は LaneMovementSystem が計算してくれる
            new Transform(0, -50), 
            
            // 動き: 下に向かって進んでくる (Y方向にスピードを持つ)
            new Velocity(0, 200), // 毎秒200ピクセルで迫ってくる
            
            // データ: レーン情報
            new LanePosition(randomLane, 3),
            
            // データ: ゲートの効果 (+gateValue)
            new Gate(Gate.OperationType.ADD, gateValue),
            
            // 見た目?
            new Rectangle(80, 50, Color.GREEN) 
        );
        
        System.out.println("ゲート生成: Lane " + randomLane + ", Value +" + gateValue);
    }
}