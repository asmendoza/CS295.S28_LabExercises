package edu.admu.cs295s28.LabActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmActivity extends AppCompatActivity {

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);

        realm = Realm.getDefaultInstance();

        if(realm.where(RealmData.class).count()==0) {
            System.out.println("CREATING");


            //save data to realm
            realm.beginTransaction();
            //unmanaged data
            RealmData realmData = new RealmData();
            realmData.setId(UUID.randomUUID().toString());
            realmData.setName("Aris Gail Mendoza");
            realmData.setAge(31);
            realmData.setEmail("aris.mendoza@gmail.com");

            realm.copyToRealm(realmData);

            //managed data
            RealmData anotherRealmData = realm.createObject(RealmData.class);
            anotherRealmData.setId(UUID.randomUUID().toString());
            anotherRealmData.setName("Gail Mendoza");
            anotherRealmData.setAge(31);
            anotherRealmData.setEmail("gail.mendoza@gmail.com");

            realm.commitTransaction();
        } else {
            System.out.println("Loading");
            RealmData getData = realm.where(RealmData.class).findFirst();

            System.out.println(getData);

            RealmResults<RealmData> results = realm.where(RealmData.class).findAll();

            for (RealmData rd:results){
                System.out.println(rd);
            }
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(!realm.isClosed()){
            System.out.println("DESTROYED...");
            realm.close();
        }
    }
}
