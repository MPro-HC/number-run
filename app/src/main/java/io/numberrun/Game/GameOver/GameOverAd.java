package io.numberrun.Game.GameOver;

import io.numberrun.Component.Image;
import io.numberrun.Component.Renderable;
import io.numberrun.UI.Graphics;

public class GameOverAd implements Renderable {

    private final Image adImage;
    private boolean isExiting = false; // 退場中か？

    public GameOverAd(int width, int height) {

        // ランダムに画像を選択する
        // /ads/ad_01.jpg ~ /ads/ad_13.jpg
        int adNumber = (int) (Math.random() * 13) + 1;
        String adImagePath = String.format("/ads/ad_%02d.jpg", adNumber);
        this.adImage = new Image(
                GameOverAd.class.getResource(adImagePath),
                width, height
        );
    }

    @Override
    public void render(Graphics g) {
        adImage.render(g);
    }

    @Override
    public float getZOrder() {
        return adImage.getZOrder();
    }

    @Override
    public void setZOrder(float zOrder) {
        adImage.setZOrder(zOrder);
    }

    @Override
    public float getWidth() {
        return adImage.getWidth();
    }

    @Override
    public float getHeight() {
        return adImage.getHeight();
    }

    public boolean getIsExiting() {
        return isExiting;
    }

    public boolean isExiting() {
        return isExiting;
    }

    public void setExiting(boolean exiting) {
        this.isExiting = exiting;
    }

}
