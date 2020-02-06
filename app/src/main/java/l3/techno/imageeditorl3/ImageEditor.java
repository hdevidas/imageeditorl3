package l3.techno.imageeditorl3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import java.util.Random;

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
        this.img_backup = img.copy(img.getConfig(),true);
        this.img_actual = img;
        w = img.getWidth();
        h = img.getHeight();
        pixels = new int[w * h];


        //imv.setImageBitmap(img);
    }

    public void defaultimg(){
        //TODO
    }

    /**
     * Reset the bitmap.
     */
    public void defaultImg(){
        img_backup.getPixels(pixels, 0, w, 0, 0, w, h);
        img_actual.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    /**
     * Put the bitmap in gray.
     */
    public void toGray(){
        int red;
        int green;
        int blue;
        int grey;
        img_actual.getPixels(pixels,0,w,0,0,w,h);
        for (int i=0; i<pixels.length;i++){
            red= Color.red(pixels[i]);
            green=Color.green(pixels[i]);
            blue=Color.blue(pixels[i]);
            grey = (int) (red*0.3)+(int) (green*0.59)+(int) (blue*0.11);
            pixels[i]=Color.rgb(grey,grey,grey);
        }
        img_actual.setPixels(pixels,0,w,0,0,w,h);
    }


    //CONVERTIE L'IMAGE EN NIVEAU DE GRIS RENDERSCRIPT
    /*public void toGrayRS(){
        RenderScript rs = RenderScript.create(this);
        Allocation input = Allocation.createFromBitmap(rs , this.img_actual);
        Allocation  output= Allocation.createTyped(rs , input. getType ());

        ScriptC_gray  grayScript = new  ScriptC_gray(rs);
        grayScript.forEach_toGray(input , output);

        output.copyTo(this.img_actual);
        input.destroy (); output.destroy ();
        grayScript.destroy (); rs.destroy ();
    }*/

    private float max(float a, float b, float c){
        return Math.max(a,Math.max(b,c));
    }
    private float min(float a, float b, float c){
        return Math.min(a,Math.min(b,c));
    }


    private float[] rgbToHsv(int red, int green, int blue){
        float[] hsv=new float[3];
        float H,S,V;

        float red2=red/255.f;
        float green2=green/255.f;
        float blue2=blue/255.f;

        float max=max(red2,green2,blue2);
        float min=min(red2,green2,blue2);
        float Delta = max-min;

        if (max==red2){
            H=60*(((green2-blue2)/Delta) % 6);
        }
        else {
            if(max==green2){
                H=60*(((blue2-red2)/Delta) +2);
            }
            else{
                if(max==blue2){
                    H=60*(((red2-green2)/Delta)+4);
                }
                else{
                    H=0;
                }
            }
        }
        hsv[0]=H;
        if(max==0){
            S=0;
        }
        else{
            S=Delta/max;
        }
        hsv[1]=S;

        V=max;
        hsv[2]=V;

        return hsv;
    }

    private int hsvToRgb(float[] hsv){
        int r, g, b;
        float C, X, m;
        float r2=0; float g2=0; float b2=0;
        float H=hsv[0];
        float S=hsv[1];
        float V=hsv[2];

        C=V*S;
        X=C*(1-Math.abs(((H/60)%2)-1));
        m=V-C;

        if(H>=0 && H<60){
            r2=C;
            g2=X;
            b2=0;
        }
        else{
            if (H>=60 && H<120){
                r2=X;
                g2=C;
                b2=0;
            }
            else{
                if(H>=120 && H<180){
                    r2=0;
                    g2=C;
                    b2=X;
                }
                else{
                    if(H>=180 && H<240){
                        r2=0;
                        g2=X;
                        b2=C;
                    }
                    else{
                        if(H>=240 && H<300){
                            r2=X;
                            g2=0;
                            b2=C;
                        }
                        else{
                            if(H>=300 && H<360){
                                r2=C;
                                g2=0;
                                b2=X;
                            }
                        }
                    }
                }
            }
        }
        r = (int)((r2+m)*255);
        g = (int)((g2+m)*255);
        b = (int)((b2+m)*255);
        return Color.rgb(r,g,b);
    }

    /**
     * Change the bitmap hue in a random way.
     *
     */
    public void colorize(){
        int r, g, b;
        Random rdt = new Random();
        float hue = (float) rdt.nextInt(360);

        img_actual.getPixels(pixels,0,w,0,0,w,h);

        for (int i=0; i<pixels.length;i++) {
            r = Color.red(pixels[i]);
            g = Color.green(pixels[i]);
            b = Color.blue(pixels[i]);
            float[] hsv=rgbToHsv(r,g,b);
            hsv[0]= hue;
            pixels[i]=hsvToRgb(hsv);
        }
        img_actual.setPixels(pixels,0,w,0,0,w,h);
    }

}
