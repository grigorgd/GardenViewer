package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class OptionView {

    public static int display(double x, double y){
        AtomicInteger result = new AtomicInteger(0);

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setX(x);
        stage.setY(y);

        HBox mainBox = new HBox(10);
        mainBox.setPadding(new Insets(10, 10, 10, 10));
        mainBox.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Anuluj");
        closeButton.setPrefWidth(80);
        closeButton.setOnAction(event -> stage.close());

        Button editButton = new Button("Edytuj");
        editButton.setPrefWidth(80);
        editButton.setOnAction(event -> {
            result.getAndSet(1);
            closeButton.fire();
        });

        Button removeButton = new Button("Usuń");
        removeButton.setPrefWidth(80);
        removeButton.setOnAction(event -> {
            result.getAndSet(2);
            closeButton.fire();
        });

        mainBox.getChildren().addAll(editButton, removeButton, closeButton);
        mainBox.getStyleClass().add("border-pane");

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add("/sample/styles/styles.css");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return result.get();
    }

    public static int confirm(double x, double y, String text){
        AtomicInteger result = new AtomicInteger(0);

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setX(x);
        stage.setY(y);

        VBox mainBox = new VBox(5);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(10, 10, 10, 10));

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        Label textLabel = new Label(text);

        Button closeButton = new Button("Anuluj");
        closeButton.setPrefWidth(80);
        closeButton.setOnAction(event -> stage.close());

        Button yesButton = new Button("Tak");
        yesButton.setPrefWidth(80);
        yesButton.setOnAction(event -> {
            result.getAndSet(1);
            closeButton.fire();
        });

        Button noButton = new Button("Nie");
        noButton.setPrefWidth(80);
        noButton.setOnAction(event -> {
            result.getAndSet(2);
            closeButton.fire();
        });

        hBox.getChildren().addAll(yesButton, noButton, closeButton);

        mainBox.getChildren().addAll(textLabel, hBox);
        mainBox.getStyleClass().add("border-pane");

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add("/sample/styles/styles.css");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return result.get();
    }

    public static boolean confirmArea(double x, double y, String text){
        AtomicBoolean result = new AtomicBoolean(false);

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setX(x);
        stage.setY(y);

        VBox mainBox = new VBox(5);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(10, 10, 10, 10));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Label textLabel = new Label(text);
        HBox descBox = new HBox(10);
        descBox.setAlignment(Pos.CENTER);
        descBox.getChildren().add(textLabel);
        ImageView alertImage = new ImageView();
        try{
            alertImage.setImage(new Image(new FileInputStream("src/resources/images/alert.png")));
            descBox.getChildren().add(alertImage);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        Button yesButton = new Button("Tak");
        yesButton.setPrefWidth(80);
        yesButton.setOnAction(event -> {
            result.getAndSet(true);
            stage.close();
        });

        Button noButton = new Button("Nie");
        noButton.setPrefWidth(80);
        noButton.setOnAction(event -> {
            stage.close();
        });

        buttonBox.getChildren().addAll(yesButton, noButton);

        mainBox.getChildren().addAll(descBox, buttonBox);
        mainBox.getStyleClass().add("border-pane");

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add("/sample/styles/styles.css");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return result.get();
    }

}
