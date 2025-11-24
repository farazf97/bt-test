package billing.core;

import java.io.*;
import java.time.LocalTime;

/**
 * Scans input file only for globalEarliest and globalLatest timestamps.
 */
public class LogFileScanner {

    private LocalTime globalEarliest = LocalTime.MAX;
    private LocalTime globalLatest = LocalTime.MIN;

    private final LogRecordFactory factory = new LogRecordFactory();

    public void scan(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogRecord record = factory.parse(line);
                if (record == null)
                    continue;

                LocalTime time = record.getTimestamp();
                if (time.isBefore(globalEarliest))
                    globalEarliest = time;
                if (time.isAfter(globalLatest))
                    globalLatest = time;
            }
        }
    }

    public LocalTime getGlobalEarliest() {
        return globalEarliest;
    }

    public LocalTime getGlobalLatest() {
        return globalLatest;
    }
}
