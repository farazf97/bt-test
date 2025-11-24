# MassiveBillingApp

## How to Compile
javac -d bin -cp lib/junit-standalone.jar src/**/*.java

## How to Run
The program requires 1 argument: [filename]

Example:
java -cp bin billing.MassiveBillingApp src/test/java/com/bt/billing/logs/deep_nesting.txt

To Run tests:
java -jar lib/junit-standalone.jar -cp bin -c test.java.com.bt.billing.MassiveBillingAppTest