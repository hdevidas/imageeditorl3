package l3.techno.imageeditorl3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.IOException;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

import static android.widget.Toast.*;


public class MainActivity extends AppCompatActivity {
    static Bitmap img;
    BitmapFactory.Options opts = new BitmapFactory.Options();
    private String photoPath = null;
    static PhotoView imv;
    SeekBar seekbar;
    LinearLayout filter;

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

        //img1.defaultImg(); // Certaine fonctions ne fonctionnent pas si on
        imv.setImageBitmap(img1.img_actual);


        // ----------------------BOUTONS---------------------

        //Bouton default (Remettre l'image par défaut)
        final Button bt_default = findViewById(R.id.bt_default);
        bt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(View.GONE);
                seekbar.setVisibility(View.GONE);
                img1.defaultImg();
                img1.saveImg();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final ImageButton bt_save = findViewById(R.id.save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.saveImg();
                Toast toast = Toast.makeText(getApplicationContext(),"Your changes have been saved", LENGTH_SHORT);
                toast.show();
            }
        });



        //Bouton Gray v2 (codé avec getpixels)
        final Button bt_gray = findViewById(R.id.bt_gray);
        bt_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                img1.toGray();
                imv.setImageBitmap(img1.img_actual);
            }
        });

        //Bouton Gray v3 (codé avec renderscript) #3
        final Button bt_grayRS = findViewById(R.id.bt_grayRS);
        bt_grayRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                toGrayRS(img1.img_actual);
                imv.setImageBitmap(img1.img_actual);
            }
        });

        final Button bt_colorize = findViewById(R.id.colorize);
        bt_colorize.setOnClickListener(new SeekBar.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setMax(359);
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        img1.useSavedImg();
                        img1.colorize(progress);
                        img1.hue=progress;
                        imv.setImageBitmap(img1.img_actual);
                        tv.setText("..." + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seekbar.setProgress(img1.hue);
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

        final Button bt_brightness = findViewById(R.id.brightness);
        bt_brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(View.GONE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setMax(500);
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        img1.useSavedImg();
                        img1.brightness(progress-250);
                        img1.brightness = progress;

                        imv.setImageBitmap(img1.img_actual);
                        tv.setText("..." + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seekbar.setProgress(img1.brightness);
            }
        });


        final Button bt_colorSaver = findViewById(R.id.colorSaver);
        bt_colorSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(View.GONE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setMax(320);
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        img1.useSavedImg();
                        img1.colorSaver(progress);

                        imv.setImageBitmap(img1.img_actual);
                        tv.setText("..." + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seekbar.setProgress(img1.brightness);
            }
        });

        final Button bt_contrastHE = findViewById(R.id.contrastHE);
        bt_contrastHE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                img1.contrastHE();
                imv.setImageBitmap(img1.img_actual);
            }
        });



        final Button bt_convolve = findViewById(R.id.convolve);
        final Button bt_Gauss = findViewById(R.id.gaussien);
        final Button bt_average = findViewById(R.id.moyenneur);
        final Button bt_sobel = findViewById(R.id.sobel);
        filter= findViewById(R.id.filter);
        bt_convolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                filter.setVisibility(View.VISIBLE);
            }
        });

        bt_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] mask =
                        {
                                {1, 1, 1, 1, 1},
                                {1, 1, 1, 1, 1},
                                {1, 1, 1, 1, 1},
                                {1, 1, 1, 1, 1},
                                {1, 1, 1, 1, 1}
                        };
                img1.convolve(mask);
                imv.setImageBitmap(img1.img_actual);
            }
        });
        bt_Gauss.setOnClickListener(new View.OnClickListener() {
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
        bt_sobel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] mask =
                        {
                                {1, 0, 1},
                                {2, 0, 2},
                                {1, 0, 1}
                        };
                img1.convolve(mask);
                imv.setImageBitmap(img1.img_actual);
            }
        });





        final ImageButton gallery = findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_IMAGE);
            }
        });

        final ImageButton photo = findViewById((R.id.photo));
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setVisibility(View.GONE);
                TakePictureIntent();
            }
        });

        final ImageButton captureImg = findViewById(R.id.captureImg);
        captureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaStore.Images.Media.insertImage(getContentResolver(), img1.img_actual, "nom image", "description");

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
                    bt_colorSaver.setVisibility(View.GONE);
                    bt_contrastDE.setVisibility(View.GONE);
                    bt_contrastHE.setVisibility(View.GONE);
                    bt_convolve.setVisibility(View.GONE);
                    bt_brightness.setVisibility(View.GONE);
                    bt_save.setVisibility(View.GONE);
                    gallery.setVisibility(View.GONE);
                    photo.setVisibility(View.GONE);

                    filter.setVisibility(View.GONE);

                    seekbar.setVisibility(View.GONE);

                    tv.setVisibility(View.GONE);
                }
                else {

                    bt_default.setVisibility(View.VISIBLE);
                    bt_gray.setVisibility(View.VISIBLE);
                    bt_grayRS.setVisibility(View.VISIBLE);
                    bt_colorize.setVisibility(View.VISIBLE);
                    bt_colorSaver.setVisibility(View.VISIBLE);
                    bt_contrastDE.setVisibility(View.VISIBLE);
                    bt_contrastHE.setVisibility(View.VISIBLE);
                    bt_convolve.setVisibility(View.VISIBLE);
                    bt_brightness.setVisibility(View.VISIBLE);
                    bt_save.setVisibility(View.VISIBLE);
                    gallery.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.VISIBLE);


                    tv.setVisibility(View.VISIBLE);

                }

                visibility = !visibility;
            }
        });

        tv.setText(img1.toString());
    }

    Integer SELECT_IMAGE = 1;
    Integer TAKE_PHOTO = 2;
    Integer CAPTURE = 3;


    Uri photoURI;
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void TakePictureIntent(){
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent2.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent2, TAKE_PHOTO);
            }
        }
    }








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

                //img1.img_actual.setRotation(90);
                imv.setImageBitmap(img1.img_actual);
                tv.setText(img1.toString());
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        if(requestCode == TAKE_PHOTO) {
            try {
                img1 = new ImageEditor(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI));
                imv.setImageBitmap(img1.img_actual);
                tv.setText(img1.toString());
            }catch (IOException e){
                e.printStackTrace();
            }
        }



    }
}
