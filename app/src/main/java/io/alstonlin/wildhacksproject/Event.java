package io.alstonlin.wildhacksproject;

public class Event {
    public String eventID, name, location, details;

    public Event(String eventID, String name, String location, String details) {
        this.eventID = eventID;
        this.name = name;
        this.location = location;
        this.details = details;
    }

    public String getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDetails() {
        return details;
    }
}
