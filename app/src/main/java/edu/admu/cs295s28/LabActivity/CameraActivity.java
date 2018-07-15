package edu.admu.cs295s28.LabActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@EActivity(R.layout.activity_camera)
public class CameraActivity extends AppCompatActivity {

    Picasso picasso;

    @ViewById
    ImageView imageView;

    @AfterViews
    public void init()
    {
        // check if savedImage.jpeg exists in path
            // load via picasso if exists

        picasso = Picasso.get();
        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, "savedImage.jpeg");

        if (savedImage.exists()) {
            refreshImageView(savedImage);
        }
    }

    @Click(R.id.button)
    public void selectPic()
    {
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
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    File savedImage = saveFile(jpeg);
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
//        FileInputStream fis = new FileInputStream(savedImage);
//        fis.read(jpeg);
        fos.write(jpeg);
        fos.close();
        //Snackbar.make().show;
        return savedImage;
    }

    private void refreshImageView(File savedImage) {
        picasso.load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }
}
