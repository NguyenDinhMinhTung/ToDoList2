package com.example.megas.todolist2.DAO;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.megas.todolist2.DTO.EventDTO;
import com.example.megas.todolist2.Database.Database;
import com.example.megas.todolist2._Date;

import java.util.ArrayList;
import java.util.List;

public class EventsDAO {
    SQLiteDatabase database;

    public EventsDAO(Context context) {
        Database data = new Database(context);
        database = data.Open();
    }

    public void addEvent(EventDTO eventDTO) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.TB_EVENTS_EVENTID, eventDTO.getEventId());
        contentValues.put(Database.TB_EVENTS_EVENNAME, eventDTO.getEventName());
        contentValues.put(Database.TB_EVENTS_TYPE, eventDTO.getType());
        contentValues.put(Database.TB_EVENTS_DAYTIME, eventDTO.getDaytime());
        contentValues.put(Database.TB_EVENTS_NOTIDAY, eventDTO.getNotiday());
        contentValues.put(Database.TB_EVENTS_STATUS, eventDTO.getStatus());
        contentValues.put(Database.TB_EVENTS_COLOR, eventDTO.getColor());
        contentValues.put(Database.TB_EVENTS_OBJECTID, eventDTO.getObjectId());
        contentValues.put(Database.TB_EVENTS_COMMENT, eventDTO.getComment());

        database.insert(Database.TB_EVENTS, null, contentValues);
    }

    public List<EventDTO> getListEvent() {
        List<EventDTO> eventDTOList = new ArrayList<>();

        String query = "SELECT * FROM " + Database.TB_EVENTS;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_ID));
            int evenId = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_EVENTID));
            int type = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_TYPE));
            int notiday = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_NOTIDAY));
            int status = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_STATUS));
            int color = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_COLOR));
            int objectId = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_OBJECTID));

            String comment = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_COMMENT));
            String daytime = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_DAYTIME));
            String evenName = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_EVENNAME));

            EventDTO eventDTO = new EventDTO(id, evenId, type, daytime, notiday, status, color, objectId, evenName, comment);

            eventDTOList.add(eventDTO);

            cursor.moveToNext();
        }

        return eventDTOList;
    }

    public List<EventDTO> getListEvent(_Date date) {
        List<EventDTO> eventDTOList = new ArrayList<>();

        String query = "SELECT * FROM " + Database.TB_EVENTS + " WHERE " + Database.TB_EVENTS_DAYTIME + ">=" + "'" + date.toString() + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_ID));
            int evenId = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_EVENTID));
            int type = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_TYPE));
            int notiday = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_NOTIDAY));
            int status = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_STATUS));
            int color = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_COLOR));
            int objectId = cursor.getInt(cursor.getColumnIndex(Database.TB_EVENTS_OBJECTID));

            String comment = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_COMMENT));
            String daytime = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_DAYTIME));
            String evenName = cursor.getString(cursor.getColumnIndex(Database.TB_EVENTS_EVENNAME));

            EventDTO eventDTO = new EventDTO(id, evenId, type, daytime, notiday, status, color, objectId, evenName, comment);

            eventDTOList.add(eventDTO);

            cursor.moveToNext();
        }

        return eventDTOList;
    }

    public void updateStatus(int id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.TB_EVENTS_STATUS, status);

        database.update(Database.TB_EVENTS, contentValues, Database.TB_EVENTS_ID + "=" + id, null);
    }

    public void update(EventDTO eventDTO){
        ContentValues contentValues=new ContentValues();

        contentValues.put(Database.TB_EVENTS_EVENTID,eventDTO.getEventId());
        contentValues.put(Database.TB_EVENTS_EVENNAME,eventDTO.getEventName());
        contentValues.put(Database.TB_EVENTS_TYPE,eventDTO.getType());
        contentValues.put(Database.TB_EVENTS_DAYTIME,eventDTO.getDaytime());
        contentValues.put(Database.TB_EVENTS_NOTIDAY,eventDTO.getNotiday());
        contentValues.put(Database.TB_EVENTS_COLOR,eventDTO.getColor());
        contentValues.put(Database.TB_EVENTS_OBJECTID,eventDTO.getObjectId());
        contentValues.put(Database.TB_EVENTS_COMMENT,eventDTO.getComment());
        //contentValues.put(Database.TB_EVENTS_STATUS,eventDTO.getStatus());

        database.update(Database.TB_EVENTS, contentValues, Database.TB_EVENTS_ID + "=" + eventDTO.getId(), null);
    }

    public void delete(int id) {
        database.beginTransaction();

        try {
            database.delete(Database.TB_EVENTS, "id=" + id, null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
