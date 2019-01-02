package com.example.megas.todolist2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String TB_SYNCQUEUES = "SYNCQUEUES";
    public static final String TB_EVENTS = "EVENTS";

    public static final String TB_SYNCQUEUES_ID = "id";
    public static final String TB_SYNCQUEUES_EVENTID = "eventid";
    public static final String TB_SYNCQUEUES_STATUS = "status";

    public static final String TB_EVENTS_ID = "id";
    public static final String TB_EVENTS_EVENTID = "eventid";
    public static final String TB_EVENTS_EVENNAME = "evenname";
    public static final String TB_EVENTS_TYPE = "type";
    public static final String TB_EVENTS_DAYTIME = "daytime";
    public static final String TB_EVENTS_NOTIDAY = "notiday";
    public static final String TB_EVENTS_STATUS = "status";
    public static final String TB_EVENTS_COLOR = "color";
    public static final String TB_EVENTS_OBJECTID = "OBJECTID";
    public static final String TB_EVENTS_COMMENT = "COMMENT";

    public Database(Context context) {
        super(context, "TODOLIST", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbEVENTS = "CREATE TABLE " + TB_EVENTS + " (" + TB_EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TB_EVENTS_EVENTID + " INTEGER NOT NULL, " + TB_EVENTS_EVENNAME + " TEXT NOT NULL,"
                + TB_EVENTS_TYPE + " INTEGER NOT NULL, " + TB_EVENTS_DAYTIME + " TEXT NOT NULL," + TB_EVENTS_NOTIDAY + " INTEGER NOT NULL, " + TB_EVENTS_STATUS + " INTEGER NOT NULL, " + TB_EVENTS_COLOR +
                " INTEGER NOT NULL," + TB_EVENTS_OBJECTID + " INTEGER, " + TB_EVENTS_COMMENT + " TEXT)";

        String tbSYNCQUEUES = "CREATE TABLE " + TB_SYNCQUEUES + "(" + TB_SYNCQUEUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TB_SYNCQUEUES_EVENTID + " INTEGER NOT NULL, " + TB_SYNCQUEUES_STATUS + " INTEGER NOT NULL)";

        db.execSQL(tbEVENTS);
        db.execSQL(tbSYNCQUEUES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase Open(){
        return this.getWritableDatabase();
    }
}
