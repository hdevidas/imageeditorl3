package l3.techno.imageeditorl3;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;

public class CustumPopup extends Dialog {

    private Button photo, gallerie;

    public CustumPopup(Activity activity){
        super(activity,R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_template);
        this.gallerie = findViewById(R.id.gallerie);
        this.photo = findViewById(R.id.photo);
    }

    public Button getPhoto(){return photo;}

    public Button getGallerie(){return gallerie;}

    public void build(){
        show();
    }
}
