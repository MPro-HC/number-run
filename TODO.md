# TODO List

## 実装する機能

- [ ] ゲームオーバー表示
  - [ ] ゲームオーバーになったらオーバーレイを表示して、ゲームオーバーであることを示して、リトライできるようにする
- [ ] レーン上に生成する壁の設計
  - [ ] 生成する壁のランダム性を向上させて、より予測できない形にする
  - [ ] ウェーブを設けて、簡単なパートとノルマのパート(一定の値ないとそこで失敗になるボス的な)のを作る？

## 過去のTODO

### Level
- [x] レーンに壁などのオブジェクトを生成する処理と World に spawn する処理 ([app/src/main/java/io/numberrun/Game/Level/LavelSystem.java](app/src/main/java/io/numberrun/Game/Level/LavelSystem.java#L8))

### Player
- [x] プレイヤーが壁を通過したときの処理を実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerPassWallSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerPassWallSystem.java#L18))
- [x] Sample.java の PlayerMovementSystem を参考にプレイヤーの動きを実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerMovementSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerMovementSystem.java#L20))
- [x] 更新処理を実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerViewSyncSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerViewSyncSystem.java#L17))
- [x] 現在の数値に value を加算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L16))
- [x] 現在の数値に value を乗算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L21))
- [x] 現在の数値を value で割算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L26))

### Lane
- [x] LaneVelocity に基づいてプレイヤーの位置を更新する (MovementSystem.java 参照) ([app/src/main/java/io/numberrun/Game/Lane/LaneMovementSystem.java](app/src/main/java/io/numberrun/Game/Lane/LaneMovementSystem.java#L16))

### Game System
- [x] プレイヤーの状態を確認して、ゲームオーバーにする処理を実装する ([app/src/main/java/io/numberrun/Game/GameOverSystem.java](app/src/main/java/io/numberrun/Game/GameOverSystem.java#L17))

