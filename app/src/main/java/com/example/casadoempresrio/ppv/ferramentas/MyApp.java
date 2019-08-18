package com.example.casadoempresrio.ppv.ferramentas;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.realm.Realm;

public class MyApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Log.i("MY_REALM", "Realm.init() executado");

        context = getApplicationContext();

        RealmControl.pupularRealm();
        //RealmControl.limpaRealm();
    }

    public static Context getAppContext(){
        return context;
    }
}
