package stardewvolume.volume;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import stardewvolume.ui.Assets;

public class VolumeGameView {
    private VolumeModel model;
    private VolumeController controller;
    private VolumeInputHandler inputHandler;

    private VBox container;
    private Canvas gameCanvas;
    private Slider volumeSlider;
    private Button startButton;

    public VolumeGameView(VolumeModel model, VolumeController controller) {
        this.model = model;
        this.controller = controller;
        this.inputHandler = new VolumeInputHandler(model);

        setupUI();
        setupListeners();
        startRenderLoop();
    }

    private void setupUI() {
        container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(40, 0, 0, 0));

        Label sliderLabel = new Label("Target Volume:");
        sliderLabel.setFont(Font.font("Monospaced", 15));
        sliderLabel.setTextFill(Assets.TEXT_COLOR);

        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setBlockIncrement(5);
        volumeSlider.setMinWidth(150);
        volumeSlider.setPrefWidth(150);
        volumeSlider.setMaxWidth(150);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            controller.setTargetVolume(newVal.doubleValue() / 100.0);
        });

        Label volumeValueLabel = new Label("50%");
        volumeValueLabel.setFont(Font.font("Monospaced", 30));
        volumeValueLabel.setTextFill(Assets.ACCENT_YELLOW);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            volumeValueLabel.setText(String.format("%d%%", newVal.intValue()));
        });

        // Game canvas
        gameCanvas = new Canvas(Assets.VOLUME_BAR_WIDTH, Assets.VOLUME_BAR_HEIGHT);
        setupInputHandlers();

        // Start button
        startButton = new Button("Start Game");
        startButton.setFont(Font.font("Monospaced", 15));
        startButton.setOnAction(e -> {
            model.startGame(controller.getTargetVolume());
            startButton.setDisable(true);
            volumeSlider.setDisable(true);
        });


        container.getChildren().addAll(
                sliderLabel,
                volumeValueLabel,
                volumeSlider,
                gameCanvas,
                startButton
        );
    }

    private void setupInputHandlers() {
        gameCanvas.setOnMousePressed(e -> inputHandler.handleMousePress());
        gameCanvas.setOnMouseReleased(e -> inputHandler.handleMouseRelease());
    }

    private void setupListeners() {
        model.volumeSetProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                startButton.setDisable(false);
                volumeSlider.setDisable(false);
            }
        });
    }

    private void startRenderLoop() {
        javafx.animation.AnimationTimer renderTimer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        renderTimer.start();
    }

    private void render() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        double width = gameCanvas.getWidth();
        double height = gameCanvas.getHeight();

        // Clear
        gc.setFill(Assets.BAR_EMPTY);
        gc.fillRect(0, 0, width, height);

        // Draw progress bar on left side
        double progressHeight = height * model.successProgressProperty().get();
        gc.setFill(Assets.ACCENT_GREEN);
        gc.fillRect(0, height - progressHeight, 10, progressHeight);

        // Draw cursor bar (inverted Y)
        double cursorY = height * (1.0 - model.cursorPositionProperty().get());
        double cursorHeight = height * 0.1;
        gc.setFill(Assets.CURSOR_BAR);
        gc.fillRect(8, cursorY - cursorHeight / 2, width - 15, cursorHeight);


        // Draw target marker
        double targetY = height * (1.0 - model.targetPositionProperty().get());
        double targetHeight = height * 0.02;
        gc.setFill(Assets.TARGET_COLOR);
        gc.fillRect(13, targetY - targetHeight / 2, width - 25, targetHeight);

        // Draw outer border
        gc.setStroke(Assets.BORDER_COLOR);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, width, height);
    }

    public VBox getView() {
        return container;
    }
}