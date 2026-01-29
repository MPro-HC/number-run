package io.numberrun.Game.Player;

// プレイヤーを表示するビュー
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;

import io.numberrun.Component.Oval;
import io.numberrun.Component.Renderable;
import io.numberrun.Component.Text;
import io.numberrun.UI.Graphics;

public class PlayerView implements Renderable {

    private final Text playerNumberText;
    private final Oval shadowOval;
    private final Color textColor = Color.white;
    private final Font textFont = new Font("Arial", Font.BOLD, 64);
    private final float textBorderWidth = 5.0f;
    private final Color textBorderColor = Color.black;
    private float scale = 1.0f; // サイズのスケール

    public PlayerView(int playerNumber) {
        this.playerNumberText = new Text(String.valueOf(playerNumber), textColor, textFont, 0, textBorderColor, textBorderWidth);
        this.shadowOval = new Oval(40, 20, new Color(0x409bc0f9, true), true); // #9bc0f9
    }

    public PlayerView() {
        this.playerNumberText = new Text("1", textColor, textFont, 0, textBorderColor, textBorderWidth);
        this.shadowOval = new Oval(40, 20, new Color(0x409bc0f9, true), true); // #9bc0f9
    }

    @Override
    public void render(Graphics g) {
        AffineTransform transform = g.saveTransform();
        // 影は少し下に描画
        g.translate(
                0, // X は動かさない
                20 // Y 
        );
        shadowOval.render(g);
        g.restoreTransform(transform);
        playerNumberText.render(g);
    }

    @Override
    public int getZOrder() {
        return playerNumberText.getZOrder();
    }

    @Override
    public void setZOrder(int zOrder) {
        // pass
    }

    @Override
    public float getWidth() {
        return playerNumberText.getWidth();
    }

    @Override
    public float getHeight() {
        return playerNumberText.getHeight();
    }

    public void setPlayerNumber(int number) {
        this.playerNumberText.setText(
                String.valueOf(number)
        );
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }
}
