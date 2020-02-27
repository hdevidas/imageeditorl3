package l3.techno.imageeditorl3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    static Bitmap img;
    BitmapFactory.Options opts = new BitmapFactory.Options();

    static PhotoView imv;
    SeekBar seekbar;

    ImageEditor img1;

    boolean visibility = true;

    TextView tv;



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
        tv = findViewById(R.id.size);

        seekbar = findViewById(R.id.seekBar);

        imv = (PhotoView) findViewById(R.id.lena);
        opts.inMutable = true;
        opts.inScaled = false;
        img = BitmapFactory.decodeResource(getResources(), R.drawable.lena,opts);
        img1 = new ImageEditor(img);

        //img1.defaultImg();
        imv.setImageBitmap(img1.img_actual);


        // ----------------------BOUTONS---------------------

        //Bouton default (Remettre l'image par défaut)
        final Button bt_default = findViewById(R.id.bt_default);
        bt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.defaultImg();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        //Bouton Gray v2 (codé avec getpixels)
        final Button bt_gray = findViewById(R.id.bt_gray);
        bt_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.toGray();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        //Bouton Gray v3 (codé avec renderscript) #3
        final Button bt_grayRS = findViewById(R.id.bt_grayRS);
        bt_grayRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGrayRS(img1.img_actual);
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final Button bt_colorize = findViewById(R.id.colorize);
        bt_colorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        img1.colorize((float)progress);
                        imv.setImageBitmap(img1.img_actual);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });

        final Button bt_contrastDE = findViewById(R.id.contrastDE);
        bt_contrastDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.contrastDE();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final Button bt_contrastHE = findViewById(R.id.contrastHE);
        bt_contrastHE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.contrastHE();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final Button bt_convolve = findViewById(R.id.convolve);
        bt_convolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] mask =
                        {
                                {10, 20, 30, 20, 10},
                                {20, 60, 80, 60, 20},
                                {30, 80, 100, 80, 30},
                                {20, 60, 80, 60, 20},
                                {10, 20, 30, 20, 10}
                        };
                img1.convolve(mask);
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final ImageButton gallery = findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_IMAGE);
            }
        });

        Button menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibility){
                    bt_default.setVisibility(View.GONE);
                    bt_gray.setVisibility(View.GONE);
                    bt_grayRS.setVisibility(View.GONE);
                    bt_colorize.setVisibility(View.GONE);
                    bt_contrastDE.setVisibility(View.GONE);
                    bt_contrastHE.setVisibility(View.GONE);
                    bt_convolve.setVisibility(View.GONE);
                    gallery.setVisibility(View.GONE);

                    tv.setVisibility(View.GONE);
                }
                else {

                    bt_default.setVisibility(View.VISIBLE);
                    bt_gray.setVisibility(View.VISIBLE);
                    bt_grayRS.setVisibility(View.VISIBLE);
                    bt_colorize.setVisibility(View.VISIBLE);
                    bt_contrastDE.setVisibility(View.VISIBLE);
                    bt_contrastHE.setVisibility(View.VISIBLE);
                    bt_convolve.setVisibility(View.VISIBLE);
                    gallery.setVisibility(View.VISIBLE);

                    tv.setVisibility(View.VISIBLE);

                }

                visibility = !visibility;
            }
        });

        tv.setText(img1.toString());
    }

    Integer SELECT_IMAGE = 1;
    Integer TAKE_PHOTO = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            Uri picturePath = data.getData();
            InputStream pictureInputStream;
            opts.inMutable = true;
            opts.inScaled = false;
            try {
                pictureInputStream = getContentResolver().openInputStream(picturePath);
                img1 = new ImageEditor(BitmapFactory.decodeStream(pictureInputStream,null,opts));
                imv.setImageBitmap(img1.img_actual);
                tv.setText(img1.toString());
                imv.setRotation(90);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        if(requestCode == TAKE_PHOTO) {
            img1 = new ImageEditor((Bitmap) data.getExtras().get("data"));
            imv.setImageBitmap(img1.img_actual);
            tv.setText(img1.toString());
        }

    }

}
