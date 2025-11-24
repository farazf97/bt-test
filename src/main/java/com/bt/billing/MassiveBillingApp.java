package billing;

import java.io.File;
import java.nio.file.Files;

import billing.core.*;
import billing.process.*;
import billing.sort.*;

public class MassiveBillingApp {
    public static void main(String[] args) {
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

            BillingCalculator calculator = new BillingCalculator(scanner.getGlobalEarliest(),
                    scanner.getGlobalLatest());

            BillingReport report = calculator.calculate(sortedFile);
            report.print();

            Files.deleteIfExists(sortedFile.toPath());

        } catch (Exception ex) {
            System.err.println("Fatal error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
