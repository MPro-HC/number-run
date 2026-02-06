# Number Run クラス図

## アーキテクチャ概要

- **Entity**: コンポーネントのコンテナ
- **Component**: データのみを保持（ロジックなし）
- **GameSystem**: ゲームロジックを実装
- **World**: EntityとGameSystemを管理

## ダイアグラム一覧

巨大すぎるので三つに分けています。

### 📦 [Component Diagram](./COMPONENT_DIAGRAM.md)

Entity、Component、Renderableインターフェースと、すべてのコンポーネント実装を含みます。

**含まれる内容:**
- Entity（コンポーネントのコンテナ）
- Component基底インターフェース
- Renderable & Buttonインターフェース
- 基本コンポーネント（Transform, Velocity, Timer, NamedValue）
- 描画コンポーネント（Image, Text, Rectangle, Circle, Sprite, etc.）
- ゲーム固有コンポーネント（Player, Wall, Lane, Scene, Effect, etc.）

### ⚙️ [System Diagram](./SYSTEM_DIAGRAM.md)

World、GameSystemインターフェースと、すべてのシステム実装を含みます。

**含まれる内容:**
- World（中央管理システム）
- GameSystem基底インターフェース
- SystemPriority（実行優先度）
- 入力関連（InputEvent, InputState, InputType）
- 各種ゲームシステム（Movement, Player, Level, Lane, UI, Scene, Effect, etc.）

### 🎮 [Engine Diagram](./ENGINE_DIAGRAM.md)

App、GameEngine、SoundManagerと、ゲームループの構造を含みます。

**含まれる内容:**
- App（エントリーポイント）
- GameEngine（ゲームループ & ウィンドウ管理）
- SoundManager（サウンド再生）
- Graphics（描画ユーティリティ）
- ゲームループとの関係図


## パッケージ構成

```
io.numberrun
├── Core/           # GameEngine, SoundManager
├── System/         # World, Entity, GameSystem, SystemPriority
├── Component/      # Component, Renderable, Button & 基本コンポーネント
├── UI/             # InputEvent, InputState, InputType, Graphics, ButtonClickSystem
└── Game/           # ゲーム固有のSystem & Component
    ├── Scene/       # Scene, SceneState, SceneType
    ├── Player/      # PlayerState, PlayerView, PlayerMovementSystem, PlayerPassWallSystem, PlayerViewSyncSystem
    ├── Level/       # Level, LevelSystem
    ├── Lane/        # Lane関連Component & System
    ├── Wall/        # Wall, WallType, WallView
    ├── Grid/        # GridLine, GridLineSpawnSystem
    ├── Cursor/      # CursorView, CursorSystem
    ├── GlobalCursor/ # GlobalCursorModel, GlobalCursorSystem
    ├── Obstacle/    # Obstacle, ObstacleWobble & 関連System
    ├── Effect/      # DamageEffect, PowerUpEffect, Easing & 関連System
    ├── Animation/   # SpriteAnimationSystem
    ├── Title/       # TitleOverlay, TitleSystem, TitleExitSystem
    └── GameOver/    # GameOverOverlay, GameOverAd & 関連System
```
