package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Garden Viewer");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/sample/styles/styles.css");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/images/icon.png"));
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            controller.closeProgram();
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
