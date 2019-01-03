package com.example.megas.todolist2.DTO;

import java.io.Serializable;

public class EventDTO implements Serializable {
    int id, eventId, type, notiday, status, color, objectId;
    String eventName, comment, daytime;

    public EventDTO(int id, int eventId, int type, String daytime, int notiday, int status, int color, int objectId, String eventName, String comment) {
        this.id = id;
        this.eventId = eventId;
        this.type = type;
        this.daytime = daytime;
        this.notiday = notiday;
        this.status = status;
        this.color = color;
        this.objectId = objectId;
        this.eventName = eventName;
        this.comment = comment;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNotiday() {
        return notiday;
    }

    public void setNotiday(int notiday) {
        this.notiday = notiday;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
