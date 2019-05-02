package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

public class Controller {
    public static final String DEFAULT_GRID_COLOR = "#80B088";

    private PolygonGarden garden;
    private Map<Shape, Description> zones;
    private Shape currentShape;

    private Preferences prefs;

    //name of current open file
    private File currentFile = null;
    //flag inform that there was changes in file
    private boolean changes = false;

    private double x0, y0, x1, y1;
    private Circle tempCircle;
    private Rectangle tempRectangle;
    private ArrayList<Circle> tempPolygon;
    private ArrayList<Line> lines;
    private Line tempLine;
    private int strokeWidth = 1;
    private File imgFile;
    private File txtFile;

    @FXML
    private ScrollPane scrollArea;

    @FXML
    private AnchorPane workingArea;

    @FXML
    private VBox toolPane;
    @FXML
    private TextField nameTextField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button strokeButton;
    private ToggleGroup group;
    @FXML
    private ToggleButton circleButton, rectangleButton, polygonButton, moveButton;
    private FileChooser txtChooser, imgChooser, saveChooser;
    @FXML
    private Button loadDescButton, loadImageButton;
    @FXML
    private Label descLabel, imageLabel;
    @FXML
    private Button txtClearButton, imgClearButton;
    @FXML
    private Button gardenButton;
    private int gardenMode = 0;//0 - plant, 1 - obstacle, 2 - garden area
    @FXML
    private MenuItem newMenuItem, saveMenuItem, saveAsMenuItem, loadMenuItem, exitMenuItem;
    @FXML
    private CheckMenuItem showNamesMenuItem;
    @FXML
    private CheckMenuItem noGrid, smallGrid, mediumGrid, bigGrid;

