package billing.process;

import billing.core.LogRecord;
import billing.core.LogRecordFactory;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

/**
 * Calculates minimal possible billing durations.
 * Single-pass, streaming, memory efficient.
 */
public class BillingCalculator {

    private final LocalTime globalEarliest;
    private final LocalTime globalLatest;

    private final LogRecordFactory factory = new LogRecordFactory();

    public BillingCalculator(LocalTime globalEarliest, LocalTime globalLatest) {
        this.globalEarliest = globalEarliest;
        this.globalLatest = globalLatest;
    }

    public BillingReport calculate(File sortedFile) throws IOException {
        Map<String, UserSessionSummary> aggregated = new TreeMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(sortedFile))) {
            String line;
            String currentUser = null;
            Deque<LocalTime> stack = new ArrayDeque<>();

            UserSessionSummary stats = null;

            while ((line = reader.readLine()) != null) {
                LogRecord record = factory.parse(line);
                if (record == null)
                    continue;

                String user = record.getUsername();

                if (!Objects.equals(user, currentUser)) {
                    if (stats != null)
                        stats.finaliseOpenSessions(stack, globalLatest);
                    stats = aggregated.computeIfAbsent(user, k -> new UserSessionSummary());
                    stack.clear();
                    currentUser = user;
                }

                handleRecord(record, stats, stack);
            }

            if (stats != null)
                stats.finaliseOpenSessions(stack, globalLatest);
        }

        return new BillingReport(aggregated);
    }

    private void handleRecord(LogRecord record,
            UserSessionSummary stats,
            Deque<LocalTime> stack) {

        if (record.getEvent() == LogRecord.SessionEvent.START) {
            stack.push(record.getTimestamp());
        } else {
            // END event
            LocalTime start = stack.isEmpty() ? globalEarliest : stack.pop();
            long duration = Math.max(0,
                    record.getTimestamp().toSecondOfDay() - start.toSecondOfDay());

            stats.incrementSessions();
            stats.addDuration(duration);
        }
    }
}
