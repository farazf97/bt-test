package billing.sort;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public final class LogLineComparator implements Comparator<String> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public int compare(String a, String b) {
        if (a == b)
            return 0;

        String[] pa = a.split("\\s+");
        String[] pb = b.split("\\s+");

        String userA = pa.length > 1 ? pa[1] : "";
        String userB = pb.length > 1 ? pb[1] : "";

        int c = userA.compareTo(userB);
        if (c != 0)
            return c;

        LocalTime tA = parse(pa[0]);
        LocalTime tB = parse(pb[0]);

        return Integer.compare(tA.toSecondOfDay(), tB.toSecondOfDay());
    }

    private LocalTime parse(String t) {
        try {
            return LocalTime.parse(t, TIME_FORMATTER);
        } catch (Exception ex) {
            return LocalTime.MIN;
        }
    }
}
