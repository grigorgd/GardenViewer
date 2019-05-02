package sample;

import javafx.scene.layout.Pane;

public interface Zone {

    public void move(double x, double y);

    public void addPane(Pane pane);

    public void setTextVisible(boolean visible);

    public void setPlant(boolean plant);

}
