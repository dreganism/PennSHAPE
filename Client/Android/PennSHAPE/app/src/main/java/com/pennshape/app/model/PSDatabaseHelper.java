package com.pennshape.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by hasun on 11/17/15.
 */
public class PSDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ps_sql.db";
    private static final String TABLE_NAME = "ps_messages";
    private static final int DATABASE_VERSION = 1;
    public static final String COL_ID = "id";
    public static final String COL_UID = "uid";
    public static final String COL_TIME = "time";
    public static final String COL_GROUP_ID = "groupid";
    public static final String COL_MSG = "msg";
    public static final String COL_EXTRA = "extra";

    public PSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStat = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_UID + " TEXT, " +
                COL_TIME + " INTEGER, " +
                COL_GROUP_ID + " INTEGER, " +
                COL_MSG + " TEXT, " +
                COL_EXTRA + " TEXT" +
                ")";
        try {
            db.execSQL(sqlStat);
        }catch (android.database.SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }

    public Cursor getDataTotal(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStat = "select * from " + TABLE_NAME +
                " ORDER BY " + COL_ID +" DESC LIMIT " + limit;
        return db.rawQuery(sqlStat, null);
    }

    public Cursor getLast() {
        return getDataTotal(1);
    }

    public void insertValues(ContentValues contentValues) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.insertOrThrow(TABLE_NAME, null, contentValues);
        }catch (android.database.SQLException e){
            e.printStackTrace();
        }
    }
}
