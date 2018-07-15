package edu.admu.cs295s28.LabActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListAdapter extends BaseAdapter {
    //ArrayList<User> list;
    Realm realm = Realm.getDefaultInstance();
    RealmResults<User> list;
    Activity activity;
    int lugar;
    //Gson gson;
    //SharedPreferences.Editor editor;
    User user;
    Picasso picasso = Picasso.get();
    File savedImage;
    ImageView imgPhoto;

//    public ListAdapter(Activity activity, ArrayList<User> users) {
//        super();
//        this.activity = activity;
//        list = users;
//    }
//    public ListAdapter(Activity activity, ArrayList<User> users
//        , Gson gson, SharedPreferences.Editor editor) {
//        super();
//        this.activity = activity;
//        list = users;
//        this.gson = gson;
//        this.editor = editor;
//    }

    public ListAdapter(Activity activity, RealmResults<User> users){
        super();
        list = users;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //make your view
        View view = null;
        if(convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.row_user,null);
        } else {
            view = convertView;
        }

        //get your data
        lugar = position;
        user = list.get(lugar);

        //fill in the view
        TextView lblName = view.findViewById(R.id.row_name);
        TextView lblUsername = view.findViewById(R.id.row_username);

        lblName.setText(user.getName());
        lblUsername.setText(user.getUserName());

        imgPhoto = view.findViewById(R.id.imgPhoto);

        File getImageDir = activity.getExternalCacheDir();
        File savedImage = new File(getImageDir, user.getUserName() + ".jpeg");
        if (savedImage.exists()) {
            refreshImageView(savedImage);
        } else {
            refreshImageView(R.mipmap.ic_launcher);
        }


        //set Buttons
        ImageButton btnEdit = view.findViewById(R.id.btnEdit);
        ImageButton btnDelete = view.findViewById(R.id.btnDelete);

        btnEdit.setTag(user);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User us = (User) v.getTag();
                //Toast.makeText(activity.getApplicationContext()
                //        ,"Editing is not yet possible.", Toast.LENGTH_SHORT).show();
                RegisterActivity_.intent(activity).extra("User", us.getUserName())
                        .extra("EditMode", true).startForResult(2);
            }
        });

        btnDelete.setTag(user);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = (User) v.getTag();
                File image = new File(u.getImgPath());
                if(image.exists()){
                    if(image.delete()) {
                        image = null;
                        u = null;
                    }
                }
                realm.beginTransaction();
                list.deleteFromRealm(lugar);
                realm.commitTransaction();
                //list.remove(v.getTag());

//                String j = gson.toJson(list);
//                editor.putString("Users", j);
//                editor.apply();
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void refreshImageView(File savedImage) {
        picasso.load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgPhoto);
    }

    private void refreshImageView(int resourceID) {
        picasso.load(resourceID)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgPhoto);
    }
}
