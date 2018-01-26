package com.example.jasmine.goalachieverassistant;

/**
 * Created by jasmine on 15/01/18.
 */

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GoalListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("mygoals.realm")
                .schemaVersion(0)
                //TODO: take out the following delete database migration line in production
                .deleteRealmIfMigrationNeeded()
     //           . migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        // Use the config
        Realm realm = Realm.getInstance(realmConfig);


        try {
            Realm.getDefaultInstance();
            //Realm file has been deleted.
        } catch (Exception ex){
            ex.printStackTrace();
            //No Realm file to remove.
        }


        //TODO take out stetho piece ouf for production
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        realm.close();
    }



}
