package edu.admu.cs295s28.LabActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtView;
    Button btnClick;
    EditText txtEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtView = (TextView) findViewById(R.id.textView);
        txtView.setText("Goodbye Cruel World!");

        btnClick  = (Button) findViewById(R.id.button);
        btnClick.setText("Click Me");

        txtEdit = (EditText) findViewById(R.id.editText);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMe();
            }
        });
    }

    public void clickMe() {
        String text = txtEdit.getText().toString();
        txtView.setText(text);

    }

    public void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    public void onStart() {
        super.onStart();
        System.out.println("onStart!");
    }
}
