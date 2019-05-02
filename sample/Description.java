package sample;

import java.io.File;

public class Description {

    private File txtFile = null;
    private File imgFile = null;

    public File getTxtFile(){
        return txtFile;
    }

    public void setTxtFile(File txtFile){
        this.txtFile = txtFile;
    }

    public File getImgFile(){
        return imgFile;
    }

    public void setImgFile(File imgFile){
        this.imgFile = imgFile;
    }

}
