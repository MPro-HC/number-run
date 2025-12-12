package io.numberrun.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 * Java2D Graphics2D の薄いラッパー 描画操作をシンプルに提供する
 */
public class Graphics {

    private final Graphics2D g2d;
    private final AffineTransform originalTransform;

    public Graphics(Graphics2D g2d) {
        this.g2d = g2d;
        this.originalTransform = g2d.getTransform();

        // アンチエイリアシングを有効化
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * 塗りつぶし矩形を描画
     */
    public void fillRect(float x, float y, float width, float height, Color color) {
        g2d.setColor(color);
        g2d.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * 矩形の枠線を描画
     */
    public void drawRect(float x, float y, float width, float height, Color color) {
        g2d.setColor(color);
        g2d.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * 塗りつぶし円を描画
     */
    public void fillOval(float x, float y, float width, float height, Color color) {
        g2d.setColor(color);
        g2d.fillOval((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * 円の枠線を描画
     */
    public void drawOval(float x, float y, float width, float height, Color color) {
        g2d.setColor(color);
        g2d.drawOval((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * 線を描画
     */
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        g2d.setColor(color);
        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    /**
     * テキストを描画
     */
    public void drawText(String text, float x, float y, Color color) {
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    /**
     * テキストを描画（フォント指定）
     */
    public void drawText(String text, float x, float y, Color color, Font font) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    /**
     * 変換を適用（移動、回転、スケール）
     */
    public void transform(float x, float y, float rotation, float scaleX, float scaleY) {
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(rotation));
        g2d.scale(scaleX, scaleY);
    }

    /**
     * 変換をリセット
     */
    public void resetTransform() {
        g2d.setTransform(originalTransform);
    }

    /**
     * 現在の変換を保存
     */
    public AffineTransform saveTransform() {
        return g2d.getTransform();
    }

    /**
     * 変換を復元
     */
    public void restoreTransform(AffineTransform transform) {
        g2d.setTransform(transform);
    }

    /**
     * 画面をクリア
     */
    public void clear(Color color) {
        g2d.setColor(color);
        g2d.fillRect(0, 0, 10000, 10000); // 十分大きな領域をクリア
    }

    /**
     * 内部のGraphics2Dを取得（高度な描画用）
     */
    public Graphics2D getGraphics2D() {
        return g2d;
    }
}
