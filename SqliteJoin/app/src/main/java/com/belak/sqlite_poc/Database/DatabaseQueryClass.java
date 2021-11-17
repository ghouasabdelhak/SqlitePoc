package com.belak.sqlite_poc.Database;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


}