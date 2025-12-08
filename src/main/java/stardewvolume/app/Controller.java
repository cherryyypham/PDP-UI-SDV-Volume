package stardewvolume.app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import stardewvolume.video.VideoPlayer;
import stardewvolume.volume.VolumeController;
import stardewvolume.volume.VolumeModel;

public class Controller {
    private VideoPlayer videoPlayer;
    private VolumeController volumeController;
    private VolumeModel volumeModel;

    private DoubleProperty currentVolume = new SimpleDoubleProperty(0.5);

    public Controller() {
        this.volumeController = new VolumeController();
        this.volumeModel = new VolumeModel();

        volumeModel.volumeSetProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentVolume.set(newVal);
                if (videoPlayer != null) {
                    videoPlayer.setVolume(newVal);
                }
            }
        });
    }

    public void setVideoPlayer(VideoPlayer player) {
        this.videoPlayer = player;
        videoPlayer.setVolume(currentVolume.get());
    }

    public VideoPlayer getVideoPlayer() {
        return videoPlayer;
    }

    public VolumeController getVolumeController() {
        return volumeController;
    }

    public VolumeModel getVolumeModel() {
        return volumeModel;
    }

    public DoubleProperty currentVolumeProperty() {
        return currentVolume;
    }
}