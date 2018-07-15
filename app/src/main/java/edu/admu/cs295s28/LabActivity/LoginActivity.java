package edu.admu.cs295s28.LabActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity{
    //SharedPreferences pref;
    //SharedPreferences.Editor editor;
    Realm realm;
    Gson gson;

    @ViewById(R.id.txtUsername)
    EditText txtUsername;

    @ViewById(R.id.txtPassword)
    EditText txtPassword;

    @ViewById(R.id.chkRemember)
    CheckBox chkRemember;

    @ViewById(R.id.btnSignIn)
    Button btnSignIn;

    /*@ViewById(R.id.btnRegister)
    Button btnRegister;*/

    @ViewById(R.id.btnCamera)
    Button btnCamera;

    @AfterViews
    public void init(){
        realm = Realm.getDefaultInstance();
        //pref = getSharedPreferences("Lab", MODE_PRIVATE);
        //editor = pref.edit();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Click(R.id.btnCamera)
    public void OpenCamera() {
        CameraActivity_.intent(this).start();
    }

    @Click(R.id.btnSignIn)
    public void SignIn(){
//        String isChecked = (chkRemember.isChecked() ? "checked" : "not checked");
//        String msg = "Username: " + txtUsername.getText().toString() + "\n" +
//                "Password: " + txtPassword.getText().toString() + "\n" +
//                "Remember Me: " + isChecked;
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        String curName="";
        //pref = getSharedPreferences("Lab", MODE_PRIVATE);

        //jsonUser = pref.getString("Users", jsonUser);
        try {
            //Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            //ArrayList<User> users = gson.fromJson(jsonUser, listType);
//            JSONObject jsonObject = new JSONObject(users);
//            JSONObject user =
//                    new JSONObject(jsonObject.getString(txtUsername.getText().toString()));
            //boolean found = false;
            //for(User user:users) {
            User user = realm.where(User.class)
                    .equalTo("userName", txtUsername.getText().toString())
                    .and()
                    .equalTo("password", txtPassword.getText().toString())
                    .findFirst();
//            if (user != null && user.getUserName().equals(txtUsername.getText().toString())
//                    && user.getPassword().equals(txtPassword.getText().toString())) {
            if (user != null) {
                //found = true;
                curName = user.getName();
                WelcomeActivity_.intent(this).userName(txtUsername.getText().toString())
                        .name(curName).start();

                if (chkRemember.isChecked()) {
                    realm.beginTransaction();
                    RememberMe rememberMe = realm.createObject(RememberMe.class);
//                        editor.putBoolean("Remember", chkRemember.isChecked());
//                        editor.putString("Username", txtUsername.getText().toString());
//                        editor.putString("Password", txtPassword.getText().toString());
//                        editor.apply();
                    rememberMe.setSavedUser(txtUsername.getText().toString());
                    rememberMe.setSavedPassword(txtPassword.getText().toString());
                    rememberMe.setRememberMe(chkRemember.isChecked());
                    realm.commitTransaction();
                } else {
                    RememberMe rememberMe = realm.where(RememberMe.class).findFirst();
                    if(rememberMe != null) {
                        realm.beginTransaction();
                        rememberMe.deleteFromRealm();
                        realm.commitTransaction();
                    }
//                        editor.putBoolean("Remember", chkRemember.isChecked());
//                        editor.putString("Username", "");
//                        editor.putString("Password", "");
//                        editor.apply();
                }
                finish();
                //break;
            } else{
                Toast.makeText(this, "Invalid Credentials"
                        , Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something wrong in getting records."
                    , Toast.LENGTH_LONG).show();
        }
    }

//    @Click(R.id.btnRegister)
//    public void Register(){
//        RegisterActivity_.intent(this).start();
//        finish();
//    }

    @Click(R.id.btnUsers)
    public void ListUsers() {
        UserListActivity_.intent(this).start();
    }

    public void onResume() {
        super.onResume();
        System.out.println("onResume");
//        String theUsername="";
//
//        pref = getSharedPreferences("Lab", MODE_PRIVATE);
//        theUsername = pref.getString("Username",theUsername);
//        txtUsername.setText(theUsername);
        String theUsername="", thePassword="";
        boolean rememberMe;

        RememberMe remMe = realm.where(RememberMe.class).findFirst();
        if(remMe != null) {
            theUsername = remMe.getSavedUser();
            thePassword = remMe.getSavedPassword();
            rememberMe = remMe.isRememberMe();
            txtUsername.setText(theUsername);
            txtPassword.setText(thePassword);
            chkRemember.setChecked(rememberMe);
        }
    }

    public void onStart() {
        super.onStart();
        System.out.println("onStart");
        String theUsername="", thePassword="";
        boolean rememberMe;

        RememberMe remMe = realm.where(RememberMe.class).findFirst();
        if(remMe != null) {
            theUsername = remMe.getSavedUser();
            thePassword = remMe.getSavedPassword();
            rememberMe = remMe.isRememberMe();
            txtUsername.setText(theUsername);
            txtPassword.setText(thePassword);
            chkRemember.setChecked(rememberMe);
        }
//        pref = getSharedPreferences("Lab", MODE_PRIVATE);
//        theUsername = pref.getString("Username",theUsername);
//        thePassword = pref.getString("Password", thePassword);
//        rememberMe = pref.getBoolean("Remember", false);
//        txtUsername.setText(theUsername);
//        txtPassword.setText(thePassword);
//        chkRemember.setChecked(rememberMe);
    }

    protected void onDestroy(){
        super.onDestroy();
        if(!realm.isClosed()) {
            realm.close();
        }
    }
}
