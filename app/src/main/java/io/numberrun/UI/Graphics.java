package io.numberrun.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
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
     * 塗りつぶしの多角形を描画
     */
    public void fillPolygon(int[] xPoints, int[] yPoints, Color color) {
        int numXPoints = xPoints.length;
        int numYPoints = yPoints.length;
        if (numXPoints != numYPoints) {
            throw new IllegalArgumentException("Number of points does not match nPoints");
        }

        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, numXPoints);
    }

    /**
     * グラデーション付きの多角形。 多分レーンの背景でしか使わないからこれだけ実装にしとく
     */
    public void fillPolygonGradientVertical(int[] xPoints, int[] yPoints, Color colorTop, Color colorBottom) {
        int numXPoints = xPoints.length;
        int numYPoints = yPoints.length;

        if (numXPoints != numYPoints) {
            throw new IllegalArgumentException("Number of points does not match nPoints");
        }

        // グラデーションペイントの作成
        GradientPaint gradientPaint = new GradientPaint(
                0, yPoints[0], colorTop,
                0, yPoints[0] + (yPoints[numYPoints - 1] - yPoints[0]), colorBottom
        );

        // 現在のペイントを保存
        Paint originalPaint = g2d.getPaint();

        // グラデーションペイントを設定
        g2d.setPaint(gradientPaint);

        // 多角形を塗りつぶし
        g2d.fillPolygon(xPoints, yPoints, numXPoints);

        // 元のペイントに戻す
        g2d.setPaint(originalPaint);
    }

    /**
     * 多角形を描画
     */
    public void drawPolygon(int[] xPoints, int[] yPoints, Color color) {
        int numXPoints = xPoints.length;
        int numYPoints = yPoints.length;
        if (numXPoints != numYPoints) {
            throw new IllegalArgumentException("Number of points does not match nPoints");
        }

        g2d.setColor(color);
        g2d.drawPolygon(xPoints, yPoints, numXPoints);
    }

    /**
     * 線を描画
     */
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        g2d.setColor(color);
        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    /**
     * 太さ指定して線を描画
     */
    public void drawLine(float x1, float y1, float x2, float y2, float width, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(width));
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
     * テキストを中央揃えで描画
     */
    public void drawTextCentered(String text, float x, float y, Color color, Font font) {
        g2d.setFont(font);
        g2d.setColor(color);
        FontMetrics fm = g2d.getFontMetrics();
        float textWidth = fm.stringWidth(text);
        float textHeight = fm.getAscent();
        // テキストの中心が (x, y) になるように描画
        g2d.drawString(text, x - textWidth / 2, y + textHeight / 2 - fm.getDescent() / 2);
    }

    /**
     * 雑にボーダーありのテキスト
     */
    public void drawTextCentered(String text, float x, float y, Color color, Font font, Color borderColor, float borderWidth) {
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        float textWidth = fm.stringWidth(text);
        float textHeight = fm.getAscent();

        // 縁取りのためにちょっとずつずらして描画
        g2d.setColor(borderColor);
        float offset = borderWidth / 2;
        g2d.drawString(
                text,
                x - textWidth / 2 - offset,
                y + textHeight / 2 - fm.getDescent() / 2
        );
        g2d.drawString(
                text,
                x - textWidth / 2 + offset,
                y + textHeight / 2 - fm.getDescent() / 2
        );
        g2d.drawString(
                text,
                x - textWidth / 2,
                y + textHeight / 2 - fm.getDescent() / 2 - offset
        );
        g2d.drawString(
                text,
                x - textWidth / 2,
                y + textHeight / 2 - fm.getDescent() / 2 + offset
        );

        // 本体
        g2d.setColor(color);
        g2d.drawString(
                text,
                x - textWidth / 2,
                y + textHeight / 2 - fm.getDescent() / 2
        );

    }

    /**
     * 画像を描画
     */
    public void drawImage(java.awt.Image image, float x, float y, float width, float height) {
        g2d.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * XYの移動
     */
    public void translate(float x, float y) {
        g2d.translate(x, y);
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
     * X軸周りの擬似3D回転（上下に倒れる効果）
     */
    public void rotateX(float angle) {
        double scale = Math.cos(Math.toRadians(angle));
        g2d.scale(1, scale);
    }

    /**
     * Y軸周りの擬似3D回転（左右に倒れる効果）
     */
    public void rotateY(float angle) {
        double scale = Math.cos(Math.toRadians(angle));
        g2d.scale(scale, 1);
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
