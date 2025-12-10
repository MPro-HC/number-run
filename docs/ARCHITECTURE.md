# Number Run ゲームエンジン アーキテクチャガイド

このドキュメントでは、Number Run ゲームエンジンで使用されているコンポーネントとシステムの作成方法について説明します。
このエンジンは **ECS (Entity-Component-System)** アーキテクチャを採用しており、**MVC (Model-View-Controller)** パターンの概念とも対応しています。

## 目次

1. [アーキテクチャ概要](#アーキテクチャ概要)
2. [MVCとの対応関係](#mvcとの対応関係)
3. [コンポーネント (Component)](#コンポーネント-component)
4. [システム (System)](#システム-system)
5. [エンティティ (Entity)](#エンティティ-entity)
6. [実装例](#実装例)

---

## アーキテクチャ概要

```
┌──────────────────────────────────────────────────────────────────┐
│                         GameEngine                               │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │                          World                              │  │
│  │  ┌─────────────────────────────────────────────────────┐   │  │
│  │  │                     Entities                         │   │  │
│  │  │  ┌─────────┐  ┌─────────┐  ┌─────────┐              │   │  │
│  │  │  │ Entity  │  │ Entity  │  │ Entity  │   ...        │   │  │
│  │  │  │┌───────┐│  │┌───────┐│  │┌───────┐│              │   │  │
│  │  │  ││ Comp. ││  ││ Comp. ││  ││ Comp. ││              │   │  │
│  │  │  │└───────┘│  │└───────┘│  │└───────┘│              │   │  │
│  │  │  └─────────┘  └─────────┘  └─────────┘              │   │  │
│  │  └─────────────────────────────────────────────────────┘   │  │
│  │                                                             │  │
│  │  ┌─────────────────────────────────────────────────────┐   │  │
│  │  │                     Systems                          │   │  │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────┐           │   │  │
│  │  │  │ System 1 │  │ System 2 │  │ System 3 │  ...      │   │  │
│  │  │  └──────────┘  └──────────┘  └──────────┘           │   │  │
│  │  └─────────────────────────────────────────────────────┘   │  │
│  └────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────┘
```

### 基本的な流れ

1. **Entity** (エンティティ) はゲーム内のオブジェクトを表す
2. **Component** (コンポーネント) はエンティティが持つデータを表す
3. **System** (システム) はコンポーネントを操作するロジックを持つ
4. **World** が全てのエンティティとシステムを管理する

---

## MVCとの対応関係

| ECS 概念 | MVC 概念 | 役割 |
|----------|----------|------|
| **Component** | **Model** | データを保持する。ロジックを持たない純粋なデータ構造 |
| **Renderable Component** | **View** | 画面への描画を担当する |
| **System** | **Controller** | ロジックを担当し、Modelを操作してViewに反映させる |

### 具体例

```
┌─────────────────────────────────────────────────────────────┐
│                    プレイヤー移動の例                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Model (データ)                                              │
│  ├── Transform: 位置情報 (x, y, rotation, scale)            │
│  └── Velocity: 速度情報 (vx, vy)                            │
│                                                             │
│  View (描画)                                                 │
│  └── Rectangle: 四角形の描画                                 │
│                                                             │
│  Controller (ロジック)                                       │
│  ├── PlayerMovementSystem: キー入力を速度に変換              │
│  └── MovementSystem: 速度を位置に反映                        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## コンポーネント (Component)

コンポーネントは **データのみ** を保持し、ロジックを持ちません。
MVCの **Model** に相当します。

### 基底インターフェース

```java
package io.numberrun.Component;

/**
 * ECS のコンポーネント基底インターフェース
 * コンポーネントはデータのみを保持し、ロジックを持たない
 */
public interface Component {
}
```

### コンポーネントの種類

#### 1. データコンポーネント (Model)

純粋なデータを保持するコンポーネントです。

**例: Transform (位置情報)**

```java
package io.numberrun.Component;

public class Transform implements Component {
    private float x;
    private float y;
    private float rotation;
    private float scaleX;
    private float scaleY;

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;
    }

    // getter/setter メソッド
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    // ... 他のgetter/setter
}
```

**例: Velocity (速度)**

```java
package io.numberrun.Component;

public class Velocity implements Component {
    private float vx;
    private float vy;

    public Velocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    // getter/setter メソッド
    public float getVx() { return vx; }
    public void setVelocity(float vx, float vy) { 
        this.vx = vx;
        this.vy = vy;
    }
    // ...
}
```

#### 2. 描画可能コンポーネント (View)

`Renderable` インターフェースを実装することで、画面に描画できるようになります。

**Renderable インターフェース:**

```java
package io.numberrun.Component;

import io.numberrun.UI.Graphics;

public interface Renderable extends Component {
    /**
     * 描画処理
     * @param g グラフィックスラッパー
     */
    void render(Graphics g);

    /**
     * 描画順序（Z-order）。小さい値が先に描画される
     */
    default int getZOrder() {
        return 0;
    }
}
```

**例: Rectangle (矩形描画)**

```java
package io.numberrun.Component;

import java.awt.Color;
import io.numberrun.UI.Graphics;

public class Rectangle implements Renderable {
    private float width;
    private float height;
    private Color color;
    private boolean filled;
    private int zOrder;

    public Rectangle(float width, float height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.filled = true;
        this.zOrder = 0;
    }

    @Override
    public void render(Graphics g) {
        if (filled) {
            g.fillRect(-width / 2, -height / 2, width, height, color);
        } else {
            g.drawRect(-width / 2, -height / 2, width, height, color);
        }
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }

    // getter/setter...
}
```

### カスタムコンポーネントの作成方法

#### ステップ1: データコンポーネント (Model) を作成

```java
package io.numberrun.Component;

/**
 * 体力を管理するコンポーネント
 * MVC の Model に相当
 */
public class Health implements Component {
    private int maxHealth;
    private int currentHealth;

    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    
    public void damage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }
    
    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }
    
    public boolean isDead() {
        return currentHealth <= 0;
    }
}
```

#### ステップ2: 描画コンポーネント (View) を作成

```java
package io.numberrun.Component;

import java.awt.Color;
import io.numberrun.UI.Graphics;

/**
 * 体力バーを描画するコンポーネント
 * MVC の View に相当
 */
public class HealthBar implements Renderable {
    private float width;
    private float height;
    private Color backgroundColor;
    private Color healthColor;
    private int zOrder;

    public HealthBar(float width, float height) {
        this.width = width;
        this.height = height;
        this.backgroundColor = Color.DARK_GRAY;
        this.healthColor = Color.GREEN;
        this.zOrder = 100; // 他のものより前面に描画
    }

    @Override
    public void render(Graphics g) {
        // 背景バー
        g.fillRect(-width / 2, -height / 2, width, height, backgroundColor);
        // 体力バー (実際の体力割合は System で設定)
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }
}
```

---

## システム (System)

システムは **ゲームロジック** を担当します。
MVCの **Controller** に相当します。

### 基底インターフェース

```java
package io.numberrun.System;

import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

public interface GameSystem {
    /**
     * システムの優先度（小さいほど先に実行）
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 毎フレーム呼ばれる更新処理
     */
    default void update(World world, float deltaTime) {
    }

    /**
     * 入力イベントを処理
     */
    default void onInput(World world, InputEvent event, InputState inputState) {
    }

    /**
     * システム初期化時に呼ばれる
     */
    default void onStart(World world) {
    }

    /**
     * システム終了時に呼ばれる
     */
    default void onStop(World world) {
    }
}
```

### システムの優先度

```java
package io.numberrun.System;

public enum SystemPriority {
    HEIGHEST(0),    // 最優先
    HIGH(1),        // 高優先度
    DEFAULT(50),    // デフォルト
    LOW(100),       // 低優先度
    VERY_LOW(1000); // 最後に実行

    private final int priority;
    // ...
}
```

### システムの種類

#### 1. 更新系システム

毎フレーム `update()` メソッドで処理を行います。

**例: MovementSystem (移動処理)**

```java
package io.numberrun.Game;

import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.System.*;

/**
 * 速度に基づいてエンティティを移動するシステム
 * MVC の Controller に相当
 */
public class MovementSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority(); // 他のシステムより後に実行
    }

    @Override
    public void update(World world, float deltaTime) {
        // Transform と Velocity を持つエンティティを検索
        for (Entity entity : world.query(Transform.class, Velocity.class)) {
            Transform transform = entity.getComponent(Transform.class).get();
            Velocity velocity = entity.getComponent(Velocity.class).get();

            // 速度を位置に反映
            transform.setX(transform.getX() + velocity.getVx() * deltaTime);
            transform.setY(transform.getY() + velocity.getVy() * deltaTime);
        }
    }
}
```

#### 2. 入力系システム

`onInput()` メソッドでユーザー入力に反応します。

**例: PlayerMovementSystem (プレイヤー操作)**

```java
package io.numberrun.Game;

import java.awt.event.KeyEvent;
import io.numberrun.Component.Velocity;
import io.numberrun.System.*;
import io.numberrun.UI.*;

/**
 * プレイヤー操作システム
 * MVC の Controller に相当
 */
public class PlayerMovementSystem implements GameSystem {

    private static final float SPEED = 400f;

    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        for (Entity entity : world.query(Transform.class, Velocity.class)) {
            entity.getComponent(Velocity.class).ifPresent(velocity -> {
                float vx = 0;
                float vy = 0;

                // WASDキーまたは矢印キーで移動
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
```

#### 3. 複合システム

複数のコンポーネントを連携させるシステム。

**例: GlobalCursorSystem + CursorSystem (カーソル追跡)**

```
┌─────────────────────────────────────────────────────────────┐
│                    カーソル追跡の仕組み                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. GlobalCursorSystem (優先度: HIGH)                       │
│     - グローバルなマウス位置を取得                           │
│     - GlobalCursorModel に位置を保存                         │
│                                                             │
│  2. CursorSystem (優先度: LOW)                              │
│     - GlobalCursorModel から位置を読み取る                   │
│     - Transform の位置を更新                                 │
│                                                             │
│  3. 描画システム (エンジン内蔵)                              │
│     - Transform の位置に CursorView を描画                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### カスタムシステムの作成方法

```java
package io.numberrun.Game;

import io.numberrun.Component.Health;
import io.numberrun.System.*;

/**
 * 死亡したエンティティを処理するシステム
 * MVC の Controller に相当
 */
public class DeathSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.VERY_LOW.getPriority(); // 最後に実行
    }

    @Override
    public void update(World world, float deltaTime) {
        for (Entity entity : world.query(Health.class)) {
            Health health = entity.getComponent(Health.class).get();
            
            if (health.isDead()) {
                // エンティティを破棄
                entity.destroy();
            }
        }
    }
}
```

---

## エンティティ (Entity)

エンティティはコンポーネントのコンテナとして機能します。
ゲーム内の全てのオブジェクトはエンティティとして表現されます。

### エンティティの生成

```java
// World.spawn() を使用してエンティティを生成
Entity player = world.spawn(
    new Transform(400, 300),    // 位置
    new Velocity(0, 0),         // 速度
    new Rectangle(50, 50, Color.CYAN)  // 描画
);
```

### エンティティの検索

```java
// 特定のコンポーネントを持つエンティティを検索
List<Entity> movables = world.query(Transform.class, Velocity.class);

// 複数のコンポーネントで検索
List<Entity> players = world.query(Transform.class, Velocity.class, PlayerTag.class);
```

### コンポーネントの操作

```java
// コンポーネントの取得
Optional<Transform> transform = entity.getComponent(Transform.class);

// コンポーネントが存在する場合のみ処理
entity.getComponent(Velocity.class).ifPresent(velocity -> {
    velocity.setVelocity(100, 0);
});

// コンポーネントの追加
entity.addComponent(new Health(100));

// エンティティの破棄
entity.destroy();
```

---

## 実装例

### 完全な機能の実装例: 体力システム

以下は、体力システムを実装する完全な例です。

#### 1. Model (データコンポーネント)

```java
// Health.java
package io.numberrun.Component;

public class Health implements Component {
    private int maxHealth;
    private int currentHealth;

    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public float getHealthRatio() { return (float) currentHealth / maxHealth; }
    public boolean isDead() { return currentHealth <= 0; }
    
    public void damage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }
}
```

#### 2. View (描画コンポーネント)

```java
// HealthBar.java
package io.numberrun.Component;

import java.awt.Color;
import io.numberrun.UI.Graphics;

public class HealthBar implements Renderable {
    private float width;
    private float height;
    private float healthRatio = 1.0f;

    public HealthBar(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setHealthRatio(float ratio) {
        this.healthRatio = ratio;
    }

    @Override
    public void render(Graphics g) {
        // 背景 (グレー)
        g.fillRect(-width / 2, -height / 2, width, height, Color.DARK_GRAY);
        // 体力 (緑→黄→赤)
        Color healthColor = healthRatio > 0.5f ? Color.GREEN : 
                           healthRatio > 0.25f ? Color.YELLOW : Color.RED;
        g.fillRect(-width / 2, -height / 2, width * healthRatio, height, healthColor);
    }

    @Override
    public int getZOrder() {
        return 100;
    }
}
```

#### 3. Controller (システム)

```java
// HealthDisplaySystem.java
package io.numberrun.Game;

import io.numberrun.Component.*;
import io.numberrun.System.*;

public class HealthDisplaySystem implements GameSystem {

    @Override
    public void update(World world, float deltaTime) {
        for (Entity entity : world.query(Health.class, HealthBar.class)) {
            Health health = entity.getComponent(Health.class).get();
            HealthBar healthBar = entity.getComponent(HealthBar.class).get();
            
            // Model の値を View に反映
            healthBar.setHealthRatio(health.getHealthRatio());
        }
    }
}
```

#### 4. 使用方法

```java
// App.java での使用例
public static void main(String[] args) {
    GameEngine engine = new GameEngine("Game", 800, 600);
    World world = engine.getWorld();

    // プレイヤーエンティティ
    world.spawn(
        new Transform(400, 300),
        new Health(100),
        new HealthBar(60, 8),
        new Rectangle(50, 50, Color.CYAN)
    );

    // システムの追加
    world.addSystems(
        new HealthDisplaySystem(),
        new DeathSystem()
    );

    engine.start();
}
```

---

## ディレクトリ構造のベストプラクティス

```
src/main/java/io/numberrun/
├── Component/           # 共通コンポーネント
│   ├── Component.java
│   ├── Renderable.java
│   ├── Transform.java
│   ├── Velocity.java
│   ├── Rectangle.java
│   └── Text.java
├── System/              # コアシステム
│   ├── Entity.java
│   ├── GameSystem.java
│   ├── SystemPriority.java
│   └── World.java
├── Core/                # エンジンコア
│   └── GameEngine.java
├── UI/                  # UI関連
│   ├── Graphics.java
│   ├── InputEvent.java
│   ├── InputState.java
│   └── InputType.java
└── Game/                # ゲーム固有の実装
    ├── MovementSystem.java
    ├── Cursor/          # 機能ごとにグループ化
    │   ├── CursorSystem.java
    │   └── CursorView.java
    └── GlobalCursor/
        ├── GlobalCursorModel.java
        └── GlobalCursorSystem.java
```

---

## まとめ

| 要素 | 役割 | MVC | 実装のポイント |
|------|------|-----|---------------|
| **Component** | データを保持 | Model | ロジックを持たない純粋なデータ |
| **Renderable** | 描画を担当 | View | `render()` メソッドで描画 |
| **GameSystem** | ロジックを実行 | Controller | `update()` や `onInput()` で処理 |
| **Entity** | コンポーネントの集合 | - | `world.spawn()` で生成 |
| **World** | 全体を管理 | - | エンティティとシステムのコンテナ |

この設計により、データ・表示・ロジックが明確に分離され、保守性と拡張性の高いゲームが構築できます。
