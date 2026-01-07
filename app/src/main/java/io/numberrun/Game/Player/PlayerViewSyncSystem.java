package io.numberrun.Game.Player;

import java.util.List;

import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;
import io.numberrun.System.Entity;

// PlayerState に応じて PlayerView を更新するシステム
public class PlayerViewSyncSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // TODO: 更新処理を実装する
        // 1. World から Player のエンティティを取得する (PlayerState, PlayerView コンポーネントを持つ)
        List<Entity> players = world.query(PlayerState.class, PlayerView.class);
        if (players.isEmpty()) return;

        for (Entity player : players) {
            // 2. PlayerState から現在の「数値」を取得する
            PlayerState state = player.getComponent(PlayerState.class).get();
            int number = state.getOwnNumber();
            // 3. PlayerView の表示を PlayerState の数値に合わせてセットする
            PlayerView view = player.getComponent(PlayerView.class).get();
            view.setPlayerNumber(number);
        }
    }
}
