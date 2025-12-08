package stardewvolume.ui;

import stardewvolume.app.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import stardewvolume.video.VideoPlayer;
import stardewvolume.video.VideoControls;
import stardewvolume.volume.VolumeGameView;

public class LayoutFactory {

    public static BorderPane createMainLayout(Controller controller) {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Assets.BACKGROUND, null, null)));
        root.setPadding(new Insets(20));

        VideoPlayer videoPlayer = new VideoPlayer();
        controller.setVideoPlayer(videoPlayer);
        VBox videoSection = createVideoSection(videoPlayer);
        root.setCenter(videoSection);

        VBox volumeSection = createVolumeSection(controller);
        root.setRight(volumeSection);

        return root;
    }

    private static VBox createVideoSection(VideoPlayer videoPlayer) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(10, 30, 10, 10));

        StackPane videoContainer = videoPlayer.getView();
        videoContainer.setPrefSize(1280, 630);

        VideoControls controls = new VideoControls(videoPlayer);
        HBox controlsBox = controls.getControlsBar();

        section.getChildren().addAll(videoContainer, controlsBox);
        return section;
    }

    private static VBox createVolumeSection(Controller controller) {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(10, 20, 10, 20));
        section.setBackground(new Background(new BackgroundFill(Assets.PANEL_BG,
                new CornerRadii(5), null)));
        section.setStyle("-fx-border-color: #5c5a70; -fx-border-width: 3; -fx-border-radius: 5;");
        section.setPrefWidth(300);

        VolumeGameView gameView = new VolumeGameView(
                controller.getVolumeModel(),
                controller.getVolumeController()
        );

        section.getChildren().addAll(gameView.getView());
        return section;
    }
}