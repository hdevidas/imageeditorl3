package l3.techno.imageeditorl3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button neew;
    private Button load;

    private MainActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity=this;

        load = findViewById(R.id.buttonLoad);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustumPopup custumPopup = new CustumPopup(activity);

                custumPopup.getGallerie().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editorActivity = new Intent(getApplicationContext(),EditorActivity.class);
                        startActivity(editorActivity);
                        finish();
                    }
                });
                custumPopup.getPhoto().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editorActivity = new Intent(getApplicationContext(),EditorActivity.class);
                        startActivity(editorActivity);
                        finish();
                    }
                });

                custumPopup.build();
            }
        });

        neew = findViewById(R.id.buttonNew);
        neew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editorActivity = new Intent(getApplicationContext(),EditorActivity.class);
                startActivity(editorActivity);
                finish();
            }
        });
    }
}
