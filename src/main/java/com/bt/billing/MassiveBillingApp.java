package main.java.com.bt.billing;

import java.io.File;

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

    }
}
