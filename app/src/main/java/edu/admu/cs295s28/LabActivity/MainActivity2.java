package edu.admu.cs295s28.LabActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity2 extends AppCompatActivity {

    @ViewById(R.id.textView)
    TextView txtView;
    @ViewById(R.id.button)
    Button btnClick;
    @ViewById(R.id.editText)
    EditText txtEdit;


    ArrayList<User> userList = new ArrayList<>();

    @Click(R.id.button)
    public void clickMe() {
//        String text = txtEdit.getText().toString();
//        txtView.setText(text);

//        StringBuilder sb = new StringBuilder();
//        sb.append(txtEdit.getText().toString());
//        String s = sb.toString();

//        Intent intent = new Intent(this, WelcomeActivity_.class);
//        intent.putExtra("data", s);
//        startActivity(intent);
//        WelcomeActivity_.intent(this).name(s)
//                .userName(s).start();

        User u = new User();
        u.setName("XXX");
        u.setPassword("NONE");
        u.setUserName("XXX");
        userList.add(u);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userList);
        System.out.println(json);

        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> userList2 = gson.fromJson(json, listType);

        System.out.println(userList2);
    }

    @AfterViews
    public void init(){
        txtView.setText("Goodbye cruel world!");
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
