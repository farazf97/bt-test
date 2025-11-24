package billing;

import java.io.File;
import billing.core.*;
import billing.sort.*;

public class MassiveBillingApp {
    public static void main(String[] args) {
        System.out.println("MassiveBillingApp started");
        if (args.length != 1) {
            System.err.println("Usage: java MassiveBillingApp <logFilePath>");
            return;
        }

        File input = new File(args[0]);
        if (!input.exists() || !input.isFile()) {
            System.err.println("Invalid file: " + args[0]);
            return;
        }

        try {
            LogFileScanner scanner = new LogFileScanner();
            scanner.scan(input);

            // File based merge sort to handle extremely massive files in chunks
            LogFileSorter sorter = new LogFileSorter();
            File sortedFile = sorter.sort(input);

        } catch (Exception ex) {
            System.err.println("Fatal error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
