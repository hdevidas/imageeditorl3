package l3.techno.imageeditorl3;

import androidx.appcompat.app.AppCompatActivity;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;
import static android.graphics.Color.colorToHSV;
import static android.graphics.Color.rgb;
import static android.graphics.Color.valueOf;


public class MainActivity extends AppCompatActivity {


    static Bitmap img;
    ImageView imv;

    //------- Fonctions --------

    //RESET IMAGE PAR DEFAUT
    void defaultImg(){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        opts.inScaled = false;
        img = BitmapFactory.decodeResource(getResources(), R.drawable.lena, opts);
        imv.setImageBitmap(img);
    }

    //CONVERTIE L'IMAGE EN NIVEAU DE GRIS AVEC GETPIXELS (OPTIMAL)
    void toGrayV2(Bitmap img){
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = new int[w * h];
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


    //TO GRAY AVEC RENDERSCRIPT
    private  void  toGrayRS(Bitmap  bmp) {
        RenderScript  rs = RenderScript.create(this);
        Allocation  input = Allocation.createFromBitmap(rs , bmp);
        Allocation  output= Allocation.createTyped(rs , input. getType ());

        ScriptC_gray  grayScript = new  ScriptC_gray(rs);
        grayScript.forEach_toGray(input , output);

        output.copyTo(bmp);
        input.destroy (); output.destroy ();
        grayScript.destroy (); rs.destroy ();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.textview);
        imv = (ImageView) findViewById(R.id.lena);

        // ----------------------BOUTONS---------------------

        //Bouton default (Remettre l'image par défaut)
        Button bt_default = findViewById(R.id.bt_default);
        bt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultImg();
            }
        });

        //Bouton Gray v2 (codé avec getpixels)
        Button bt_gray_2 = findViewById(R.id.bt_gray);
        bt_gray_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGrayV2(img);
            }
        });

        //Bouton Gray v3 (codé avec renderscript) #3
        Button bt_grayRS = findViewById(R.id.bt_grayRS);
        bt_grayRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGrayRS(img);
            }
        });

    }
}
