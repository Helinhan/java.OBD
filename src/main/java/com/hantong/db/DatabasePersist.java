package com.hantong.db;

public abstract class DatabasePersist {
    public IEventPersist getEvent() {
        return event;
    }

    public void setEvent(IEventPersist event) {
        this.event = event;
    }

    private IEventPersist event;

}
