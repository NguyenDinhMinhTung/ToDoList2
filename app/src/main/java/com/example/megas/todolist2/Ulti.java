package com.example.megas.todolist2;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public static final String SHARED_PREFERENCES_NAME = "todolist";
    public static final String IS_SHOW_NOTIFICATION = "isShowNotification";


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

                long dayRemaining;
                String remaining = "";

                Date date1 = _Date.Parse(eventDTO.getDaytime()).toDate();
                Date date2 = Calendar.getInstance().getTime();
                long diff = date1.getTime() - date2.getTime();
                dayRemaining = (long) (diff / (1000.0 * 60 * 60 * 24));

                if (dayRemaining > 0) {
                    remaining = ("（後" + dayRemaining + "日）");
                } else {
                    remaining = "（今日）";
                }
                listResult.add("・"+eventDTO.getEventName() + remaining);
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
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            //mChannel.setDescription(Description);
            mChannel.enableLights(false);
            //mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(false);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            mChannel.setSound(null, null);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        String even=getEvent(context);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_to_do_list)
                        .setContentTitle(new SimpleDateFormat("MM月dd日").format(new Date()))
                        .setContentText(even.split(System.lineSeparator())[0]+"...")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(even))
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

        //cancelNotification(mNotificationManager);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
