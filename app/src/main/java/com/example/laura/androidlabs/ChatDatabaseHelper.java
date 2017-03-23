package com.example.laura.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Chats.db";
    public static final String TABLE_NAME = "ChatMessage";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";
    private static int VERSION_NUM = 1;
    public static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE " + TABLE_NAME + " ("
                            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + KEY_MESSAGE + " text);";
    public static final String DROP_TABLE_MESSAGE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, TABLE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("ChatDataBaseHelper", "Calling onCreate "+CREATE_TABLE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL(DROP_TABLE_MESSAGE);
        onCreate(db);
        Log.i("ChatDataBaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer +" "+ DROP_TABLE_MESSAGE);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL(DROP_TABLE_MESSAGE);
        onCreate(db);
        Log.i("ChatDataBaseHelper", "Calling onDowngrade, oldVersion=" + oldVer + " newVersion=" + newVer +" "+ DROP_TABLE_MESSAGE);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        Log.i("ChatDataBaseHelper", "Calling onOpen ");

    }

}
