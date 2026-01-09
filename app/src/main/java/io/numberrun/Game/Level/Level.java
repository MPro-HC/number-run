package io.numberrun.Game.Level;

import io.numberrun.Component.Component;

/**
 * レベル進行状態を管理するコンポーネント 障害物の生成タイミングや難易度を制御する
 */
public class Level implements Component {

    private float currentDistance = 0;       // プレイヤーの進行距離
    private float spawnDistance = 0;         // 次の障害物生成距離
    private float baseSpawnInterval = 0.8f;  // 基本生成間隔（レーン単位）
    private float scrollSpeed = 0.25f;        // スクロール速度（lane単位/秒）
    private float difficulty = 1.0f;         // 難易度係数（高いほど生成頻度UP）
    private boolean active = true;           // レベルが動作中かどうか

    public Level() {
    }

    public Level(float scrollSpeed, float baseSpawnInterval) {
        this.scrollSpeed = scrollSpeed;
        this.baseSpawnInterval = baseSpawnInterval;
    }

    // === 距離管理 ===
    public float getCurrentDistance() {
        return currentDistance;
    }

    public void addDistance(float delta) {
        this.currentDistance += delta;
    }

    public void setCurrentDistance(float currentDistance) {
        this.currentDistance = currentDistance;
    }

    // === 生成距離管理 ===
    public float getSpawnDistance() {
        return spawnDistance;
    }

    public void setSpawnDistance(float spawnDistance) {
        this.spawnDistance = spawnDistance;
    }

    // === 生成間隔 ===
    public float getBaseSpawnInterval() {
        return baseSpawnInterval;
    }

    public void setBaseSpawnInterval(float baseSpawnInterval) {
        this.baseSpawnInterval = baseSpawnInterval;
    }

    // === スクロール速度 ===
    public float getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    // === 難易度 ===
    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void increaseDifficulty(float amount) {
        this.difficulty += amount;
    }

    // === アクティブ状態 ===
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
