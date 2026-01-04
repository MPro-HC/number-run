# TODO List

## Level
- [ ] レーンに壁などのオブジェクトを生成する処理と World に spawn する処理 ([app/src/main/java/io/numberrun/Game/Level/LavelSystem.java](app/src/main/java/io/numberrun/Game/Level/LavelSystem.java#L8))

## Player
- [ ] プレイヤーが壁を通過したときの処理を実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerPassWallSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerPassWallSystem.java#L18))
- [ ] Sample.java の PlayerMovementSystem を参考にプレイヤーの動きを実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerMovementSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerMovementSystem.java#L20))
- [ ] 更新処理を実装する ([app/src/main/java/io/numberrun/Game/Player/PlayerViewSyncSystem.java](app/src/main/java/io/numberrun/Game/Player/PlayerViewSyncSystem.java#L17))
- [ ] 現在の数値に value を加算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L16))
- [ ] 現在の数値に value を乗算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L21))
- [ ] 現在の数値を value で割算して更新し、更新後の数値を返す ([app/src/main/java/io/numberrun/Game/Player/PlayerState.java](app/src/main/java/io/numberrun/Game/Player/PlayerState.java#L26))

## Lane
- [ ] LaneVelocity に基づいてプレイヤーの位置を更新する (MovementSystem.java 参照) ([app/src/main/java/io/numberrun/Game/Lane/LaneMovementSystem.java](app/src/main/java/io/numberrun/Game/Lane/LaneMovementSystem.java#L16))

## Game System
- [ ] プレイヤーの状態を確認して、ゲームオーバーにする処理を実装する ([app/src/main/java/io/numberrun/Game/GameOverSystem.java](app/src/main/java/io/numberrun/Game/GameOverSystem.java#L17))
