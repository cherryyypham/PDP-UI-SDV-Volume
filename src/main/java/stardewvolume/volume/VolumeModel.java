package stardewvolume.volume;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Random;

public class VolumeModel {
    private enum GameState { IDLE, PLAYING, SUCCESS }

    private final DoubleProperty cursorPosition;
    private final DoubleProperty targetPosition;
    private final DoubleProperty cursorVelocity;
    private final DoubleProperty successProgress;
    private final ObjectProperty<Double> volumeSet;

    private static final double GRAVITY = 0.0008;
    private static final double CLICK_BOOST = 0.03;
    private static final double MAX_VELOCITY = 0.02;
    private static final double CURSOR_SIZE = 0.1; // Remember to change cursorHeight in UI
    private static final double TARGET_SIZE = 0.02; // Remember to change targetHeight in UI
    private static final double SUCCESS_RATE = 0.003;
    private static final double FAIL_RATE = 0.001;
    private static final double TARGET_MOVE_SPEED = 0.003;
    private static final double TARGET_JUMP_CHANCE = 0.25;
    private static final double FAIL_TIMEOUT = 200.0;

    private GameState state;
    private AnimationTimer gameLoop;
    private final Random random;
    private long lastUpdateTime;
    private int jumpCount;
    private double lastJumpTime;
    private double targetVolFixed;
    private double timeAtZeroProgress;

    public VolumeModel() {
        this.cursorPosition = new SimpleDoubleProperty(0.5);
        this.targetPosition = new SimpleDoubleProperty(0.5);
        this.cursorVelocity = new SimpleDoubleProperty(0.0);
        this.successProgress = new SimpleDoubleProperty(0.0);
        this.volumeSet = new SimpleObjectProperty<>(null);

        this.state = GameState.IDLE;
        this.random = new Random();

        setupGameLoop();
    }

    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    return;
                }

                double deltaTime = (now - lastUpdateTime) / 10000000.0;
                lastUpdateTime = now;

                update(deltaTime);
            }
        };
    }

    public void startGame(double targetVol) {
        state = GameState.PLAYING;
        targetVolFixed = targetVol;
        jumpCount = 0;
        lastJumpTime = 0.0;
        targetPosition.set(targetVol);
        cursorPosition.set(0.5);
        cursorVelocity.set(0.0);
        successProgress.set(0.0);
        volumeSet.set(null);
        lastUpdateTime = 0;
        timeAtZeroProgress = 0;
        gameLoop.start();
    }

    public void stopGame() {
        state = GameState.IDLE;
        gameLoop.stop();
        volumeSet.set(-1.0);
    }

    private void update(double deltaTime) {
        if (state != GameState.PLAYING) return;

        // Apply gravity
        cursorVelocity.set(cursorVelocity.get() - GRAVITY * deltaTime);

        // Clamp velocity
        cursorVelocity.set(Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, cursorVelocity.get())));

        // Update position
        double newPos = cursorPosition.get() + cursorVelocity.get() * deltaTime / 2;
        double clampedPos = Math.max(0.0 + CURSOR_SIZE / 2, Math.min(1.0 - CURSOR_SIZE / 2, newPos));
        cursorPosition.set(clampedPos);

        // Reset velocity at top and bottom
        if (clampedPos == 0.0 && cursorVelocity.get() < 0) {
            cursorVelocity.set(0.0); // Hit bottom, stop gravity
        } else if (clampedPos == 1.0 && cursorVelocity.get() > 0) {
            cursorVelocity.set(- GRAVITY * deltaTime / 100); // Hit top, only apply gravity
        }

        // Move target slightly
        double targetDrift = (random.nextDouble() - 0.5) * TARGET_MOVE_SPEED * deltaTime;
        double newTargetPos = targetPosition.get() + targetDrift;
        targetPosition.set(Math.max(0.0, Math.min(1.0, newTargetPos)));

        // Winning conditions
        boolean overlapping = isOverlapping();
        if (overlapping) {
            successProgress.set(Math.min(1.0, successProgress.get() + SUCCESS_RATE * deltaTime));
            timeAtZeroProgress = 0;
            if (successProgress.get() >= 1.0) {
                // If victory occur during jump, jump back
                if (Math.abs(targetPosition.get() - targetVolFixed) > 0.0099) {
                    targetPosition.set(targetVolFixed);
                } else {
                    state = GameState.SUCCESS;
                    volumeSet.set(targetPosition.get());
                    gameLoop.stop();
                }
            }

            // Random chance to jump when close to success
            if (successProgress.get() > 0.5
                    && random.nextDouble() < TARGET_JUMP_CHANCE
                    && jumpCount < 3
                    && System.currentTimeMillis() - lastJumpTime > 3000
                ){
                targetPosition.set(random.nextDouble());
                jumpCount++;
                lastJumpTime = System.currentTimeMillis();
            }
        } else {
            double newProgress = Math.max(0.0, successProgress.get() - FAIL_RATE * deltaTime);
            successProgress.set(newProgress);

            if (newProgress <= 0.0) {
                timeAtZeroProgress += deltaTime;
                if (timeAtZeroProgress >= FAIL_TIMEOUT) {
                    stopGame();
                    System.out.println(isPlaying());
                }
            }
        }
    }

    private boolean isOverlapping() {
        double cursorTop = cursorPosition.get() - CURSOR_SIZE / 2;
        double cursorBottom = cursorPosition.get() + CURSOR_SIZE / 2;
        double targetTop = targetPosition.get() - TARGET_SIZE / 2;
        double targetBottom = targetPosition.get() + TARGET_SIZE / 2;

        return !(cursorBottom < targetTop || cursorTop > targetBottom);
    }

    public void onClickPress() {
        if (state == GameState.PLAYING) {
            cursorVelocity.set(cursorVelocity.get() + CLICK_BOOST);
        }
    }

    public DoubleProperty cursorPositionProperty() { return cursorPosition; }
    public DoubleProperty targetPositionProperty() { return targetPosition; }
    public DoubleProperty successProgressProperty() { return successProgress; }
    public ObjectProperty<Double> volumeSetProperty() { return volumeSet; }
    public boolean isPlaying() { return state == GameState.PLAYING; }
}