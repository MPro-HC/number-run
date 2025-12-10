package io.numberrun;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Text;
import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.Core.GameEngine;
import io.numberrun.Game.MovementSystem;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 * プレイヤー操作システム WASDキーでプレイヤーを移動する
 */
class PlayerMovementSystem implements GameSystem {

    private static final float SPEED = 400f;

    @Override
    public void update(World world, float deltaTime) {
        // このシステムはonInputで処理するので、updateでは何もしない
    }

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // Transform と Velocity を持つエンティティを取得（プレイヤー）
        for (Entity entity : world.query(Transform.class, Velocity.class)) {
            entity.getComponent(Velocity.class).ifPresent(velocity -> {
                float vx = 0;
                float vy = 0;

                if (inputState.isKeyPressed(KeyEvent.VK_W) || inputState.isKeyPressed(KeyEvent.VK_UP)) {
                    vy -= SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_S) || inputState.isKeyPressed(KeyEvent.VK_DOWN)) {
                    vy += SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_A) || inputState.isKeyPressed(KeyEvent.VK_LEFT)) {
                    vx -= SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_D) || inputState.isKeyPressed(KeyEvent.VK_RIGHT)) {
                    vx += SPEED;
                }

                velocity.setVelocity(vx, vy);
            });

        }
    }
}

/**
 * 回転システム エンティティを回転させる（デモ用）
 */
class RotationSystem implements GameSystem {

    @Override
    public void update(World world, float deltaTime) {
        // 回転マーカーを持つエンティティを回転（Ovalを使用）
        for (Entity entity : world.query(Transform.class, Oval.class)) {
            entity.getComponent(Transform.class).ifPresent(transform -> {
                transform.setRotation(transform.getRotation() + 90 * deltaTime);
            });
        }
    }
}

public class App {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        // ゲームエンジンの作成
        GameEngine engine = new GameEngine("Number Run - ECS Demo", WINDOW_WIDTH, WINDOW_HEIGHT);
        engine.setBackgroundColor(new Color(40, 40, 40));

        World world = engine.getWorld();

        // プレイヤーエンティティ（青い四角、WASDで移動）
        Rectangle playerRect = new Rectangle(50, 50, Color.CYAN);
        playerRect.setZOrder(1);
        world.spawn(
                new Transform(400, 300),
                new Velocity(0, 0),
                playerRect
        );

        // 静的な障害物エンティティ（赤い四角）
        Rectangle redRect = new Rectangle(60, 60, Color.RED);
        redRect.setZOrder(2);
        world.spawn(
                new Transform(200, 200),
                redRect
        );

        // テキストエンティティ
        world.spawn(
                new Transform(400, 50),
                new Text("ECS Demo - Use WASD or Arrow Keys to move", Color.WHITE, new Font("SansSerif", Font.BOLD, 16))
        );

        // システムの追加
        world.addSystems(
                new PlayerMovementSystem(), // プレイヤー操作
                new RotationSystem(), // 回転
                new MovementSystem() // 移動（Velocityを適用）
        );

        // ゲーム開始
        engine.start();
    }
}