    @FXML
    public void initialize(){
        prefs = Preferences.userRoot().node(this.getClass().getName());

        zones = new HashMap<>();

        txtChooser = new FileChooser();
        txtChooser.setTitle("Dodaj opis");
        txtChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));

        imgChooser = new FileChooser();
        imgChooser.setTitle("Dodaj obraz");
        imgChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

        saveChooser = new FileChooser();
        saveChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));

        workingArea = new AnchorPane();

        workingArea.setPrefSize(1200, 1600);
        scrollArea.setContent(workingArea);

        //tools
        colorPicker.setValue(Color.CORAL);
        strokeButton.setOnAction(event -> {
            strokeWidth++;
            if(strokeWidth == 5) strokeWidth = 1;
            strokeButton.setText("grubość = " + strokeWidth);
        });
        group = new ToggleGroup();
        circleButton.setToggleGroup(group);
        rectangleButton.setToggleGroup(group);
        polygonButton.setToggleGroup(group);
        moveButton.setToggleGroup(group);

        gardenButton.setOnAction(event -> {
            if(gardenMode == 0){
                gardenButton.setText("Przeszkody");
            }
            else if(gardenMode == 1){
                gardenButton.setText("Granice");
            }
            else{
                gardenButton.setText("Rośliny");
            }
            gardenMode++;
            if(gardenMode == 3) gardenMode = 0;
        });

        loadDescButton.setOnAction(event -> {
            txtFile = txtChooser.showOpenDialog(scrollArea.getScene().getWindow());
            if(txtFile != null){
                txtChooser.setInitialDirectory(txtFile.getParentFile());
                descLabel.setText(txtFile.getName());
            }
        });

        loadImageButton.setOnAction(event -> {
            imgFile = imgChooser.showOpenDialog(scrollArea.getScene().getWindow());
            if(imgFile != null){
                imgChooser.setInitialDirectory(imgFile.getParentFile());
                imageLabel.setText(imgFile.getName());
            }
        });

        txtClearButton.setOnAction(event -> {
            txtFile = null;
            descLabel.setText("");
        });

        imgClearButton.setOnAction(event -> {
            imgFile = null;
            imageLabel.setText("");
        });

        //menu

        newMenuItem.setOnAction(event -> {
            if(changes){
                int response = OptionView.confirm(workingArea.getScene().getWindow().getX() + 100, workingArea.getScene().getWindow().getY() + 200, "Czy chcesz zapisać zmiany w pliku przed utworzeniem nowego projektu?");
                if(response == 1){
                    if(currentFile == null){
                        saveAsMenuItem.fire();
                    }
                    else{
                        saveMenuItem.fire();
                    }
                    currentFile = null;
                    changes = false;
                    zones.clear();
                    workingArea.getChildren().clear();
                }
                else if(response == 2){
                    currentFile = null;
                    changes = false;
                    zones.clear();
                    workingArea.getChildren().clear();
                }
            }
            else{
                if(currentFile != null){
                    zones.clear();
                    workingArea.getChildren().clear();
                }
            }
        });

        saveMenuItem.setOnAction(event -> {
            if(currentFile == null){
                saveAsMenuItem.fire();
            }
            else{
                //add garden area
                if(garden != null)zones.put(garden, new Description());
                ContentManager.save(Transformation.onString(zones), currentFile.getAbsolutePath());
                changes = false;
            }
        });

        saveAsMenuItem.setOnAction(event -> {
            saveChooser.setTitle("Zapisz jako");
            File file = saveChooser.showSaveDialog(workingArea.getScene().getWindow());
            if(file != null){
                //add garden area
                if(garden != null) zones.put(garden, new Description());
                saveChooser.setInitialDirectory(file.getParentFile());
                ContentManager.save(Transformation.onString(zones), file.getAbsolutePath());
                changes = false;
                currentFile = file;
            }
        });

        loadMenuItem.setOnAction(event -> {
            saveChooser.setTitle("Otwórz");
            File file = saveChooser.showOpenDialog(workingArea.getScene().getWindow());
            if(file != null){
                saveChooser.setInitialDirectory(file.getParentFile());
                currentFile = file;
                zones.clear();
                workingArea.getChildren().clear();
                garden = null;
                ArrayList<String> shapesString = ContentManager.load(file.getAbsolutePath());
                zones = Transformation.onShape(shapesString);

                for(Map.Entry<Shape, Description> entry : zones.entrySet()){
                    if(entry.getKey() instanceof PolygonGarden){
                        //garden = new PolygonGarden(new Double[]{1.0, 1.0, 2.0, 2.0, 2.0, 4.0});
                        garden = (PolygonGarden) entry.getKey();
                    }
                }
                if(garden != null){
                    zones.remove(garden);
                    workingArea.getChildren().add(garden);
                }

                Zone zone = null;
                for(Shape sh : zones.keySet()){
                    ((Zone) sh).addPane(workingArea);
                }
            }
        });

        exitMenuItem.setOnAction(event -> {
            closeProgram();
        });

        showNamesMenuItem.setOnAction(event -> {
            for(Shape sh : zones.keySet()){
                ((Zone)sh).setTextVisible(showNamesMenuItem.isSelected());
            }
        });


        noGrid.setOnAction(event -> {
            smallGrid.setSelected(false);
            mediumGrid.setSelected(false);
            bigGrid.setSelected(false);
            workingArea.setStyle("-fx-background-color: white;");
        });

        smallGrid.setOnAction(event -> {
            noGrid.setSelected(false);
            mediumGrid.setSelected(false);
            bigGrid.setSelected(false);
            workingArea.setStyle("-fx-background-color: white,\n" +
                    "linear-gradient(from 0px 0px to 10px 0px, repeat, " + DEFAULT_GRID_COLOR + " 4%, transparent 8%),\n" +
                    "linear-gradient(from 0px 0px to 0px 10px, repeat, " + DEFAULT_GRID_COLOR + " 4%, transparent 8%);");
        });

        mediumGrid.setOnAction(event -> {
            smallGrid.setSelected(false);
            noGrid.setSelected(false);
            bigGrid.setSelected(false);
            workingArea.setStyle("-fx-background-color: white,\n" +
                    "linear-gradient(from 0px 0px to 20px 0px, repeat, " + DEFAULT_GRID_COLOR + " 2%, transparent 4%),\n" +
                    "linear-gradient(from 0px 0px to 0px 20px, repeat, " + DEFAULT_GRID_COLOR + " 2%, transparent 4%);");
        });

        bigGrid.setOnAction(event -> {
            smallGrid.setSelected(false);
            mediumGrid.setSelected(false);
            noGrid.setSelected(false);
            workingArea.setStyle("-fx-background-color: white,\n" +
                    "linear-gradient(from 0px 0px to 40px 0px, repeat, " + DEFAULT_GRID_COLOR + " 1%, transparent 2%),\n" +
                    "linear-gradient(from 0px 0px to 0px 40px, repeat, " + DEFAULT_GRID_COLOR + " 1%, transparent 2%);");
        });

        //drawing
        drawingShape();
        drawingPolygon();
        showingDescription();
        tempPolygon = new ArrayList<>();
        lines = new ArrayList<>();

        //preferences
        saveChooser.setInitialDirectory(new File(prefs.get("saveDir", System.getProperty("user.home"))));
        txtChooser.setInitialDirectory(new File(prefs.get("saveTxt", System.getProperty("user.home"))));
        imgChooser.setInitialDirectory(new File(prefs.get("saveImg", System.getProperty("user.home"))));
        int grid = prefs.getInt("grid", 0);
        if(grid == 0){
            noGrid.setSelected(true);
            noGrid.fire();
        }
        else if(grid == 1){
            smallGrid.setSelected(true);
            smallGrid.fire();
        }
        else if(grid == 2){
            mediumGrid.setSelected(true);
            mediumGrid.fire();
        }
        else{
            bigGrid.setSelected(true);
            bigGrid.fire();
        }

    }


    private void drawingPolygon(){
        workingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY){
                    if(polygonButton.isSelected() || gardenMode == 2){

                        x0 = event.getX();
                        y0 = event.getY();

                        tempCircle = new Circle(x0, y0, 5);
                        tempCircle.setStroke(Color.TRANSPARENT);
                        tempCircle.setFill(colorPicker.getValue());
                        tempPolygon.add(tempCircle);

                        if(tempPolygon.size() > 1){
                            Circle start = tempPolygon.get(tempPolygon.size() - 2);
                            Circle end = tempPolygon.get(tempPolygon.size() - 1);
                            tempLine = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
                            tempLine.setStroke(colorPicker.getValue());
                            tempLine.setStrokeWidth(strokeWidth);
                            lines.add(tempLine);
                            workingArea.getChildren().add(tempLine);
                        }
                        workingArea.getChildren().add(tempCircle);

                        if(tempPolygon.size() > 3){
                            double centerX = tempPolygon.get(0).getCenterX();
                            double centerY = tempPolygon.get(0).getCenterY();
                            double pointX = tempPolygon.get(tempPolygon.size() - 1).getCenterX();
                            double pointY = tempPolygon.get(tempPolygon.size() - 1).getCenterY();
                            if(Math.hypot(centerX - pointX, centerY - pointY) < 5){
                                Double[] coordinates = new Double[tempPolygon.size() * 2 - 2];
                                for(int i = 0; i < tempPolygon.size() - 1; i++){
                                    coordinates[i * 2] = tempPolygon.get(i).getCenterX();
                                    coordinates[i * 2 + 1] = tempPolygon.get(i).getCenterY();
                                }
                                if(polygonButton.isSelected()){
                                    PolygonZone polygonZone = new PolygonZone(coordinates);
                                    polygonZone.setStrokeWidth(strokeWidth);
                                    polygonZone.setStroke(colorPicker.getValue());
                                    polygonZone.setName(nameTextField.getText());
                                    if(gardenMode == 0) polygonZone.setPlant(true);
                                    polygonZone.addPane(workingArea);

                                    Description description = getDescription();
                                    replaceShape();
                                    zones.put(polygonZone, description);
                                }
                                else if(gardenMode == 2){
                                    boolean changeBorder = true;
                                    if(garden != null) {
                                        changeBorder = OptionView.confirmArea(workingArea.getScene().getWindow().getX() + 100,
                                                workingArea.getScene().getWindow().getY() + 200, "Jesteś pewny że chcesz ustawić nowe granice?");
                                    }
                                    if(changeBorder){
                                        workingArea.getChildren().remove(garden);
                                        garden = new PolygonGarden(coordinates);
                                        garden.setStrokeType(StrokeType.OUTSIDE);
                                        garden.setOpacity(0.8);
                                        workingArea.getChildren().add(garden);
                                        garden.toBack();
                                    }
                                }

                                changes = true;

                                for(Line l : lines){
                                    workingArea.getChildren().remove(l);
                                }
                                for(Circle c : tempPolygon){
                                    workingArea.getChildren().remove(c);
                                }
                                lines.clear();
                                tempPolygon.clear();
                                workingArea.getChildren().remove(tempCircle);
                                workingArea.getChildren().remove(tempLine);
                            }
                        }
                    }
                    if(!polygonButton.isSelected() && !circleButton.isSelected() && !rectangleButton.isSelected() && !moveButton.isSelected()){

                        EventTarget target = event.getTarget();
                        if(target instanceof Zone){
                            Shape shape;
                            String name = "";
                            File txtFile;
                            File imgFile;
                            if(target.getClass().equals(CircleZone.class)){
                                shape = (CircleZone)target;
                                name = ((CircleZone) shape).getName();
                            }
                            else if(target.getClass().equals(RectangleZone.class)){
                                shape = (RectangleZone)target;
                                name = ((RectangleZone) shape).getName();
                            }
                            else{
                                shape = (PolygonZone)target;
                                name = ((PolygonZone) shape).getName();
                            }

                            int result = OptionView.display(workingArea.getScene().getWindow().getX() + event.getX(), workingArea.getScene().getWindow().getY() + event.getY());
                            if(result == 1){
                                currentShape = shape;
                                nameTextField.setText(name);
                                colorPicker.setValue((Color)shape.getStroke());
                                strokeWidth = (int)shape.getStrokeWidth();
                                strokeButton.setText("grubość = " + strokeWidth);
                                txtFile = zones.get(shape).getTxtFile();
                                setTxtFile(txtFile);
                                if(txtFile == null) descLabel.setText("");
                                else descLabel.setText(txtFile.getName());
                                imgFile = zones.get(shape).getImgFile();
                                setImgFile(imgFile);
                                if(imgFile == null) imageLabel.setText("");
                                else imageLabel.setText(imgFile.getName());
                                changes = true;
                            }
                            else if(result == 2){
                                zones.remove(shape);
                                workingArea.getChildren().remove(shape);
                                changes = true;
                            }
                        }
                    }
                }
            }
        });
    }

    private void drawingShape(){
        workingArea.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY && gardenMode != 2){
                    x0 = event.getX();
                    x1 = event.getX();
                    y0 = event.getY();
                    y1 = event.getY();
                    if(circleButton.isSelected()){
                        tempCircle = new Circle();
                        tempCircle.setCenterX(x0);
                        tempCircle.setCenterY(y0);
                        tempCircle.setRadius(0);
                        tempCircle.setStroke(colorPicker.getValue());
                        tempCircle.setFill(Color.TRANSPARENT);
                        tempCircle.setStrokeWidth(strokeWidth);
                        workingArea.getChildren().add(tempCircle);
                    }
                    else if(rectangleButton.isSelected()){
                        tempRectangle = new Rectangle(x0, y0, 0, 0);
                        tempRectangle.setStroke(colorPicker.getValue());
                        tempRectangle.setFill(Color.TRANSPARENT);
                        tempRectangle.setStrokeWidth(strokeWidth);
                        workingArea.getChildren().add(tempRectangle);
                    }
                    else if(!polygonButton.isSelected() && !circleButton.isSelected() && !rectangleButton.isSelected()){
                        EventTarget target = event.getTarget();
                        if(Shape.class.isAssignableFrom(target.getClass())){
                            currentShape = (Shape) target;
                        }
                    }
                }
            }
        });

        workingArea.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY && gardenMode != 2){
                    if(circleButton.isSelected()){
                        workingArea.getChildren().remove(tempCircle);
                        CircleZone circleZone = new CircleZone(x0, y0, Math.hypot(x0 - x1, y0 - y1));
                        circleZone.setStrokeWidth(strokeWidth);
                        circleZone.setStroke(colorPicker.getValue());
                        circleZone.setName(nameTextField.getText());
                        if(gardenMode == 0) circleZone.setPlant(true);
                        circleZone.addPane(workingArea);
                        Description description = getDescription();
                        replaceShape();
                        changes = true;
                        zones.put(circleZone, description);
                    }
                    else if(rectangleButton.isSelected()){
                        workingArea.getChildren().remove(tempRectangle);
                        RectangleZone rectangleZone = new RectangleZone((x0<x1)?x0:x1, (y0<y1)?y0:y1, Math.abs(x0 - x1), Math.abs(y0 - y1));
                        rectangleZone.setStrokeWidth(strokeWidth);
                        rectangleZone.setStroke(colorPicker.getValue());
                        rectangleZone.setName(nameTextField.getText());
                        if(gardenMode == 0) rectangleZone.setPlant(true);
                        rectangleZone.addPane(workingArea);
                        Description description = getDescription();
                        replaceShape();
                        changes = true;
                        zones.put(rectangleZone, description);
                    }
                    else if(moveButton.isSelected() && currentShape != null){
                        Description des = zones.get(currentShape);
                        if(currentShape instanceof Zone){
                            ((Zone)currentShape).move(currentShape.getTranslateX(), currentShape.getTranslateY());
                            changes = true;
                        }
                        zones.replace(currentShape, des);
                        currentShape.setTranslateX(0);
                        currentShape.setTranslateY(0);
                        currentShape = null;
                    }
                }
            }
        });

        workingArea.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY && gardenMode != 2){
                    x1 = event.getX();
                    y1 = event.getY();
                    if(circleButton.isSelected()){
                        tempCircle.setRadius(Math.hypot(x0 - x1, y0 - y1));
                    }
                    else if(rectangleButton.isSelected()){
                        tempRectangle.setX((x0<x1)?x0:x1);
                        tempRectangle.setWidth(Math.abs(x0 - x1));
                        tempRectangle.setY((y0<y1)?y0:y1);
                        tempRectangle.setHeight(Math.abs(y0 - y1));
                    }
                    else if(moveButton.isSelected() && currentShape != null && currentShape instanceof Zone){
                        currentShape.setTranslateX(x1 - x0);
                        currentShape.setTranslateY(y1 - y0);
                        changes = true;
                    }
                }
            }
        });
    }

    private void showingDescription(){
        workingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY){
                    EventTarget target = event.getTarget();
                    if(target instanceof Zone){
                        Shape shape = (Shape) target;
                        if(zones.get(shape).getTxtFile() != null || zones.get(shape).getImgFile() != null){
                            PlantView.display(zones.get(shape).getImgFile(), zones.get(shape).getTxtFile(),
                                    workingArea.getScene().getWindow().getX() + event.getX(),
                                    workingArea.getScene().getWindow().getY() + event.getY());
                        }
                    }
                }
            }
        });
    }

    private void setTxtFile(File txtFile){
        this.txtFile = txtFile;
    }

    private void setImgFile(File imgFile){
        this.imgFile = imgFile;
    }

    private void replaceShape(){
        if(currentShape != null){
            zones.remove(currentShape);
            workingArea.getChildren().remove(currentShape);
            currentShape = null;
        }
    }

    private Description getDescription(){
        Description description = new Description();
        description.setTxtFile(txtFile);
        description.setImgFile(imgFile);
        return  description;
    }

    public void closeProgram(){
        prefs.put("saveDir", saveChooser.getInitialDirectory().getPath());
        prefs.put("saveImg", imgChooser.getInitialDirectory().getPath());
        prefs.put("saveTxt", txtChooser.getInitialDirectory().getPath());
        int grid = 0;
        if(smallGrid.isSelected()) grid = 1;
        else if(mediumGrid.isSelected()) grid = 2;
        else if(bigGrid.isSelected()) grid = 3;
        prefs.putInt("grid", grid);
        if(changes){
            int response = OptionView.confirm(workingArea.getScene().getWindow().getX() + 100, workingArea.getScene().getWindow().getY() + 200, "Czy chcesz zapisać zmiany w pliku przez zamknięciem?");
            if(response == 1){
                if(currentFile == null){
                    saveAsMenuItem.fire();
                }
                else{
                    saveMenuItem.fire();
                }
                Platform.exit();
            }
            else if(response == 2){
                Platform.exit();
            }
        }
        else{
            Platform.exit();
        }
    }


}
