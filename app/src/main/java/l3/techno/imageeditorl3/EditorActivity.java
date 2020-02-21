package l3.techno.imageeditorl3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class EditorActivity extends AppCompatActivity {
    static Bitmap img;

    static ImageView imv;



    //------- Fonctions --------


    //TO GRAY AVEC RENDERSCRIPT
    private  void  toGrayRS(Bitmap  bmp) {
        RenderScript rs = RenderScript.create(this);
        Allocation input = Allocation.createFromBitmap(rs , bmp);
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
        setContentView(R.layout.activity_editor);

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

        Button bt_colorize = findViewById(R.id.colorize);
        bt_colorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.colorize();
            }
        });

        Button bt_contrastDE = findViewById(R.id.contrastDE);
        bt_contrastDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.contrastDE();
            }
        });

        Button bt_contrastHE = findViewById(R.id.contrastHE);
        bt_contrastHE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.contrastHE();
            }
        });

        final Button bt_convolve = findViewById(R.id.convolve);
        bt_convolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] mask =
                        {
                                {1, 2, 3, 2, 1},
                                {2, 6, 8, 6, 2},
                                {3, 8, 10, 8, 3},
                                {2, 6, 8, 6, 2},
                                {1, 2, 3, 2, 1}
                        };
                img1.convolve(mask);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        imv = (ImageView) findViewById(R.id.lena);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        opts.inScaled = false;
        final Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.lena,opts);
        final ImageEditor img1 = new ImageEditor(img);

        switch (item.getItemId()){
            case R.id.item1:
                img1.toGray();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
