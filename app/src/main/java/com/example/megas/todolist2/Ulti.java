package com.example.megas.todolist2;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DTO.EventDTO;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Ulti {
    public static final int NOTIFICATION_ID = 111;

    public static final String SHARED_PREFERENCES_NAME="todolist";

    public static int[] colors = {Color.rgb(255, 255, 255),
            Color.rgb(255, 112, 67),
            Color.rgb(156, 204, 101),
            Color.rgb(253, 216, 53),
            Color.rgb(186, 104, 200)};

    public static String numToString(int num, int length) {
        String result = String.valueOf(num);

        for (int i = 0; i < length - result.length(); i++) {
            result = "0" + result;
        }

        return result;
    }

    public static _Date addDay(_Date date, int day) {
        Date tmp = date.toDate();
        Calendar c = Calendar.getInstance();
        c.setTime(tmp);
        c.add(Calendar.DATE, day);
        return new _Date(c.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static String getEvent(Context context) {
        EventsDAO eventsDAO = new EventsDAO(context);

        List<String> listResult = new ArrayList<>();
        String result = "";

        _Date now = new _Date();
        now.setHour(0);
        now.setMinute(0);
        now.setSecond(0);

        List<EventDTO> list = eventsDAO.getListEvent(now);

        if (list.size() > 0) {
            list.sort(new Comparator<EventDTO>() {
                @Override
                public int compare(EventDTO eventDTO1, EventDTO eventDTO2) {
                    _Date dt1 = Ulti.addDay(_Date.Parse(eventDTO1.getDaytime()), -eventDTO1.getNotiday());
                    _Date dt2 = Ulti.addDay(_Date.Parse(eventDTO2.getDaytime()), -eventDTO2.getNotiday());

                    return dt1.compareTo(dt2);
                }
            });


            for (EventDTO eventDTO : list) {
                if (Ulti.addDay(_Date.Parse(eventDTO.getDaytime()), -eventDTO.getNotiday()).toDateString().compareTo(now.toDateString()) > 0) {
                    break;
                }

                if (eventDTO.getStatus() == 1) continue;
                listResult.add(eventDTO.getEventName());
            }

            if (listResult.size() > 0) {
                for (int i = 0; i < listResult.size() - 1; i++) {
                    result += listResult.get(i) + System.lineSeparator();
                }

                result += listResult.get(listResult.size() - 1);
            }
        }

        if (result.isEmpty()) result = "今日は行事がありません";
        return result;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void pushNotification(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_to_do_list)
                        .setContentTitle(new SimpleDateFormat("MM月dd日").format(new Date()))
                        .setContentText("Much longer text that cannot fit one line...")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getEvent(context)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
// Creates an explicit intent for an Activity in your app
        //mBuilder.setOngoing(true);
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
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
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //cancelNotification(mNotificationManager);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public static void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
