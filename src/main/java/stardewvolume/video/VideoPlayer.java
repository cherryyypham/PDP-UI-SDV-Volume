package stardewvolume.video;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

public class VideoPlayer {
    private ObjectProperty<MediaPlayer.Status> statusProperty;
    public MediaView mediaView;
    private StackPane viewContainer;
    private MediaPlayer mediaPlayer;

    public VideoPlayer() {
        this.statusProperty = new SimpleObjectProperty<>(MediaPlayer.Status.UNKNOWN);
        this.mediaView = new MediaView();
        this.viewContainer = new StackPane();
        File video = new File("/Users/cherryyypham/github.com/PDP-UI-SDV-Volume/src/main/resources/the-duck-song.mp4");
        loadVideo(video);
    }

    public void loadVideo(File videoFile) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }

        mediaPlayer = new MediaPlayer(new Media(videoFile.toURI().toString()));
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(1100);

        statusProperty.bind(mediaPlayer.statusProperty());
        viewContainer.getChildren().clear();
        viewContainer.getChildren().add(mediaView);
        mediaPlayer.play();


    }

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video to play");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", ".mov"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            loadVideo(selectedFile);
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public StackPane getView() {
        return viewContainer;
    }

    public ObjectProperty<MediaPlayer.Status> statusProperty() {
        return statusProperty;
    }
}
