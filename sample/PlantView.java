package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class PlantView extends HBox {
    private static final double MIN_X = 150.0;
    private static final double MAX_X = 300.0;
    private static final double MAX_Y = 600.0;

    private static double x = 0;
    private static double y = 0;
    private static double translateX;
    private static double translateY;

    public static void display(File imgFile, File txtFile, double x, double y){
        Stage stage = new Stage();
        stage.setMaxHeight(MAX_Y);
        stage.setMinWidth(MIN_X);
        stage.setMaxWidth(MAX_X);
        stage.setResizable(false);
        PlantView.x = x;
        PlantView.y = y;
        stage.setX(x);
        stage.setY(y);

        VBox mainBox = new VBox(5);
        mainBox.setPadding(new Insets(10, 10, 10, 10));
        mainBox.setMaxWidth(MAX_X);
        mainBox.setAlignment(Pos.CENTER);

        if(imgFile != null && imgFile.exists()){
            ImageView imageView = new ImageView(new Image(imgFile.toURI().toString()));
            mainBox.getChildren().add(imageView);
        }

        VBox descBox = new VBox();
        descBox.setAlignment(Pos.CENTER);

        Text nameText = new Text("Nazwa: ");
        nameText.getStyleClass().add("bolded");
        Text latinText = new Text("Nazwa łacińska: ");
        latinText.getStyleClass().add("bolded");
        Text soilText = new Text("Gleba: ");
        soilText.getStyleClass().add("bolded");
        Text positionText = new Text("Stanowisko: ");
        positionText.getStyleClass().add("bolded");
        Text otherText = new Text("Inne: ");
        otherText.getStyleClass().add("bolded");

        if(txtFile != null && txtFile.exists()){
            String desc = TextLoader.read(txtFile.toURI().getPath());
            String[] split = desc.split("\n");
            Text text0 = new Text(split[0] + "\n");
            Text text1 = new Text(split[1] + "\n");
            Text text2 = new Text(split[2] + "\n");
            Text text3 = new Text(split[3] + "\n");
            StringBuilder sb = new StringBuilder();
            for(int i = 4; i < split.length; i++){
                sb.append(split[i]);
            }
            Text text4 = new Text(sb.toString());

            TextFlow descLabel = new TextFlow(nameText, text0, latinText, text1, soilText, text2, positionText, text3, otherText, text4);
            descLabel.setMaxWidth(280);

            descBox.getChildren().add(descLabel);


            mainBox.getChildren().add(descBox);
        }
        Button closeButton = new Button("Zamknij");
        closeButton.setPrefWidth(80);
        closeButton.setOnAction(event -> {
            stage.close();
        });
        mainBox.getChildren().add(closeButton);
        mainBox.getStyleClass().add("border-pane");

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add("/sample/styles/styles.css");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);


        stage.showAndWait();
    }


}
