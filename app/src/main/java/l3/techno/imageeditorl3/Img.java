package l3.techno.imageeditorl3;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Img {

    //Attributs
    private Bitmap img_default;
    private Bitmap img_saved;
    private Bitmap img_displayed;

    //Constructeurs
    public Img(){ //Constructeur avec image par défaut si rien n'a été renseigné.

    }

    public Img(Bitmap image){ //Constructeur avec image renseigné via galerie ou caméra

    }


    //Méthodes
    public void toGray(){
        int w = img_displayed.getWidth();
        int h = img_displayed.getHeight();
        int pixels[] = new int[w * h];

        int red;
        int green;
        int blue;
        int grey;
        img_displayed.getPixels(pixels,0,w,0,0,w,h);
        for (int i=0; i<pixels.length;i++){
            red= Color.red(pixels[i]);
            green=Color.green(pixels[i]);
            blue=Color.blue(pixels[i]);
            grey = (int) (red*0.3)+(int) (green*0.59)+(int) (blue*0.11);
            pixels[i]=Color.rgb(grey,grey,grey);
        }
        img_displayed.setPixels(pixels,0,w,0,0,w,h);
    }

}
