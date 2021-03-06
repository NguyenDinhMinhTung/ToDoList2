package com.example.megas.todolist2;

import android.content.Context;

import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DAO.SyncQueueDAO;
import com.example.megas.todolist2.DTO.EventDTO;
import com.example.megas.todolist2.DTO.SyncQueueDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Sync {
    public static String hostIp = "10.228.130.69";
    public static String push_url = "http://" + hostIp + "/push_data.php";
    public static String get_url = "http://" + hostIp + "/get_data.php";
    public static String check_version_url = "http://" + hostIp + "/check_version.php";

    public static void PushToSyncQueue(Context context, int id, int type) {
        SyncQueueDAO syncQueueDAO = new SyncQueueDAO(context);

        syncQueueDAO.AddQueue(new SyncQueueDTO(0, id, 0, type));
    }

    public static void StartSyncFromServer(Context context, _Date date, final Action action) {
        String url = String.format("%s?date=%s", get_url, date.toString());
        final EventsDAO eventsDAO = new EventsDAO(context);

        new HttpRequest2(new HttpRequest2.Action() {
            @Override
            public void action(String s) {
                String[] data = s.split("</br>");
                if (data[0].equals("SUCCESS") && data[data.length - 1].equals("END")) {
                    eventsDAO.deleteAll();

                    for (int i = 1; i < data.length - 1; i++) {
                        String[] eventItem = data[i].split("\\|");

                        int eventId = Integer.parseInt(eventItem[0]);
                        String eventName = eventItem[1];
                        int type = Integer.parseInt(eventItem[2]);
                        String daytime = eventItem[3];
                        int notiDay = Integer.parseInt(eventItem[4]);
                        int status = Integer.parseInt(eventItem[5]);
                        int color = Integer.parseInt(eventItem[6]);
                        int objectId = Integer.parseInt(eventItem[7]);

                        String comment = "";
                        if (eventItem.length > 8) comment = eventItem[8];

                        EventDTO eventDTO = new EventDTO(0, eventId, type, daytime, notiDay, status, color, objectId, eventName, comment);
                        eventsDAO.addEvent(eventDTO);
                    }

                    action.Action();
                }
            }
        }).execute(url);
    }

    public static int startSyncToServer2(Context context) {
        final SyncQueueDAO syncQueueDAO = new SyncQueueDAO(context);
        EventsDAO eventsDAO = new EventsDAO(context);

        List<SyncQueueDTO> syncQueueDTOList = syncQueueDAO.getList();

        for (final SyncQueueDTO syncQueueDTO : syncQueueDTOList) {
            if (syncQueueDTO.getType() == 1) {
                EventDTO eventDTO = eventsDAO.getEventById(syncQueueDTO.getEventId());

                if (eventDTO != null) {
                    String url = String.format("%s?evenid=%d&evenname=%s&type=%d&daytime=%s&notiday=%d&status=%d&color=%d&objectid=%d&comment=%s",
                            push_url, eventDTO.getEventId(), eventDTO.getEventName(), eventDTO.getType(), eventDTO.getDaytime(), eventDTO.getNotiday(),
                            eventDTO.getStatus(), eventDTO.getColor(), eventDTO.getObjectId(), eventDTO.getComment());

                    if (HttpRequest(url).toUpperCase().equals("OK")) {
                        syncQueueDAO.setSynced(syncQueueDTO.getId());
                    } else {
                        return 1;
                    }
                }

            } else if (syncQueueDTO.getType() == 2) {
                String url = String.format("%s?delete_id=%d",
                        push_url, syncQueueDTO.getEventId());
                if (HttpRequest(url).toUpperCase().equals("OK")) {
                    syncQueueDAO.setSynced(syncQueueDTO.getId());
                } else {
                    return 1;
                }
            }
        }

        return 0;
    }

    public static void getServerVersion() {

    }

    public static String HttpRequest(String url) {
        URLConnection connection = null;
        String result = "";

        try {
            connection = (new URL(url)).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            in.close();
            result = html.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void StartSyncToServer(Context context) {
        final SyncQueueDAO syncQueueDAO = new SyncQueueDAO(context);
        EventsDAO eventsDAO = new EventsDAO(context);

        List<SyncQueueDTO> syncQueueDTOList = syncQueueDAO.getList();

        for (final SyncQueueDTO syncQueueDTO : syncQueueDTOList) {
            if (syncQueueDTO.getType() == 1) {
                EventDTO eventDTO = eventsDAO.getEventById(syncQueueDTO.getEventId());

                if (eventDTO != null) {
                    String url = String.format("%s?evenid=%d&evenname=%s&type=%d&daytime=%s&notiday=%d&status=%d&color=%d&objectid=%d&comment=%s",
                            push_url, eventDTO.getEventId(), eventDTO.getEventName(), eventDTO.getType(), eventDTO.getDaytime(), eventDTO.getNotiday(),
                            eventDTO.getStatus(), eventDTO.getColor(), eventDTO.getObjectId(), eventDTO.getComment());

                    new HttpRequest2(new HttpRequest2.Action() {
                        @Override
                        public void action(String s) {
                            if (s.toUpperCase().equals("OK")) {
                                syncQueueDAO.setSynced(syncQueueDTO.getId());
                            }
                        }
                    }).execute(url);
                }

            } else if (syncQueueDTO.getType() == 2) {
                String url = String.format("%s?delete_id=%d",
                        push_url, syncQueueDTO.getEventId());
                new HttpRequest2(new HttpRequest2.Action() {
                    @Override
                    public void action(String s) {

                    }
                }).execute(url);
            }
        }
    }

    public interface Action {
        void Action();
    }
}
