package sample;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Transformation {

    public static String onString(Map<Shape, Description> shapes){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Shape, Description> entry : shapes.entrySet()){
            if(entry.getKey().getClass().equals(CircleZone.class)){
                CircleZone circle  = (CircleZone)entry.getKey();
                sb.append(circle.toString());
            }
            else if(entry.getKey().getClass().equals(RectangleZone.class)){
                RectangleZone rectangle = (RectangleZone)entry.getKey();
                sb.append(rectangle.toString());
            }
            else if(entry.getKey().getClass().equals(PolygonZone.class)){
                PolygonZone polygon = (PolygonZone)entry.getKey();
                sb.append(polygon.toString());
            }
            //garden area
            else if(entry.getKey().getClass().equals(PolygonGarden.class)){
                PolygonGarden polygon = (PolygonGarden) entry.getKey();
                sb.append(polygon.toString());
            }

            sb.append(",");
            if(entry.getValue().getTxtFile() != null){
                sb.append(entry.getValue().getTxtFile().getAbsolutePath());
            }
            else sb.append("-");
            sb.append(",");
            if(entry.getValue().getImgFile() != null){
                sb.append(entry.getValue().getImgFile().getAbsolutePath());
            }
            else sb.append("-");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Map<Shape, Description> onShape(ArrayList<String> lines){
        Map<Shape, Description> shapes = new HashMap<>();
        Shape shape = null;
        Description description = new Description();

        for(String s : lines){
            String text = s.trim();
            String[] split = text.split(",");
            Class c = Object.class;
            try{
                c = Class.forName(split[0]);
            }catch(ClassNotFoundException ex){
                ex.printStackTrace();
            }
            if(c.equals(CircleZone.class)){
                shape = new CircleZone(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
                shape.setStrokeWidth(Double.parseDouble(split[4]));
                shape.setStroke(Paint.valueOf(split[5]));
                ((CircleZone) shape).setName(split[6]);
                ((CircleZone) shape).setPlant(Boolean.parseBoolean(split[7]));
                if(!split[8].equals("-"))description.setTxtFile(new File(split[8]));
                if(!split[9].equals("-"))description.setImgFile(new File(split[9]));
            }
            else if(c.equals(RectangleZone.class)){
                shape = new RectangleZone(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Double.parseDouble(split[4]));
                shape.setStrokeWidth(Double.parseDouble(split[5]));
                shape.setStroke(Paint.valueOf(split[6]));
                ((RectangleZone) shape).setName(split[7]);
                ((RectangleZone) shape).setPlant(Boolean.parseBoolean(split[8]));
                if(!split[9].equals("-"))description.setTxtFile(new File(split[9]));
                if(!split[10].equals("-"))description.setImgFile(new File(split[10]));
            }
            else if(c.equals(PolygonZone.class) || c.equals(PolygonGarden.class)){
                Double[] points = Arrays.stream(split[1].split(";"))
                        .map(Double::valueOf)
                        .toArray(Double[]::new);
                if(c.equals(PolygonGarden.class)){
                    shape = new PolygonGarden(points);
                    shape.setStrokeWidth(Double.parseDouble(split[2]));
                    shape.setStroke(Paint.valueOf(split[3]));
                    shape.setOpacity(0.6);
                }
                else{
                    shape = new PolygonZone(points);
                    shape.setStrokeWidth(Double.parseDouble(split[2]));
                    shape.setStroke(Paint.valueOf(split[3]));
                    ((PolygonZone) shape).setName(split[4]);
                    ((PolygonZone) shape).setPlant(Boolean.parseBoolean(split[5]));
                    if(!split[6].equals("-"))description.setTxtFile(new File(split[6]));
                    if(!split[7].equals("-"))description.setImgFile(new File(split[7]));
                }
            }
            shapes.put(shape, description);

        }

        return shapes;
    }
}
