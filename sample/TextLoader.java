package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextLoader {

    public static String read(String path){
        String file = null;
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            file = sb.toString();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return file;
    }
}
