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

    static ImageView imv;



    //------- Fonctions --------

    //RESET IMAGE PAR DEFAUT
    void defaultImg(){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        opts.inScaled = false;
        img = BitmapFactory.decodeResource(getResources(), R.drawable.lena, opts);
        imv.setImageBitmap(img);
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
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        opts.inScaled = false;
        final Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.lena,opts);
        final ImageEditor img1 = new ImageEditor(img);

        //img1.defaultImg();
        imv.setImageBitmap(img1.img_actual);


        // ----------------------BOUTONS---------------------

        //Bouton default (Remettre l'image par défaut)
        Button bt_default = findViewById(R.id.bt_default);
        bt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    img1.defaultImg();
            }
        });

        //Bouton Gray v2 (codé avec getpixels)
        Button bt_gray_2 = findViewById(R.id.bt_gray);
        bt_gray_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.toGray();
            }
        });

        //Bouton Gray v3 (codé avec renderscript) #3
        Button bt_grayRS = findViewById(R.id.bt_grayRS);
        bt_grayRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGrayRS(img1.img_actual);
            }
        });

    }
}
