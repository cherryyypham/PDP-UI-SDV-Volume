package stardewvolume.video;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

import javafx.scene.text.Font;

public class VideoControls {
    private VideoPlayer videoPlayer;
    private HBox controlsBar;
    private Button loadButton;
    private Button playPauseButton;
    private Button stopButton;

    public VideoControls(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
        this.controlsBar = new HBox(15);
        controlsBar.setAlignment(Pos.CENTER);

        setupControls();
        setupListeners();
    }

    private void setupControls() {
        loadButton = createButton("Load Video");
        loadButton.setOnAction(event -> videoPlayer.openFileChooser());

        playPauseButton = createButton("Play");
        playPauseButton.setOnAction(event -> togglePlayPause());
        playPauseButton.setDisable(true);

        stopButton = createButton("Stop");
        stopButton.setOnAction(event -> {
            videoPlayer.stop();
            playPauseButton.setText("Play");
        });
        stopButton.setDisable(true);

        controlsBar.getChildren().addAll(loadButton, playPauseButton, stopButton);
    }

    private void togglePlayPause() {
        MediaPlayer mediaPlayer = videoPlayer.getMediaPlayer();
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    private void setupListeners() {
        videoPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != MediaPlayer.Status.UNKNOWN) {
                playPauseButton.setDisable(false);
                stopButton.setDisable(false);
            }

            if (newValue == MediaPlayer.Status.PLAYING) {
                playPauseButton.setText("Pause");
            } else if (newValue == MediaPlayer.Status.PAUSED ||
                    newValue == MediaPlayer.Status.STOPPED ||
                    newValue == MediaPlayer.Status.READY) {
                playPauseButton.setText("Play");
            }
        });
    }


    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Monospaced", 12));
        button.setPrefWidth(100);
        return button;
    }

    public HBox getControlsBar() {
        return controlsBar;
    }
}
