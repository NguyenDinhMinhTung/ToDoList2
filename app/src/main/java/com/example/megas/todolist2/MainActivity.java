package com.example.megas.todolist2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.megas.todolist2.Adapter.MainListAdapter;
import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DTO.EventDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView lstMainList;
    MainListAdapter mainListAdapter;

    EventsDAO eventsDAO;

    FloatingActionButton fabAdd;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("To Do List");

        setVar();

        generateList();

        setEvent();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_more_vert)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(111, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateList() {
        _Date date = new _Date();
        date.setHour(0);
        date.setMinute(0);
        date.setSecond(0);

        List<EventDTO> tmpList = eventsDAO.getListEvent(date);

        if (tmpList.size() > 0) {
            tmpList.sort(new Comparator<EventDTO>() {
                @Override
                public int compare(EventDTO eventDTO1, EventDTO eventDTO2) {
                    _Date dt1=Ulti.addDay(_Date.Parse(eventDTO1.getDaytime()),-eventDTO1.getNotiday());
                    _Date dt2=Ulti.addDay(_Date.Parse(eventDTO2.getDaytime()),-eventDTO2.getNotiday());

                    return dt1.compareTo(dt2);
                }
            });

            String now = date.toDateString();

            List<List<EventDTO>> list = new ArrayList<>();
            list.add(new ArrayList<EventDTO>());
            list.get(0).add(new EventDTO(0,0,-1,new _Date().toString(),0,0,0,0,"",""));

            //llist[0].size()==1: ngày hôm nay trống
            for (EventDTO eventDTO : tmpList) {
                String d=Ulti.addDay(_Date.Parse(eventDTO.getDaytime()), -eventDTO.getNotiday()).toDateString();
                if (Ulti.addDay(_Date.Parse(eventDTO.getDaytime()), -eventDTO.getNotiday()).toDateString().compareTo(now) > 0) {
                    list.add(new ArrayList<EventDTO>());
                    now = eventDTO.getDaytime().split(" ")[0];
                }

                list.get(list.size() - 1).add(eventDTO);
            }

            mainListAdapter = new MainListAdapter(this, list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            lstMainList.setAdapter(mainListAdapter);
            lstMainList.setLayoutManager(layoutManager);
        }
    }

    private void setVar() {
        lstMainList = findViewById(R.id.lstMainList);
        fabAdd = findViewById(R.id.fabAddMainList);

        eventsDAO = new EventsDAO(this);
    }

    private void setEvent() {
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.fabAddMainList:
                Intent intent = new Intent(this, AddEventActivity.class);
                startActivity(intent);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();

        generateList();
    }
}
