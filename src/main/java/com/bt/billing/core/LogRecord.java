package billing.core;

import java.time.LocalTime;

public final class LogRecord {

    public enum SessionEvent {
        START, END
    }

    private final LocalTime timestamp;
    private final String username;
    private final SessionEvent event;

    public LogRecord(LocalTime timestamp, String username, SessionEvent event) {
        this.timestamp = timestamp;
        this.username = username;
        this.event = event;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public SessionEvent getEvent() {
        return event;
    }
}
