package com.example.megas.todolist2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.megas.todolist2.DTO.SyncQueueDTO;
import com.example.megas.todolist2.Database.Database;

import java.util.ArrayList;
import java.util.List;

public class SyncQueueDAO {
    SQLiteDatabase database;

    public SyncQueueDAO(Context context) {
        Database data = new Database(context);
        database = data.Open();
    }

    public void AddQueue(SyncQueueDTO syncQueueDTO) {

        List<SyncQueueDTO> list = getList();

        for (SyncQueueDTO s : list) {
            if (s.getEventId() == syncQueueDTO.getEventId()) {
                updateType(syncQueueDTO.getId(),syncQueueDTO.getType());
                return;
            }
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.TB_SYNCQUEUES_EVENTID, syncQueueDTO.getEventId());
        contentValues.put(Database.TB_SYNCQUEUES_STATUS, 0);
        contentValues.put(Database.TB_SYNCQUEUES_TYPE, syncQueueDTO.getType());

        database.insert(Database.TB_SYNCQUEUES, null, contentValues);
    }

    public List<SyncQueueDTO> getList() {
        List<SyncQueueDTO> syncQueueDTOList = new ArrayList<>();

        String query = "SELECT * FROM " + Database.TB_SYNCQUEUES + " WHERE " + Database.TB_SYNCQUEUES_STATUS + "=0";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(Database.TB_SYNCQUEUES_ID));
            int evenId = cursor.getInt(cursor.getColumnIndex(Database.TB_SYNCQUEUES_EVENTID));
            int status = cursor.getInt(cursor.getColumnIndex(Database.TB_SYNCQUEUES_STATUS));
            int type = cursor.getInt(cursor.getColumnIndex(Database.TB_SYNCQUEUES_TYPE));

            SyncQueueDTO syncQueueDTO = new SyncQueueDTO(id, evenId, status, type);

            syncQueueDTOList.add(syncQueueDTO);

            cursor.moveToNext();
        }

        return syncQueueDTOList;
    }

    public void setSynced(int id) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.TB_SYNCQUEUES_STATUS, 1);

        database.update(Database.TB_SYNCQUEUES, contentValues, Database.TB_SYNCQUEUES_ID + "=" + id, null);
    }

    public void updateType(int id, int type) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.TB_SYNCQUEUES_TYPE, type);

        database.update(Database.TB_SYNCQUEUES, contentValues, Database.TB_SYNCQUEUES_ID + "=" + id, null);
    }
}
