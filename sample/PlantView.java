package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class PlantView extends HBox {
    private static final double MIN_X = 150.0;
    private static final double MAX_X = 300.0;
    private static final double MAX_Y = 600.0;
    private static final double MAX_IMAGE_SIZE = 280;

    public static void display(File imgFile, File txtFile, double x, double y){
        Stage stage = new Stage();
        stage.setMaxHeight(MAX_Y);
        stage.setMinWidth(MIN_X);
        stage.setMaxWidth(MAX_X);
        stage.setResizable(false);
        stage.setX(x);
        stage.setY(y);

        VBox mainBox = new VBox(5);
        mainBox.setPadding(new Insets(5, 10, 5, 10));
        mainBox.setMaxWidth(MAX_X);
        mainBox.setAlignment(Pos.CENTER);

        if(imgFile != null && imgFile.exists()){
            Image image = new Image(imgFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            mainBox.getChildren().add(imageView);

            if(image.getWidth() > MAX_IMAGE_SIZE || image.getHeight() > MAX_IMAGE_SIZE){
                if(image.getWidth() >= image.getHeight()) imageView.setFitWidth(MAX_IMAGE_SIZE);
                else imageView.setFitHeight(MAX_IMAGE_SIZE);
                imageView.setPreserveRatio(true);
            }
        }

        VBox descBox = new VBox();
        descBox.setAlignment(Pos.CENTER);

        Text nameText = new Text("Nazwa: ");
        nameText.getStyleClass().add("bolded");
        Text latinText = new Text("Nazwa łacińska: ");
        latinText.getStyleClass().add("bolded");
        Text groupText = new Text("Grupa: ");
        groupText.getStyleClass().add("bolded");
        Text formText = new Text("Forma: ");
        formText.getStyleClass().add("bolded");
        Text soilText = new Text("Gleba: ");
        soilText.getStyleClass().add("bolded");
        Text reactionText = new Text("Odczyn: ");
        reactionText.getStyleClass().add("bolded");
        Text waterText = new Text("Wilgotność");
        waterText.getStyleClass().add("bolded");
        Text positionText = new Text("Stanowisko: ");
        positionText.getStyleClass().add("bolded");
        Text frostText = new Text("Mróz: ");
        frostText.getStyleClass().add("bolded");
        Text pestDisease = new Text("Choroby i szkodniki: ");
        pestDisease.getStyleClass().add("bolded");

        if(txtFile != null && txtFile.exists()){
            String desc = TextLoader.read(txtFile.toURI().getPath());
            String[] split = desc.split("\n");
            Text text0 = new Text(split[0] + "\n");
            Text text1 = new Text(split[1] + "\n");
            Text text2 = new Text(split[2] + "\n");
            Text text3 = new Text(split[3] + "\n");
            Text text4 = new Text(split[4] + "\n");
            Text text5 = new Text(split[5] + "\n");
            Text text6 = new Text(split[6] + "\n");
            Text text7 = new Text(split[7] + "\n");
            Text text8 = new Text(split[8] + "\n");
            Text text9 = new Text(split[9] + "\n");

            TextFlow descLabel = new TextFlow(nameText, text0, latinText, text1, groupText, text2, formText, text3, soilText, text4, reactionText, text5, waterText, text6, positionText, text7, frostText, text8, pestDisease, text9);
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
