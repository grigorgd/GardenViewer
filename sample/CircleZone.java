package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CircleZone extends Circle implements Zone{
    private static final double MIN_RADIUS = 4.0;
    private Text text;
    private boolean plant = false;

    public CircleZone(double x, double y, double radius){
        setCenterX(x);
        setCenterY(y);
        if(radius < MIN_RADIUS) setRadius(MIN_RADIUS);
        else setRadius(radius);
        setFill(Color.TRANSPARENT);

        text = new Text("");
        text.setFont(Font.font("Gabriola", 24));

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                text.setLayoutX(getCenterX() - text.getLayoutBounds().getWidth() / 2);
                text.setLayoutY(getCenterY() - getRadius() - 10);
                text.setFill(getStroke());
                setFill(getStroke());
                setOpacity(0.3);
                text.setVisible(true);
            }
        });

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(plant)setFill(Color.TRANSPARENT);
                setOpacity(1.0);
                text.setVisible(false);
            }
        });
    }

    public void move(double x, double y){
        setCenterX(getCenterX() + x);
        setCenterY(getCenterY() + y);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName() + "," + getCenterX() + "," + getCenterY() + "," + getRadius() + ",");
        sb.append(getStrokeWidth() + "," + getStroke() + "," + getName() + "," + plant);
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

    public boolean isPlant(){
        return plant;
    }


}
