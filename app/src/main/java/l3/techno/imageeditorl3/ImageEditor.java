package l3.techno.imageeditorl3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static l3.techno.imageeditorl3.EditorActivity.imv;

public class ImageEditor extends AppCompatActivity {

    //Attributs
    public Bitmap img_backup;
    public Bitmap img_actual;
    int w;
    int h;
    private int[] pixels;


    Img image;
    //Dimensions ??

    //Constructeur
    public ImageEditor(Bitmap img){
        this.img_backup = img.copy(img.getConfig(),true);
        this.img_actual = img.copy(img.getConfig(),true);
        w = img.getWidth();
        h = img.getHeight();
        pixels = new int[w * h];
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

    public int max_r(int[] colors){
        int m=Color.red(colors[0]);
        int c_r;
        for(int c:colors){
            c_r=Color.red(c);
            if(m<c_r){
                m=c_r;
            }
        }
        return m;
    }

    public int max_g(int[] colors){
        int m=Color.green(colors[0]);
        int c_g;
        for(int c:colors){
            c_g=Color.green(c);
            if(m<c_g){
                m=c_g;
            }
        }
        return m;
    }

    public int max_b(int[] colors){
        int m=Color.blue(colors[0]);
        int c_b;
        for(int c:colors){
            c_b=Color.blue(c);
            if(m<c_b){
                m=c_b;
            }
        }
        return m;
    }

    public int min_r(int[] colors){
        int m=Color.red(colors[0]);
        int c_r;
        for(int c:colors){
            c_r=Color.red(c);
            if(m>c_r){
                m=c_r;
            }
        }
        return m;
    }

    public int min_g(int[] colors){
        int m=Color.green(colors[0]);
        int c_g;
        for(int c:colors){
            c_g=Color.green(c);
            if(m>c_g){
                m=c_g;
            }
        }
        return m;
    }

    public int min_b(int[] colors){
        int m=Color.blue(colors[0]);
        int c_b;
        for(int c:colors){
            c_b=Color.blue(c);
            if(m>c_b){
                m=c_b;
            }
        }
        return m;
    }

    /**
     *
     * Increase the contrast of the bitmap (with the dynamic extension method).
     *
     */
    public void contrastDE(){ //redondance de code pour la LUT
        int[] LUTr=new int[256];
        int[] LUTg=new int[256];
        int[] LUTb=new int[256];

        int min_r=min_r(pixels);
        int max_r=max_r(pixels);

        int min_g=min_g(pixels);
        int max_g=max_g(pixels);

        int min_b=min_b(pixels);
        int max_b=max_b(pixels);

        for(int i=0;i<256;i++){
            LUTr[i]= (int)(255.f*(i-min_r))/(max_r-min_r); //traiter le cas où l'image est uniforme (max=min)
            LUTg[i]= (int)(255.f*(i-min_g))/(max_g-min_g);
            LUTb[i]= (int)(255.f*(i-min_b))/(max_b-min_b);
        }

        int r,g,b;
        for(int i=0;i<pixels.length;i++){
            r=Color.red(pixels[i]);
            g=Color.green(pixels[i]);
            b=Color.blue(pixels[i]);
            pixels[i]=Color.rgb(LUTr[r],LUTg[g],LUTb[b]);
        }

        img_actual.setPixels(pixels,0,w,0,0,w,h);
    }

    /**
     *
     * Increase the contrast of the bitmap (with the histogram equalization method).
     *
     */
    public void contrastHE() {
        int r, g, b;

        //indication : faire avec hsv et pas rgb
        int[] hist_r = new int[256]; //à corriger : faire attention au "int" pour les grosses valeurs
        int[] hist_g = new int[256];
        int[] hist_b = new int[256];

        for (int i = 0; i < 255; i++) {
            hist_r[i] = 0;
            hist_g[i] = 0;
            hist_b[i] = 0;

        }

        for (int i = 0; i < pixels.length; i++) {
            hist_r[Color.red(pixels[i])]++;
            hist_g[Color.green(pixels[i])]++;
            hist_b[Color.blue(pixels[i])]++;

        }

        for (int i = 1; i < 255; i++) {
            hist_r[i] += hist_r[i - 1];
            hist_g[i] += hist_g[i - 1];
            hist_b[i] += hist_b[i - 1];
        }
        for (int i = 0; i < pixels.length; i++) {
            if (Color.red(pixels[i]) != 255 && Color.green(pixels[i]) != 255 && Color.blue(pixels[i]) != 255) {
                r = (hist_r[Color.red(pixels[i])] * 254) / hist_r[254];
                g = (hist_r[Color.green(pixels[i])] * 254) / hist_r[254];
                b = (hist_r[Color.blue(pixels[i])] * 254) / hist_r[254];
            } else {
                r = 255;
                g = 255;
                b = 255;
            }
            pixels[i] = Color.rgb(r, g, b);
        }
        img_actual.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    /**
     *  Apply a convolution filter on the bitmap.
     *
     * @param mask Mask to apply on the bitmap
     */

    public void convolve(int[][] mask){
        int n = mask.length;

        int somme = 0;
        for(int i=0; i<n; i++ ){
            for (int j=0; j<n; j++){
                somme+=mask[i][j];
            }
        }
        int [] convolveColors = new int[w*h];

        img_actual.getPixels(pixels,0,w,0,0,w,h);

        int line = 0;
        float convolveValue_r = 0;
        float convolveValue_g = 0;
        float convolveValue_b = 0;

        for(int i=0;i<pixels.length;i+=w){
            for(int j=0;j<w;j++){
                if(j<(n-1)/2 || j>=w-((n-1)/2) || line<(n-1)/2 || line>=h-((n-1)/2)){
                    convolveColors[i+j]= Color.rgb(0,0,0);
                }
                else{
                    int row=0;
                    for(int k=i-w*((n-1)/2); k<=i+w*((n-1)/2); k+=w){
                        int column =0;
                        for(int l=j-((n-1)/2); l<=j+((n-1)/2); l++){
                            convolveValue_r += mask[row][column]*Color.red(pixels[k+l]);
                            convolveValue_g += mask[row][column]*Color.green(pixels[k+l]);
                            convolveValue_b += mask[row][column]*Color.blue(pixels[k+l]);
                            column++;
                        }
                        row++;
                    }
                    convolveColors[i+j]= Color.rgb((int) convolveValue_r/(somme),(int) convolveValue_g/(somme),(int) convolveValue_b/(somme));
                    convolveValue_r=0;
                    convolveValue_g=0;
                    convolveValue_b=0;
                }
            }
            line++;
        }
        img_actual.setPixels(convolveColors,0,w,0,0,w,h);
    }

    public String toString(){
        return w + " x "+ h;
    }

}
