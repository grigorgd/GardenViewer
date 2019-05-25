package sample;

import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlantTable {
    private static final double MIN_WIDTH = 300.0;
    private static final double MIN_COLUMN_WIDTH = 50;
    private static ImagePattern pattern;

    public static void display(double x, double y, Map<Shape, Description> zones) {
        Stage stage = new Stage();
        stage.setMinWidth(MIN_WIDTH);
        stage.setX(x);
        stage.setY(y);

        TableView tableView = new TableView();

        TableColumn<String, Plant> nameColumn = new TableColumn<>("Nazwa");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> latinNameColumn = new TableColumn<>("Nazwa łacińska");
        latinNameColumn.setCellValueFactory(new PropertyValueFactory<>("latinName"));
        latinNameColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> groupColumn = new TableColumn<>("Grupa");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> formColumn = new TableColumn<>("Forma");
        formColumn.setCellValueFactory(new PropertyValueFactory<>("form"));
        formColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> soilColumn = new TableColumn<>("Gleba");
        soilColumn.setCellValueFactory(new PropertyValueFactory<>("soil"));
        soilColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> reactionColumn = new TableColumn<>("Odczyn");
        reactionColumn.setCellValueFactory(new PropertyValueFactory<>("reaction"));
        reactionColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> waterColumn = new TableColumn<>("Wilgotność");
        waterColumn.setCellValueFactory(new PropertyValueFactory<>("water"));
        waterColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> positionColumn = new TableColumn<>("Stanowisko");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> frostColumn = new TableColumn<>("Mrozoodporność");
        frostColumn.setCellValueFactory(new PropertyValueFactory<>("frost"));
        frostColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> pestDiseaseColumn = new TableColumn<>("Odporność na choroby i szkodniki");
        pestDiseaseColumn.setCellValueFactory(new PropertyValueFactory<>("frost"));
        pestDiseaseColumn.setMinWidth(MIN_COLUMN_WIDTH);

        tableView.getColumns().addAll(nameColumn, latinNameColumn, groupColumn, formColumn, soilColumn, reactionColumn, waterColumn, positionColumn, frostColumn, pestDiseaseColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        pattern = new ImagePattern(createHatch(), 0, 0, 5, 5, false);

        tableView.getSelectionModel().selectedItemProperty().addListener(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                List<Plant> selectedPlants = new ArrayList<>();
                selectedPlants.addAll(tableView.getSelectionModel().getSelectedItems());
                for(Shape shape : zones.keySet()){
                    shape.setFill(Color.TRANSPARENT);
                }
                for (Map.Entry<Shape, Description> entry : zones.entrySet()) {
                    for (Plant plant : selectedPlants) {
                        if (entry.hashCode() == plant.getZoneHashCode()) {
                            entry.getKey().setFill(pattern);
                        }
                    }
                }
            }
        });

        ArrayList<Plant> plants = new ArrayList<>();
        for(Map.Entry<Shape, Description> entry : zones.entrySet()){
            if(((Zone)entry.getKey()).isPlant() && entry.getValue().getTxtFile() != null && entry.getValue().getTxtFile().exists()){
                ArrayList<String> att = ContentManager.load(entry.getValue().getTxtFile().toURI().getPath());
                plants.add(new Plant(entry.hashCode(), att.get(0), att.get(1), att.get(2), att.get(3), att.get(4), att.get(5), att.get(6), att.get(7), att.get(8), att.get(9)));
            }
        }

        for(Plant plant : plants){
            tableView.getItems().add(plant);
        }

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(tableView);
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("/sample/styles/styles.css");
        stage.setScene(scene);

        stage.show();

    }

    private static Image createHatch(){
        Pane pane = new Pane();
        pane.setPrefSize(20, 20);
        Line fw = new Line(-5, -5, 25, 25);
        Line bw = new Line(-5, 25, 25, -5);
        fw.setStroke(Color.BLACK);
        bw.setStroke(Color.BLACK);
        fw.setStrokeWidth(1);
        bw.setStrokeWidth(1);
        pane.getChildren().addAll(fw, bw);
        new Scene(pane);
        return pane.snapshot(null, null);
    }
}
