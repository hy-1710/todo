package com.hfad.joke.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by serpentcs on 12/7/16.
 */
public class Database extends SQLiteOpenHelper {


    private static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TASK (_ID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"TITLE TEXT,"
                +"DESCRIPTION TEXT)");

    }
    public static void insertTask(SQLiteDatabase db, String title, String description)

    {
        ContentValues values = new ContentValues();
        values.put("TITLE",title);
        values.put("DESCRIPTION",description);
        db.insert("TASK",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
