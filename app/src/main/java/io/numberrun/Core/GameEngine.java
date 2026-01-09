package io.numberrun.Core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import io.numberrun.System.World;
import io.numberrun.UI.Graphics;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;
import io.numberrun.UI.InputType;

/**
 * ゲームエンジンのメインクラス Swingをラップしてゲームループを提供する
 */
public class GameEngine {

    private final String title;
    private final int width;
    private final int height;
    private final World world;
    private final InputState inputState;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;

    private boolean running = false;
    private int targetFps = 60;
    private Color backgroundColor = Color.BLACK;

    public GameEngine(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.world = new World();
        this.inputState = new InputState();
    }

    /**
     * ゲームワールドを取得
     */
    public World getWorld() {
        return world;
    }

    /**
     * 入力状態を取得
     */
    public InputState getInputState() {
        return inputState;
    }

    /**
     * ターゲットFPSを設定
     */
    public void setTargetFps(int fps) {
        this.targetFps = fps;
    }

    /**
     * 背景色を設定
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * ゲームを開始
     */
    public void start() {
        initializeWindow();
        setupInputHandlers();

        running = true;
        world.start();

        gameLoop();
    }

    /**
     * ゲームを停止
     */
    public void stop() {
        running = false;
        world.stop();
    }

    private void initializeWindow() {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(true);

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        this.world.setGlobalFrame(frame);
    }

    private void setupInputHandlers() {
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                inputState.keyPressed(e.getKeyCode());
                InputEvent event = InputEvent.keyEvent(InputType.KEY_PRESSED, e.getKeyCode(), e.getKeyChar());
                world.handleInput(event, inputState);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputState.keyReleased(e.getKeyCode());
                InputEvent event = InputEvent.keyEvent(InputType.KEY_RELEASED, e.getKeyCode(), e.getKeyChar());
                world.handleInput(event, inputState);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                InputEvent event = InputEvent.keyEvent(InputType.KEY_TYPED, e.getKeyCode(), e.getKeyChar());
                world.handleInput(event, inputState);
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                inputState.mousePressed(e.getButton());
                InputEvent event = InputEvent.mouseEvent(InputType.MOUSE_PRESSED, x, y, e.getButton());
                world.handleInput(event, inputState);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                inputState.mouseReleased(e.getButton());
                InputEvent event = InputEvent.mouseEvent(InputType.MOUSE_RELEASED, x, y, e.getButton());
                world.handleInput(event, inputState);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                InputEvent event = InputEvent.mouseEvent(InputType.MOUSE_CLICKED, x, y, e.getButton());
                world.handleInput(event, inputState);
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                inputState.setMousePosition(x, y);
                InputEvent event = InputEvent.mouseEvent(InputType.MOUSE_MOVED, x, y, 0);
                world.handleInput(event, inputState);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                inputState.setMousePosition(x, y);
                InputEvent event = InputEvent.mouseEvent(InputType.MOUSE_DRAGGED, x, y, 0);
                world.handleInput(event, inputState);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / targetFps;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;

            if (delta >= 1) {
                float deltaTime = (float) (1.0 / targetFps);
                update(deltaTime);
                render();
                delta--;
            }

            // CPU使用率を下げるために少し待機
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        frame.dispose();
    }

    private void update(float deltaTime) {
        world.update(deltaTime);
    }

    private void render() {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) bufferStrategy.getDrawGraphics();

        try {
            // 背景をクリア
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);

            // ウィンドウの中心を原点にするためにオフセットを適用
            g2d.translate(width / 2.0, height / 2.0);

            // ワールドを描画
            Graphics graphics = new Graphics(g2d);
            world.render(graphics);
        } finally {
            g2d.dispose();
        }

        bufferStrategy.show();
    }

    /**
     * ウィンドウ幅を取得
     */
    public int getWidth() {
        return width;
    }

    /**
     * ウィンドウ高さを取得
     */
    public int getHeight() {
        return height;
    }
}
