package sample;

import java.io.*;
import java.util.ArrayList;

public class ContentManager {

    public static void save(String string, String path){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write(string);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> load(String path){
        ArrayList<String> result = new ArrayList<>();

        String line = "";
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            while((line = br.readLine()) != null){
                result.add(line);
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
