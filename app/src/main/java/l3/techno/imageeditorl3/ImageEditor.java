package l3.techno.imageeditorl3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageEditor extends AppCompatActivity {

    //Attributs
    private Bitmap img;
    BitmapFactory.Options opts = new BitmapFactory.Options();
    private ImageView imv;
    int w;
    int h;
    int[] pixels;
    //Dimensions ??

    //Constructeur
    public ImageEditor(ImageView imv){
        w = img.getWidth();
        h = img.getHeight();
        pixels = new int[w * h];
        opts.inMutable = true;
        opts.inScaled = false;
        this.img = BitmapFactory.decodeResource(getResources(), R.drawable.lena,opts);
        this.imv.setImageBitmap(img);
        this.imv = imv;
    }

    //RESET IMAGE PAR DEFAUT
    public void defaultImg(){
        img = BitmapFactory.decodeResource(getResources(), R.drawable.lena, opts);
        this.imv.setImageBitmap(img);
    }

    //CONVERTIE L'IMAGE EN NIVEAU DE GRIS AVEC GETPIXELS (OPTIMAL)
    public void toGray(){
        int red;
        int green;
        int blue;
        int grey;
        int alpha = 0xFF << 24;
        img.getPixels(pixels, 0, w, 0, 0, w, h);
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
        img.setPixels(pixels, 0, w, 0, 0, w, h);
    }

}
