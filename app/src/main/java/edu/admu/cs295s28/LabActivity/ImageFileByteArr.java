package edu.admu.cs295s28.LabActivity;

public class ImageFileByteArr {
    private static ImageFileByteArr IFB;

    private ImageFileByteArr(){}

    public static ImageFileByteArr get() {
        if(IFB == null){
            IFB = new ImageFileByteArr();
        }

        return IFB;
    }


}
