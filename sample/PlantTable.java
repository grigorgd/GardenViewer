package sample;

import javafx.collections.ObservableList;
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

        TableColumn<String, Plant> soilColumn = new TableColumn<>("Gleba");
        soilColumn.setCellValueFactory(new PropertyValueFactory<>("soil"));
        soilColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> reactionColumn = new TableColumn<>("Odczyn");
        reactionColumn.setCellValueFactory(new PropertyValueFactory<>("reaction"));
        reactionColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> positionColumn = new TableColumn<>("Stanowisko");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionColumn.setMinWidth(MIN_COLUMN_WIDTH);

        TableColumn<String, Plant> otherColumn = new TableColumn<>("Inne");
        otherColumn.setCellValueFactory(new PropertyValueFactory<>("other"));
        otherColumn.setMinWidth(MIN_COLUMN_WIDTH);
        otherColumn.setPrefWidth(100);

        tableView.getColumns().addAll(nameColumn, latinNameColumn, soilColumn, reactionColumn, positionColumn, otherColumn);
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
                String description = TextLoader.read(entry.getValue().getTxtFile().toURI().getPath());
                String[] split = description.split("\n");
                plants.add(new Plant(entry.hashCode(), split[0], split[1], split[2], split[3], split[4], split[5]));
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
