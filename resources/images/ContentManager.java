package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ContentManager {

    public static void save(String string, String path){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))){
            writer.write(string);
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> load(String path){
        ArrayList<String> result = new ArrayList<>();
        String line = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))){
            while((line = reader.readLine()) != null){
                result.add(line);
            }
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
}
