package billing.process;

import java.util.Map;

public class BillingReport {

    private final Map<String, UserSessionSummary> stats;

    public BillingReport(Map<String, UserSessionSummary> stats) {
        this.stats = stats;
    }

    public void print() {
        stats.forEach((user, s) -> System.out.println(user + " " + s.getSessionCount() + " " + s.getTotalSeconds()));
    }
}
