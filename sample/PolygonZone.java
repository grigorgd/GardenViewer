package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PolygonZone extends Polygon implements Zone {
    private Text text;
    private boolean plant;

    double pointX;
    double pointY;

    public PolygonZone(Double[] xy){
        getPoints().addAll(xy);

        setFill(Color.TRANSPARENT);

        text = new Text("");
        text.setFont(Font.font("Gabriola", 24));

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pointX = getPoints().get(0);
                pointY = getPoints().get(1);
                for(int i = getPoints().size() - 1; i >= 0; i--){
                    if(i % 2 != 0){
                        if(pointY > getPoints().get(i)){
                            pointY = getPoints().get(i);
                            pointX = getPoints().get(i - 1);
                        }
                    }
                }
                text.setLayoutX(pointX - text.getLayoutBounds().getWidth() / 2);
                text.setLayoutY(pointY - 10);
                text.setFill(getStroke());
                setFill(getStroke());
                setOpacity(0.3);
                text.setVisible(true);
            }
        });

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(plant) setFill(Color.TRANSPARENT);
                setOpacity(1.0);
                text.setVisible(false);
            }
        });

    }

    public void move(double x, double y){
        Double[] newCoordinates = new Double[getPoints().size()];
        for(int i = 0; i < getPoints().size(); i++){
            if(i % 2 == 0) newCoordinates[i] = getPoints().get(i) + x;
            else newCoordinates[i] = getPoints().get(i) + y;
        }
        getPoints().removeAll(getPoints());
        getPoints().addAll(newCoordinates);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName() + ",");
        Double[] doubleArray = getPoints().toArray(new Double[getPoints().size()]);
        String[] stringArray = new String[doubleArray.length];
        for(int i = 0; i < doubleArray.length; i++){
            stringArray[i] = String.valueOf(doubleArray[i]);
        }
        String points = String.join(";", stringArray);
        sb.append(points);
        sb.append("," + getStrokeWidth() + "," + getStroke() + "," + getName() + "," + plant);
        return sb.toString();
    }

    public String getName(){
        return text.getText();
    }

    public void setName(String name){
        text.setText(name);
    }

    public void addPane(Pane pane){
        pane.getChildren().addAll(this, text);
    }

    public void setTextVisible(boolean visible){
        this.text.setVisible(visible);
    }

    public void setPlant(boolean plant){
        this.plant = plant;
    }

}
