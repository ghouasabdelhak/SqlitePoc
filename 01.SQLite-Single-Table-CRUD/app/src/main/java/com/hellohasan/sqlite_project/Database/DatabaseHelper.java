package com.hellohasan.sqlite_project.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hellohasan.sqlite_project.Util.CarConnectedServicesDAO;
import com.hellohasan.sqlite_project.Util.Config;
import com.hellohasan.sqlite_project.Util.OfferConnectedServiceBO;
import com.hellohasan.sqlite_project.Util.PriceConnectedServiceBO;
import com.hellohasan.sqlite_project.Util.PriceOfferConnectedServiceDAO;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.psa.bouser.mym.dao.OfferConnectedServiceDAO;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    private AtomicInteger mOpenCounter = new AtomicInteger();    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private SQLiteDatabase mDatabase;
    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        OfferConnectedServiceDAO.Companion.createTable(db);
        PriceOfferConnectedServiceDAO.Companion.createTable(db);
        CarConnectedServicesDAO.Companion.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Create tables again
        onCreate(db);
    }

    /**
     * Open the database if it was not opened previously before, else return the writable database instance.
     *
     * @return a writable instance of the database
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            try {
                mDatabase = databaseHelper.getWritableDatabase();
            } catch (SQLiteException e) {
                Log.e("Database",e.getMessage());
            }
        }
        if (mDatabase == null) {
    }
        return mDatabase;
    }

    /**
     * Close the previously opened database connection.
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
//            mDatabase.close();
        }
    }
}
