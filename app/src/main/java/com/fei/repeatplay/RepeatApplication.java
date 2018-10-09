package com.fei.repeatplay;

import android.app.Application;

public class RepeatApplication extends Application {
    private static RepeatApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static RepeatApplication getApp(){
        return app;
    }
}
