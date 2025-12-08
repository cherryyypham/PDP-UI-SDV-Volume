package stardewvolume.app;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stardewvolume.ui.LayoutFactory;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Control
        Controller controller = new Controller();

        // Map controller to layout
        Parent root = LayoutFactory.createMainLayout(controller);
        root.setStyle("-fx-background-color: #22202f;");

        // Scene
        Scene scene = new Scene(root, 1920, 700);

        // Map scene to stage -> show stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Stardew Valley Volume Picker");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}