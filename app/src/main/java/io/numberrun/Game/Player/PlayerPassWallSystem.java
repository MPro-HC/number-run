package io.numberrun.Game.Player;

// プレイヤーが壁を通過したときの処理を管理するシステム
import java.awt.Image;
import java.io.IOException;
import java.util.List;

import io.numberrun.Component.Sprite;
import io.numberrun.Component.Transform;
import io.numberrun.Core.SoundManager;
import io.numberrun.Game.Effect.DamageEffectSystem;
import io.numberrun.Game.Effect.PowerUpEffectSystem;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.Game.Wall.WallType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerPassWallSystem implements GameSystem {

    private final int windowWidth;
    private final int windowHeight;

    // 群衆
    private final float unitSpacing = 5.0f;
    private final float maxRadius;

    // 群衆のスプライト画像
    private Image spriteImage;

    private void loadSpriteImage() {
        try {
            this.spriteImage = javax.imageio.ImageIO.read(PlayerView.class.getResource("/images/runner_sprite.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite image");
        }
    }

    public PlayerPassWallSystem(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.maxRadius = windowWidth / 6.0f;
        loadSpriteImage();
    }

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {

        // 壁を通過したかの判定 + 通過した際の効果を適用する
        // 1. world から PlayerState, LaneTransform を持つエンティティを取得する
        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) {
            return;
        }
        Entity player = players.get(0);
        PlayerState playerState = player.getComponent(PlayerState.class).get();
        LaneTransform playerLT = player.getComponent(LaneTransform.class).get();

        // 2. PlayerView の位置情報を取得する
        float playerX = playerLT.getLaneX();
        float playerY = playerLT.getLaneY();

        int previousNumber = playerState.getNumber();

        // 3. world から Wall, LaneTransform を持つエンティティを取得する
        List<Entity> walls = world.query(Wall.class, LaneTransform.class);

        for (Entity wallEntity : walls) {
            Wall wall = wallEntity.getComponent(Wall.class).get();
            LaneTransform wallLT = wallEntity.getComponent(LaneTransform.class).get();
            LaneSize wallSize = wallEntity.getComponent(LaneSize.class).get();

            float wallX = wallLT.getLaneX();
            float wallY = wallLT.getLaneY();
            float wallWidth = wallSize.getWidth();
            float wallHeight = wallSize.getHeight();

            // X 座標が wall を通過する位置にいるかどうか
            boolean passX = (playerX + 0.01f >= wallX - wallWidth / 2) && (playerX - 0.01f <= wallX + wallWidth / 2);
            if (!passX) {
                continue;
            }

            // 4. 各壁エンティティについて、PlayerView の位置と比較して通過したか判定する
            float vy = wallEntity.getComponent(LaneVelocity.class)
                    .map(LaneVelocity::getVy)
                    .orElse(0f);

            float prevWallY = wallY - vy * deltaTime;

            boolean crossed;
            if (vy != 0f) {
                // 速度情報がある場合は、前フレーム位置と比較して通過を判定する
                crossed = (prevWallY < playerY) && (wallY >= playerY);
            } else {
                // 速度情報がない場合は雑に「プレイヤーYを超えたら通過」とする
                crossed = (wallY >= playerY);
            }

            if (!crossed) {
                continue;
            }

            // 4.1 通過していたら、Wall の効果を PlayerState に適用する
            applyWallEffect(playerState, wall);
            int newNumber = playerState.getNumber();

            // 値が減少したらダメージエフェクトを出す
            if (newNumber < previousNumber) {
                DamageEffectSystem.spawnDamageEffect(world, windowWidth, windowHeight);
                if (newNumber <= 0) {
                    // ゲームオーバーになった場合
                    SoundManager.play("/sounds/gameover.wav");
                } else {
                    // まだ生きている（ダメージのみ）場合
                    SoundManager.play("/sounds/damage.wav");
                }
            } else if (newNumber > previousNumber) {
                // 値が増加したらパワーアップエフェクトを出す
                PowerUpEffectSystem.spawnPowerUpEffect(world, windowWidth, windowHeight);
                SoundManager.play("/sounds/powerup.wav");
            }

            // 4.2 壁エンティティを world から削除する (無効化する)
            wallEntity.destroy();
            break;

        }

        // 群衆をリスポーン
        if (playerState.getNumber() != previousNumber) {
            despawnChildSprites(player);
            spawnChildSprite(world, player, playerState.getNumber());
        }
    }

    private void applyWallEffect(PlayerState playerState, Wall wall) {
        WallType type = wall.getWallType();
        int value = wall.getValue();
        playerState.setNumber(
                type.getAppliedNumber(playerState.getNumber(), value)
        );
    }

    public float radiusLimit(int count) {
        return Math.min(maxRadius, unitSpacing * (float) (count));
    }

    public void spawnChildSprite(World world, Entity parent, int count) {

        // めっちゃ多い場合はどうせ数えられないし重くなるだけなのでキャップする
        if (count > 100) {
            count = 100;
        }

        for (int i = 0; i < count; i++) {
            float angle = (float) Math.random() * 2.0f * (float) Math.PI;
            float radius = radiusLimit(count) * (float) (Math.random());
            float offsetX = radius * (float) Math.cos(angle);
            float offsetY = radius * (float) Math.sin(angle); // マイナスほど下に行く

            parent.addChild(
                    world.spawn(
                            // Y が下に行くほど上に表示したいが、親よりは下に表示したい
                            // つまり、YがマイナスなほどZOrderを大きくする
                            new Sprite(
                                    spriteImage,
                                    96, 160,
                                    16.0f
                            ).withZOrder(-offsetY - 10000.0f),
                            new Transform(offsetX, -offsetY)
                    )
            );
        }
    }

    public void despawnChildSprites(Entity parent) {
        for (Entity child : parent.getChildren()) {
            if (child.hasComponent(Sprite.class)) {
                child.destroy();
            }
        }
    }
}
