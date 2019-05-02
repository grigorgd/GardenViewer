package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonGarden extends Polygon {
    public static final String DEFAULT_STROKE_COLOR = "#202050";

    public PolygonGarden(Double[] xy){
        getPoints().addAll(xy);
        setFill(Color.WHITE);
        setStrokeWidth(4);
        setStroke(Color.valueOf(DEFAULT_STROKE_COLOR));
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
        sb.append("," + getStrokeWidth() + "," + getStroke());
        return sb.toString();
    }

}
