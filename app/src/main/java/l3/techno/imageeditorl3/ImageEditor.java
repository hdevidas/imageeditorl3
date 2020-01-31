package l3.techno.imageeditorl3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static l3.techno.imageeditorl3.MainActivity.imv;

public class ImageEditor extends AppCompatActivity {

    //Attributs
    public Bitmap img_backup;
    public Bitmap img_actual;
    int w;
    int h;
    int[] pixels;
    //Dimensions ??

    //Constructeur
    public ImageEditor(Bitmap img){
        this.img_backup = img;
        this.img_actual = img;
        w = img.getWidth();
        h = img.getHeight();
        pixels = new int[w * h];


        //imv.setImageBitmap(img);
    }

    public void defaultimg(){
        //TODO
    }

    //RESET IMAGE PAR DEFAUT
    public void defaultImg(){
        img_backup.getPixels(pixels, 0, w, 0, 0, w, h);
        img_actual.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    //CONVERTIE L'IMAGE EN NIVEAU DE GRIS AVEC GETPIXELS (OPTIMAL)
    public void toGray(){
        int red;
        int green;
        int blue;
        int grey;
        int alpha = 0xFF << 24;
        img_actual.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                grey = pixels[w * x + y];
                red = ((grey & 0x00FF0000) >> 16);
                green = ((grey & 0x0000FF00) >> 8);
                blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[w * x + y] = grey;
            }
        }
        img_actual.setPixels(pixels, 0, w, 0, 0, w, h);
    }

/*
    //CONVERTIE L'IMAGE EN NIVEAU DE GRIS RENDERSCRIPT
    public void toGrayRS(){
        RenderScript rs = RenderScript.create(this);
        Allocation input = Allocation.createFromBitmap(rs , this.img_actual);
        Allocation  output= Allocation.createTyped(rs , input. getType ());

        ScriptC_gray  grayScript = new  ScriptC_gray(rs);
        grayScript.forEach_toGray(input , output);

        output.copyTo(this.img_actual);
        input.destroy (); output.destroy ();
        grayScript.destroy (); rs.destroy ();
    }
*/
}
