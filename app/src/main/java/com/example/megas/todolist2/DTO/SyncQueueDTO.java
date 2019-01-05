package com.example.megas.todolist2.DTO;

public class SyncQueueDTO {
    int id, eventId, status, type;

    public SyncQueueDTO(int id, int eventId, int status, int type) {
        this.id = id;
        this.eventId = eventId;
        this.status = status;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
