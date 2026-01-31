package io.numberrun.Component;

import java.io.IOException;
import java.net.URL;

import io.numberrun.UI.Graphics;

public class Sprite implements Renderable {

    private float zOrder;

    private final java.awt.Image image;
    // 描画するときの幅と高さ
    private final float width;
    private final float height;

    // 画像の元の幅と高さ
    private final float imageWidth;
    private final float imageHeight;

    // 4x4 グリッド
    private final int COLS = 4;
    private final int ROWS = 4;

    // 現在のフレーム
    private int currentFrame = 0;

    // アニメーション用
    private final float fps;
    private final float frameDuration;
    private float frameTimer;

    public Sprite(java.awt.Image image) {
        this.image = image;
        this.width = (float) image.getWidth(null) / COLS;
        this.height = (float) image.getHeight(null) / ROWS;
        this.imageWidth = image.getWidth(null);
        this.imageHeight = image.getHeight(null);
        this.fps = 24.0f; // デフォルトFPS
        this.frameDuration = 1000.0f / fps;
        this.frameTimer = 0.0f;
    }

    public Sprite(java.awt.Image image, float width, float height, float fps) {
        this.image = image;
        this.width = width;
        this.height = height;

        this.imageWidth = image.getWidth(null);
        this.imageHeight = image.getHeight(null);

        this.fps = fps;
        this.frameTimer = 0.0f;
        this.frameDuration = 1000.0f / fps;
    }

    public Sprite(URL imageUrl, float fps) {
        try {
            this.image = javax.imageio.ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageUrl, e);
        }
        this.width = (float) image.getWidth(null) / COLS;
        this.height = (float) image.getHeight(null) / ROWS;
        this.imageWidth = image.getWidth(null);
        this.imageHeight = image.getHeight(null);
        this.fps = fps;
        this.frameTimer = 0.0f;
        this.frameDuration = 1000.0f / fps;
    }

    public Sprite(URL imageUrl, float width, float height, float fps) {
        try {
            this.image = javax.imageio.ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageUrl, e);
        }
        this.width = width;
        this.height = height;
        this.imageWidth = image.getWidth(null);
        this.imageHeight = image.getHeight(null);
        this.fps = fps;
        this.frameTimer = 0.0f;
        this.frameDuration = 1000.0f / fps;
    }

    // 画像のどの部分から撮ってくるかの座標
    private int frameX(int index) {
        return (index % COLS) * (int) (imageWidth / COLS);
    }

    private int frameY(int index) {
        return (index / COLS) * (int) (imageHeight / ROWS);
    }

    private int frameWidth() {
        return (int) (imageWidth / COLS);
    }

    private int frameHeight() {
        return (int) (imageHeight / ROWS);
    }

    @Override
    public void render(Graphics g) {
        float sourceX = frameX(currentFrame);
        float sourceY = frameY(currentFrame);
        float sourceWidth = frameWidth();
        float sourceHeight = frameHeight();

        g.drawImageArea(
                image,
                // destination
                -width / 2, -height / 2, width, height,
                // source
                sourceX, sourceY, sourceWidth, sourceHeight
        );
    }

    @Override
    public float getZOrder() {
        return zOrder;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

    public void setCurrentFrame(int frame) {
        this.currentFrame = frame % (COLS * ROWS);
    }

    public void setNextFrame() {
        this.currentFrame = (this.currentFrame + 1) % (COLS * ROWS);
    }

    public void tick(
            float deltaTime // ms
    ) {
        frameTimer += deltaTime;
        while (frameTimer >= frameDuration) {
            setNextFrame();
            frameTimer -= frameDuration;
        }
    }

}
