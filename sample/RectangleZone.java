package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class RectangleZone extends Rectangle implements Zone {
    private static final double MIN_SIDE = 8.0;
    private Text text;
    private boolean plant;

    public RectangleZone(double x, double y, double width, double height){
        setX(x);
        setY(y);
        if(width < MIN_SIDE) setWidth(MIN_SIDE);
        else setWidth(width);
        if(height < MIN_SIDE) setHeight(MIN_SIDE);
        else setHeight(height);
        setFill(Color.TRANSPARENT);

        text = new Text("");
        text.setFont(Font.font("Gabriola", 24));

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                text.setLayoutX(getX() + width/2 - text.getLayoutBounds().getWidth() / 2);
                text.setLayoutY(getY() - 10);
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
        setX(getX() + x);
        setY(getY() + y);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName() + "," + getX() + "," + getY() + "," + getWidth() + "," + getHeight() + ",");
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

}
