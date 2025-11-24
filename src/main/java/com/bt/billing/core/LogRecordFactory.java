package billing.core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Factory responsible for creating LogRecord objects
 * - silently filters invalid lines.
 */
public class LogRecordFactory {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogRecord parse(String line) {
        if (line == null || line.isBlank())
            return null;

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3)
            return null;

        try {
            LocalTime ts = LocalTime.parse(parts[0], FORMATTER);
            String user = parts[1];

            LogRecord.SessionEvent event = "start".equalsIgnoreCase(parts[2]) ? LogRecord.SessionEvent.START
                    : "end".equalsIgnoreCase(parts[2]) ? LogRecord.SessionEvent.END : null;

            if (event == null)
                return null;

            return new LogRecord(ts, user, event);

        } catch (Exception e) {
            return null; // silently ignore per requirements
        }
    }
}
