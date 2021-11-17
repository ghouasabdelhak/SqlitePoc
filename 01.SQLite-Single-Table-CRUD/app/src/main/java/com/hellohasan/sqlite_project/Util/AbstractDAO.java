package com.hellohasan.sqlite_project.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.hellohasan.sqlite_project.Database.DatabaseHelper;


/**
 * Base class for DAO
 */
public abstract class AbstractDAO {

    /**
     * Writable database working with DAO
     */
    protected SQLiteDatabase database;

    /**
     * Current context
     */
    protected Context context;


    /**
     * CONSTRUCTOR
     */
    public AbstractDAO(Context ctx, SQLiteDatabase db) {
        this.context = ctx;
        this.database = db;
    }

    public AbstractDAO(Context ctx) {
        this.context = ctx;
    }


    /**
     *  METHODS
     *
     */


    /**
     * Open the database (get the writableDatabase)
     */
    protected void openDatabase() {

        if (this.context != null)
            this.database = DatabaseHelper.getInstance(this.context).openDatabase();
    }

    /**
     * Close the database
     */
    protected void closeDatabase() {

        if (this.context != null)
            DatabaseHelper.getInstance(this.context).closeDatabase();
    }

    /**
     * Execute SQL request
     *
     * @param sqlRequest
     */
    protected void execSQL(String sqlRequest) {
        if (this.database != null)
            this.database.execSQL(sqlRequest);
    }

    /**
     * Execute SQL Request with args
     *
     * @param sqlRequest
     * @param bindArgs
     */
    protected void execSQL(String sqlRequest, Object[] bindArgs) {
        if (this.database != null)
            this.database.execSQL(sqlRequest, bindArgs);
    }

    public static boolean isTableExists(@NonNull SQLiteDatabase database,  String tableName) {
        if (!database.isOpen()) {
            return false;
        }
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

}
