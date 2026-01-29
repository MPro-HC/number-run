package io.numberrun.Game.GameOver;

import io.numberrun.Component.Renderable;
import io.numberrun.UI.Graphics;

public class GameOverOverlay implements Renderable {

    private int zOrder = 100;

    @Override
    public void render(Graphics g) {
        // TODO: View の範囲
        System.out.println("ゲームオーバーを表示中");
    }

    @Override
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    @Override
    public int getZOrder() {
        return zOrder;
    }
}
