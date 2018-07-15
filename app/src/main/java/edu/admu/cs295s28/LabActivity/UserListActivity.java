package edu.admu.cs295s28.LabActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity(R.layout.activity_user_list)
public class UserListActivity extends AppCompatActivity {
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;
    //ArrayList<User> users;
    RealmResults<User> users;
//    Gson gson;
//    Type listType;
    ListAdapter adapter;
    Realm realm;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    public void init(){
        //make our adapter from our list
//        SharedPreferences prefs = getSharedPreferences("Lab",MODE_PRIVATE);
//        editor = prefs.edit();

//        gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonUsers = prefs.getString("Users", "[]");

        //System.out.println(jsonUsers);

        //listType = new TypeToken<ArrayList<User>>(){}.getType();
        //users = gson.fromJson(jsonUsers,listType);
        realm = Realm.getDefaultInstance();
        users = realm.where(User.class).findAll();

        //adapter = new ListAdapter(this, users, gson, editor);
        adapter = new ListAdapter(this, users);

        //put adapter to list
        list.setAdapter(adapter);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        init();
//    }

    @Click(R.id.btnGoRegister)
    public void launchRegister()
    {
        RegisterActivity_.intent(this).startForResult(1);
    }

    //requestCode
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if(requestCode == 1) {
            if(responseCode == RESULT_OK) {
                if(data != null){
                    User newUser = (User) data.getSerializableExtra("newUser");
                    addUser(newUser);
                    //init();
                    adapter.notifyDataSetChanged();
                }
            }
        }else if(requestCode == 2){
            if(responseCode == RESULT_OK) {
                if(data != null) {
                    String oldUserName = (String) data.getSerializableExtra("oldUser");
                    User editUser = (User) data.getSerializableExtra("editUser");

                    User oldUser = realm.where(User.class)
                            .equalTo("userName", oldUserName)
                            .findFirst();
                    if(oldUser != null) {
                        if(updateUser(oldUser, editUser)) {
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No changes made.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(this, "Cannot match records",Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        }
    }

    private boolean updateUser(User oldUser, User newUser){
        String j;
        boolean success = false;

        User u = realm.where(User.class)
                .equalTo("userName", oldUser.getUserName())
                .findFirst();

        if(u != null){
            realm.beginTransaction();
            u.setUserName(newUser.getUserName());
            u.setName(newUser.getName());
            u.setPassword(newUser.getPassword());
            u.setImgPath(newUser.getImgPath());
            realm.commitTransaction();
            success = true;
            return success;
        }
//        for(User u:users){
//            if(u.getUserName().equals(oldUser.getUserName())){
//                u.setUserName(newUser.getUserName());
//                u.setName(newUser.getName());
//                u.setPassword(newUser.getPassword());
//                u.setImgPath(newUser.getImgPath());
//                success = true;
//                j = gson.toJson(users);
//                editor.putString("Users", j);
//                editor.apply();
//                return success;
//            }
//        }
        return success;
    }

    private boolean addUser(User user){
        String j;
        boolean success = true;

        for(User u:users){
            if(u.getUserName().equals(user.getUserName())){
                success = false;
                return success;
            }
        }

//        if(getUsers().equals("")) {
//            j = new JsonObject();
//        }else {
//            j = new JsonParser().parse(getUsers()).getAsJsonObject();
//            if(j.get(key) != null) {
//                success = false;
//                return success;
//            }
//        }

        //j.addProperty(key, jsonObject.toString());

        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
        //users.add(user);
        //j = gson.toJson(users);
        //editor.putString("Users", j);
        //editor.apply();
        return success;
    }

//    @Click(R.id.list)
//    private void selectUser(){
//        list.getSelectedItem();
//    }
}
