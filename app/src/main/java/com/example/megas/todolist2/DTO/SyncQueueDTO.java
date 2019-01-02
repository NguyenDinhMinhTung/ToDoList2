package com.example.megas.todolist2.DTO;

public class SyncQueueDTO {
    int id, eventId, status;

    public SyncQueueDTO(int id, int eventId, int status) {
        this.id = id;
        this.eventId = eventId;
        this.status = status;
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
