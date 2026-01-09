package io.numberrun.Game.Player;

// プレイヤーが壁を通過したときの処理を管理するシステム
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerPassWallSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {

        // TODO: プレイヤーが壁を通過したときの処理を実装する
        // 壁を通過したかの判定 + 通過した際の効果を適用する
        // 1. world から PlayerState, LaneTransform を持つエンティティを取得する
        {
            // 2. PlayerView の位置情報を取得する
            {
                // 3. world から Wall, LaneTransform を持つエンティティを取得する
                {
                    // 4. 各壁エンティティについて、PlayerView の位置と比較して通過したか判定する(ここが面倒かもしれない)
                    {
                        // 4.1 通過していたら、Wall の効果を PlayerState に適用する
                        {
                            // 4.2 壁エンティティを world から削除する (無効化する)
                        }
                    }
                }
            }
        }
    }
}
