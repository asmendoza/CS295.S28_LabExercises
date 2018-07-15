package edu.admu.cs295s28.LabActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import io.realm.Realm;

//1035733203
@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    String strKulang = "All fields are required.";
    String strCancel = "Exiting Registration.";
    Picasso picasso = Picasso.get();
    File savedImage;
    byte[] jpeg;
    //SharedPreferences pref;
    //SharedPreferences.Editor editor;
    //JSONObject user;
    @Extra("User")
    String oldUserName;

    User oldUser;
    Realm realm;

    @Extra("EditMode")
    boolean editMode;

    User user;
    //ArrayList<User> users;
    //GsonBuilder gsonBuilder;
    //Gson gson;
    //Type listType;

    @ViewById(R.id.lblLabel)
    TextView lblLabel;

    @ViewById(R.id.imgRegPhoto)
    ImageView imgRegPhoto;

    @ViewById(R.id.txtRegName)
    EditText txtRegName;

    @ViewById(R.id.txtRegUsername)
    EditText txtRegUserName;

    @ViewById(R.id.txtRegPassword)
    EditText txtRegPassword;

    @ViewById(R.id.btnOK)
    Button btnOK;

    @ViewById(R.id.btnCancel)
    Button btnCancel;

    @AfterViews
    public void init(){
//        pref = getSharedPreferences("Lab",MODE_PRIVATE);
//        editor = pref.edit();
//        gsonBuilder = new GsonBuilder().setPrettyPrinting();
//        gson = gsonBuilder.create();
//        users = new ArrayList<>();
//        listType = new TypeToken<ArrayList<User>>(){}.getType();
//        users = gson.fromJson(pref.getString("Users","[]"), listType);
        realm = Realm.getDefaultInstance();
        if(editMode){
            oldUser = realm.where(User.class)
                    .equalTo("userName", oldUserName)
                    .findFirst();
            if(oldUser != null) {
                txtRegName.setText(oldUser.getName());
                txtRegUserName.setText(oldUser.getUserName());
                txtRegPassword.setText(oldUser.getPassword());

                if (oldUser.getImgPath().trim().length() > 0) {
                    try {
                        loadFile(oldUser.getImgPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this
                                , "Cannot load the image."
                                , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    refreshImageView(R.mipmap.ic_launcher);
                }
                lblLabel.setText("EDIT USER");
            }
        } else {
            refreshImageView(R.mipmap.ic_launcher);
        }
    }

    @Click(R.id.imgRegPhoto)
    public void CaptureImage(){
        ImageActivity_.intent(this).startForResult(0);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        if (requestCode==0)
        {
            if (responseCode==100)
            {
                // save rawImage to file savedImage.jpeg
                // load file via picasso
                jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    savedImage = saveFile(jpeg);
                    refreshImageView(savedImage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    @NonNull
    private File saveFile(byte[] jpeg) throws IOException {
        File getImageDir = getExternalCacheDir();

        File savedImage = new File(getImageDir, "savedImage.jpeg");

        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }
    @NonNull
    private File saveFile(byte[] jpeg, String fileName) throws IOException {
        File getImageDir = getExternalCacheDir();

        File savedImage = new File(getImageDir, fileName);

        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }

    private void loadFile(String fileName) throws IOException {
        savedImage = new File(fileName);
        System.out.println(fileName);
        picasso.load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgRegPhoto);
    }

    private void refreshImageView(File savedImage) {
        picasso.load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgRegPhoto);
    }

    private void refreshImageView(int resourceID) {
        picasso.load(resourceID)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgRegPhoto);
    }

    @Click(R.id.btnOK)
    public void Register(){
        //user = new JSONObject();
        user = new User();
        if(!txtRegName.getText().equals("")
                && !txtRegUserName.getText().equals("")
                && !txtRegPassword.getText().equals("")){

            try {
                user.setName(txtRegName.getText().toString());
                user.setUserName(txtRegUserName.getText().toString());
                user.setPassword(txtRegPassword.getText().toString());

                if(jpeg != null) {
                    savedImage = saveFile(jpeg, user.getUserName() + ".jpeg");
                }
                if(savedImage != null) {
                    user.setImgPath(savedImage.getAbsolutePath());
                }

                Intent i = new Intent();
                if(editMode){
                    i.putExtra("editUser", user);
                    i.putExtra("oldUser", oldUserName);
                } else {
                    i.putExtra("newUser",user);
                }
                setResult(RESULT_OK,i);
                finish();
//                if(addUser(user)) {
//                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show();
//                    printJSONObject();
//                    //LoginActivity_.intent(this).start();
//                    finish();
//                } else {
//                    Toast.makeText(this
//                            , txtRegUserName.getText().toString() + " already exists."
//                            , Toast.LENGTH_SHORT).show();
//                }
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Something is wrong when saving the JSON."
                        , Toast.LENGTH_SHORT).show();
                System.out.println(user.toString());
            }
        } else {
            Toast.makeText(this, strKulang, Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.btnCancel)
    public void CancelRegister(){
        Toast.makeText(this, strCancel, Toast.LENGTH_SHORT).show();
        LoginActivity_.intent(this).start();
        finish();
    }

//    private boolean addUser(User user){
//        String j;
//        boolean success = true;
//
//        for(User u:users){
//            if(u.getUserName().equals(user.getUserName())){
//                success = false;
//                return success;
//            }
//        }
//
////        if(getUsers().equals("")) {
////            j = new JsonObject();
////        }else {
////            j = new JsonParser().parse(getUsers()).getAsJsonObject();
////            if(j.get(key) != null) {
////                success = false;
////                return success;
////            }
////        }
//
//        //j.addProperty(key, jsonObject.toString());
//        users.add(user);
//        j = gson.toJson(users);
//        editor.putString("Users", j);
//        editor.apply();
//        return success;
//    }
//
//    private boolean addUser(String key, JSONObject jsonObject){
//        JsonObject j;
//        boolean success = true;
//
//        if(getUsers().equals("")) {
//            j = new JsonObject();
//        }else {
//            j = new JsonParser().parse(getUsers()).getAsJsonObject();
//            if(j.get(key) != null) {
//                success = false;
//                return success;
//            }
//        }
//
//        j.addProperty(key, jsonObject.toString());
//        editor.putString("Users", j.toString());
//        editor.apply();
//        return success;
//    }
//
//    private String getUsers(){
//        return pref.getString("Users", "[]");
//    }
//
//    private void printJSONObject(){
//        System.out.println(pref.getAll().toString());
//    }

}
