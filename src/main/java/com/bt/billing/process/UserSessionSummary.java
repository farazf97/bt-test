package billing.process;

import java.time.LocalTime;
import java.util.Deque;

public class UserSessionSummary {

    private int sessionCount;
    private long totalSeconds;

    public void incrementSessions() {
        sessionCount++;
    }

    public void addDuration(long seconds) {
        totalSeconds += seconds;
    }

    public void finaliseOpenSessions(Deque<LocalTime> stack, LocalTime globalLatest) {
        while (!stack.isEmpty()) {
            LocalTime start = stack.pop();
            long duration = Math.max(0, globalLatest.toSecondOfDay() - start.toSecondOfDay());
            totalSeconds += duration;
            sessionCount++;
        }
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }
}
