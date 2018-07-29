package com.kiriloff.max.imdb;


import com.kiriloff.max.imdb.di.component.DaggerAppComponent;
import com.kiriloff.max.newsaggregator.di.module.AppContextModule;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent
                .builder()
                .setAppContextModule(new AppContextModule(getApplicationContext()))
                .build();
    }
}

